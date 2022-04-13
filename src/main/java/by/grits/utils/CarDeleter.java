package by.grits.utils;

import by.grits.service.FerryService;

public class CarDeleter implements Runnable {
  private final FerryService ferryService;

  public CarDeleter(FerryService ferryService) {
    this.ferryService = ferryService;
  }

  @Override
  public void run() {
    while (true) {
      try {
        ferryService.delete();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
