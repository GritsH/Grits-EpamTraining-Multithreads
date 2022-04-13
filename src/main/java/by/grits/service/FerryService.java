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

public class FerryService {
  private final ReentrantLock reentrantLock = new ReentrantLock();
  private Condition condition = reentrantLock.newCondition();
  private List<Car> carsToLoad;
  private int ferryCurrentCapacity = 10;
  private int carCounter = 0;
  private int carID = 0;

  private static final Logger LOGGER = LogManager.getLogger(FerryService.class);

  public FerryService() {
    carsToLoad = new CopyOnWriteArrayList<>();
  }

  public void add(Car car) throws InterruptedException {
    TimeUnit.SECONDS.sleep(1);
    reentrantLock.lock();

    if (ferryCurrentCapacity == 0) {
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
    }
    reentrantLock.unlock();

  }

  public void delete() throws InterruptedException {
    reentrantLock.lock();
    if (ferryCurrentCapacity != 10){
      condition.await();
    }
      if (ferryCurrentCapacity == 0) {
        TimeUnit.SECONDS.sleep(1);
        int buff = carCounter - 1;
        if (ferryCurrentCapacity <= 10) {
          Car car = carsToLoad.get(buff);
          // for (Car car_ : carsToLoad) {
          if (car.getCarType() == CarType.PASSENGER && car.isLoaded()) {
            ferryCurrentCapacity = ferryCurrentCapacity + 1;
            carsToLoad.remove(car);
          }
          if (car.getCarType() == CarType.TRUCK && car.isLoaded()) {
            ferryCurrentCapacity = ferryCurrentCapacity + 2;
            carsToLoad.remove(car);
            carCounter--;
          }
          LOGGER.info("Car " + car.getCarID() + " was unloaded.");
          // }
        }
      }
    reentrantLock.unlock();
  }
}
