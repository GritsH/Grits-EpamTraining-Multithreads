package by.grits.loading;

import by.grits.queue.CarQueue;
import by.grits.service.Ferry;

import java.util.concurrent.TimeUnit;

public class CarLoader implements Runnable {
  private final Ferry ferry;
  private final CarQueue carQueue;

  public CarLoader(Ferry ferry, CarQueue carQueue) {
    this.ferry = ferry;
    this.carQueue = carQueue;
  }

  @Override
  public void run() {
    while (true) {
      try {
        TimeUnit.SECONDS.sleep(5);
        ferry.load(carQueue.getNextCar());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
