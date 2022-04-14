package by.grits.service;

import by.grits.entities.Car;
import by.grits.entities.CarType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Ferry {
  private static final Logger LOGGER = LogManager.getLogger(Ferry.class);
  private int ferryCurrentCapacity = 10;
  private int leftSpace = 0;
  private int carCounter = 0;
  private int carID = 0;
  private final ReentrantLock reentrantLock = new ReentrantLock();
  private Condition condition = reentrantLock.newCondition();
  private List<Car> carsToLoad;

  public Ferry() {
    carsToLoad = new CopyOnWriteArrayList<>();
  }

  public void load(Car car) throws InterruptedException {
    TimeUnit.SECONDS.sleep(6);

    reentrantLock.lock();
    if (leftSpace < car.getCarSize()) {
      condition.await();
    }
    if (ferryCurrentCapacity >= 0 && 10 - ferryCurrentCapacity + car.getCarSize() <= 10) {

      car.setCarID(carID++);
      carsToLoad.add(car);
      carCounter++;
      car.setLoaded(true);
      if (car.getCarSize() == 1) {
        ferryCurrentCapacity = ferryCurrentCapacity - 1;
      }
      if (car.getCarSize() == 2) {
        ferryCurrentCapacity = ferryCurrentCapacity - 2;
      }
      LOGGER.info("Car " + car.getCarID() + " was loaded on ferry");
      LOGGER.info("Current ferry capacity = " + ferryCurrentCapacity);
      leftSpace = ferryCurrentCapacity;
    }
    reentrantLock.unlock();
  }

  public void unload() throws InterruptedException {
    TimeUnit.SECONDS.sleep(3);
    reentrantLock.lock();
    if (ferryCurrentCapacity == 10) {
      leftSpace = ferryCurrentCapacity;
      condition.signalAll();
    }
    if (leftSpace < 2) {
      Car car = carsToLoad.get(carCounter - carsToLoad.size());
      if (car.getCarType() == CarType.PASSENGER && car.isLoaded()) {
        ferryCurrentCapacity = ferryCurrentCapacity + 1;
        carsToLoad.remove(car);
        carCounter = carCounter - 1;
      }
      if (car.getCarType() == CarType.TRUCK && car.isLoaded()) {
        ferryCurrentCapacity = ferryCurrentCapacity + 2;
        carsToLoad.remove(car);
        carCounter = carCounter - 1;
      }
      LOGGER.info("------------------------------------------");
      LOGGER.info("Car " + car.getCarID() + " was unloaded.");
      LOGGER.info("Current ferry capacity = " + ferryCurrentCapacity);
      LOGGER.info("------------------------------------------");
    }
    reentrantLock.unlock();
  }

  public int getCounter() {
    return carCounter;
  }
}
