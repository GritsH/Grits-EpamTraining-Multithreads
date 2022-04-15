package by.grits;

import by.grits.loading.CarLoader;
import by.grits.loading.CarUnloader;
import by.grits.queue.CarQueue;
import by.grits.queue.CarQueueGenerator;
import by.grits.service.Ferry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  private static final Logger LOGGER = LogManager.getLogger(Main.class);

  public static void main(String[] args) throws FileNotFoundException {
    Ferry ferry = Ferry.getInstance();
    CarQueue carQueue = CarQueue.getInstance();
    CarLoader carLoader = new CarLoader(ferry, carQueue);
    CarUnloader carUnloader = new CarUnloader(ferry);
    CarQueueGenerator carQueueGenerator = new CarQueueGenerator(carQueue);

    int processors = Runtime.getRuntime().availableProcessors();
    LOGGER.info("Available processors = " + processors);

    ExecutorService service = Executors.newFixedThreadPool(processors);
    service.execute(carQueueGenerator);
    service.execute(carLoader);
    service.execute(carUnloader);
    service.shutdown();
    LOGGER.info(service.toString());
  }
}
