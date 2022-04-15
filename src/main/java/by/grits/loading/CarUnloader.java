package by.grits.loading;

import by.grits.service.Ferry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class CarUnloader implements Runnable {
  private static final Logger LOGGER = LogManager.getLogger(CarUnloader.class);

  private final Ferry ferry;

  public CarUnloader(Ferry ferry) {
    this.ferry = ferry;
  }

  @Override
  public void run() {
    while (true) {
      try {
        TimeUnit.SECONDS.sleep(3);
        ferry.unload();
      } catch (InterruptedException e) {
        LOGGER.warn("Caught exception, could not unload");
      }
    }
  }
}
