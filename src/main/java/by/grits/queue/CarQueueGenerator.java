package by.grits.queue;

import by.grits.entities.Car;
import by.grits.entities.CarType;
import by.grits.loading.CarUnloader;
import by.grits.utils.FileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CarQueueGenerator implements Runnable {
  private static final Logger LOGGER = LogManager.getLogger(CarUnloader.class);

  private final CarQueue carQueue;
  private final AtomicInteger carID;

  public CarQueueGenerator(CarQueue carQueue) throws FileNotFoundException {
    carID = new AtomicInteger(0);
    this.carQueue = carQueue;
    FileReader fileReader = new FileReader();
    List<Integer> intTypes = fileReader.getCarsTypes("src/main/resources/InitFile.txt");
    for (int i : intTypes) {
      Car car = new Car();
      car.setCarType(CarType.getByIndex(i));
      if (car.getCarType() == CarType.PASSENGER) {
        car.setCarSize(1);
      } else {
        car.setCarSize(2);
      }
      car.setCarID(carID.get());
      carQueue.addCarToQueue(car);
      carID.incrementAndGet();
    }
  }

  @Override
  public void run() {
    while (true) {
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        LOGGER.warn("Caught exception, could not put thread to sleep");
      }
      Car car = new Car();
      Random random = new Random();
      int intType = random.nextInt(5) + 1;

      if (intType % 2 == 0) {
        car.setCarType(CarType.TRUCK);
        car.setCarSize(2);
      } else {
        car.setCarType(CarType.PASSENGER);
        car.setCarSize(1);
      }
      car.setCarID(carID.get());
      carQueue.addCarToQueue(car);
      carID.incrementAndGet();
    }
  }
}
