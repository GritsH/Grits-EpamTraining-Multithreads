package by.grits.loading;

import by.grits.queue.CarQueue;
import by.grits.service.Ferry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class CarLoader implements Runnable {
  private static final Logger LOGGER = LogManager.getLogger(CarUnloader.class);

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
        TimeUnit.SECONDS.sleep(3);
        ferry.load(carQueue.getNextCar());
      } catch (InterruptedException e) {
        LOGGER.warn("Caught exception, could not load");
      }
    }
  }
}
