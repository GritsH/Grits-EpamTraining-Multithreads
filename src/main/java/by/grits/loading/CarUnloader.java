package by.grits.loading;

import by.grits.service.Ferry;

import java.util.concurrent.TimeUnit;

public class CarUnloader implements Runnable {
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
        e.printStackTrace();
      }
    }
  }
}
