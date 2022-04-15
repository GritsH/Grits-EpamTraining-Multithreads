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
      int occupiedSpace = updateOccupiedSpace();
      int leftSpace = MAX_CAPACITY - occupiedSpace;
      if (leftSpace < car.getCarSize()) {
        setLeftSpace(leftSpace);
        condition.await();
      }
      if (occupiedSpace >= 0 && occupiedSpace + car.getCarSize() <= MAX_CAPACITY) {
        cars.add(car);
        car.setLoaded(true);
        occupiedSpace = updateOccupiedSpace();
        LOGGER.info("Car " + car.getCarID() + " was loaded on ferry");
        LOGGER.info("Current ferry capacity = " + (MAX_CAPACITY - occupiedSpace));
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
      int occupiedSpace = updateOccupiedSpace();
      if (occupiedSpace == 0) {
        setLeftSpace(MAX_CAPACITY);
        condition.signal();
      }
      if (leftSpace < 2) {
        if (cars.element().isLoaded()) {
          LOGGER.info("------------------------------------------");
          LOGGER.info("Car " + cars.element().getCarID() + " was unloaded.");
          LOGGER.info(
              "Current ferry capacity = "
                  + (MAX_CAPACITY - occupiedSpace + cars.element().getCarSize()));
          LOGGER.info("------------------------------------------");
        }
        cars.remove();
      }
    } finally {
      reentrantLock.unlock();
    }
  }

  private int updateOccupiedSpace() {
    reentrantLock.lock();
    try {
      int occupiedSpace = 0;
      for (Car car : cars) {
        occupiedSpace = occupiedSpace + car.getCarSize();
      }
      return occupiedSpace;
    } finally {
      reentrantLock.unlock();
    }
  }

  private void setLeftSpace(int leftSpace){
    reentrantLock.lock();
    try{
      this.leftSpace = leftSpace;
    }finally{
      reentrantLock.unlock();
    }
  }
}
