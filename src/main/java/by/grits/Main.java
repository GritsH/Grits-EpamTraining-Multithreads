package by.grits;

import by.grits.service.FerryService;
import by.grits.utils.CarDeleter;
import by.grits.utils.CarGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

  public static void main(String[] args) {
    final Logger LOGGER = LogManager.getLogger(Main.class);
    FerryService ferryService = new FerryService();
    CarGenerator carGenerator = new CarGenerator(ferryService);
    CarDeleter carDeleter = new CarDeleter(ferryService);

    int processors = Runtime.getRuntime().availableProcessors();
    LOGGER.info("Available processors = " + processors);

    ExecutorService service = Executors.newFixedThreadPool(processors);
    service.execute(carGenerator);
    service.execute(carDeleter);
    service.shutdown();
    LOGGER.info(service.toString());
  }
}
