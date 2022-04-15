package by.grits.service;

import by.grits.entities.Car;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Ferry {
  private static final Logger LOGGER = LogManager.getLogger(Ferry.class);

  private static final Integer MAX_CAPACITY = 10;
  private static Ferry INSTANCE;
  private final ReentrantLock reentrantLock;
  private final Condition condition;
  private final Queue<Car> cars;
  private int leftSpace;

  private Ferry() {
    reentrantLock = new ReentrantLock();
    condition = reentrantLock.newCondition();
    cars = new LinkedList<>();
    leftSpace = MAX_CAPACITY;
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
      int leftSpace = estimateLeftSpace();
      if (leftSpace < car.getCarSize()) {
        // setLeftSpace(leftSpace);
        condition.await();
      }
      cars.add(car);
      car.setLoaded(true);
      leftSpace = estimateLeftSpace();
      LOGGER.info("Car " + car.getCarID() + " was loaded on ferry");
      LOGGER.info("Current ferry capacity = " + (leftSpace));
    } catch (InterruptedException e) {
      LOGGER.warn("Something wrong");
    } finally {
      reentrantLock.unlock();
    }
  }

  public void unload() throws InterruptedException {
    reentrantLock.lock();
    try {
      int leftSpace = estimateLeftSpace();
      if (leftSpace == MAX_CAPACITY) {
        condition.signal();
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
