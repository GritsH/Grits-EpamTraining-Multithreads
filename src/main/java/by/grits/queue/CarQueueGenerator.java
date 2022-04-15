package by.grits.queue;

import by.grits.entities.Car;
import by.grits.entities.CarType;
import by.grits.utils.FileReader;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CarQueueGenerator implements Runnable {
  private final CarQueue carQueue;
  private int carID;

  public CarQueueGenerator(CarQueue carQueue) throws FileNotFoundException {
    this.carQueue = carQueue;
    carID = 0;
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
      car.setCarID(carID++);
      carQueue.addCarToQueue(car);
    }
  }

  @Override
  public void run() {
    while(true){
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        e.printStackTrace();
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
      car.setCarID(carID++);
      carQueue.addCarToQueue(car);
    }

  }
}
