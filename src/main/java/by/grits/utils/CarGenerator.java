package by.grits.utils;

import by.grits.entities.Car;
import by.grits.entities.CarType;
import by.grits.service.FerryService;

import java.util.Random;

public class CarGenerator implements Runnable {
  private final FerryService ferryService;

  public CarGenerator(FerryService ferryService) {
    this.ferryService = ferryService;
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
        ferryService.add(car);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
