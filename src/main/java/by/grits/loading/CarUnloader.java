package by.grits.loading;

import by.grits.service.Ferry;

public class CarUnloader implements Runnable {
  private final Ferry ferry;

  public CarUnloader(Ferry ferry) {
    this.ferry = ferry;
  }

  @Override
  public void run() {
    while (ferry.getCounter() < 20) {
      try {
        ferry.unload();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
