package by.grits.loading;

import by.grits.entities.Car;
import by.grits.entities.CarType;
import by.grits.service.Ferry;
import by.grits.utils.FileReader;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class CarLoader implements Runnable {
  private final Ferry ferry;
  FileReader fileReader;
  List<Integer> intTypes;
  int i = 0;
  ReentrantLock reentrantLock = new ReentrantLock();

  public CarLoader(Ferry ferry) {
    this.ferry = ferry;
  }

  @Override
  public void run() {
    while (true) {
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

      try {
        ferry.load(car);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
