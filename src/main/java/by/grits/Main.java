package by.grits;

import by.grits.service.Ferry;
import by.grits.loading.CarUnloader;
import by.grits.loading.CarLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

  public static void main(String[] args) throws FileNotFoundException {
    final Logger LOGGER = LogManager.getLogger(Main.class);
    Ferry ferry = new Ferry();
    CarLoader carGenerator = new CarLoader(ferry);
    CarUnloader carUnloader = new CarUnloader(ferry);

    int processors = Runtime.getRuntime().availableProcessors();
    LOGGER.info("Available processors = " + processors);

    ExecutorService service = Executors.newFixedThreadPool(processors);
    service.execute(carGenerator);
    service.execute(carGenerator);
    //service.execute(carDeleter);
    service.execute(carUnloader);
    service.shutdown();
    LOGGER.info(service.toString());
  }
}
