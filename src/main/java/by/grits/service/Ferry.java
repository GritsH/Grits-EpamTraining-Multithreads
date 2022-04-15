package by.grits.service;

import by.grits.entities.Car;
import by.grits.state.FerryStateType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Ferry {
  private static final Logger LOGGER = LogManager.getLogger(Ferry.class);

  private static final Integer MAX_CAPACITY = 10;
  private static Ferry INSTANCE;
  private final ReentrantLock reentrantLock;
  private final Condition loadCondition;
  private final Condition unloadCondition;
  private final Queue<Car> cars;
  // true - loading
  // false - unloading
  protected final AtomicReference<FerryStateType> state = new AtomicReference<>(FerryStateType.LOADED);

  private Ferry() {
    reentrantLock = new ReentrantLock();
    loadCondition = reentrantLock.newCondition();
    unloadCondition = reentrantLock.newCondition();
    cars = new LinkedList<>();
  }

  public static Ferry getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Ferry();
    }
    return INSTANCE;
  }

  public void load(Car car) throws InterruptedException {
    reentrantLock.lock();
    try {
      // unloading
      while (state.get() != FerryStateType.LOADED) {
        loadCondition.await();
      }
      if (estimateLeftSpace() < car.getCarSize()) {
        // set to load
        state.set(FerryStateType.UNLOADED);
        unloadCondition.signal();
      } else {
        cars.add(car);
        car.setLoaded(true);
        LOGGER.info("Car " + car.getCarID() + " was loaded on ferry");
        LOGGER.info("Current ferry capacity = " + (estimateLeftSpace()));
      }
    } catch (InterruptedException e) {
      LOGGER.warn("Something wrong");
    } finally {
      reentrantLock.unlock();
    }
  }

  public void unload() throws InterruptedException {
    reentrantLock.lock();
    try {
      // loading
      while (state.get() == FerryStateType.LOADED) {
        unloadCondition.await();
      }
      int leftSpace = estimateLeftSpace();
      if (leftSpace == MAX_CAPACITY) {
        state.set(FerryStateType.LOADED);
        loadCondition.signal();
      } else {
        if (cars.element().isLoaded()) {
          LOGGER.info("------------------------------------------");
          LOGGER.info("Car " + cars.element().getCarID() + " was unloaded.");
          LOGGER.info("Current ferry capacity = " + (leftSpace + cars.element().getCarSize()));
          LOGGER.info("------------------------------------------");
        }
        cars.remove();
      }
    } finally {
      reentrantLock.unlock();
    }
  }

  private int estimateLeftSpace() {
    reentrantLock.lock();
    try {
      int occupiedSpace = 0;
      for (Car car : cars) {
        occupiedSpace = occupiedSpace + car.getCarSize();
      }
      return MAX_CAPACITY - occupiedSpace;
    } finally {
      reentrantLock.unlock();
    }
  }
}
