package by.grits.queue;

import by.grits.entities.Car;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class CarQueue {
  private static CarQueue INSTANCE;
  private final Queue<Car> carQueue;
  private final ReentrantLock reentrantLock;

  private CarQueue() {
    carQueue = new LinkedList<>();
    reentrantLock = new ReentrantLock();
  }

  public static CarQueue getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new CarQueue();
    }
    return INSTANCE;
  }

  public Car getNextCar() {
    reentrantLock.lock();
    try {
      return carQueue.poll();
    } finally {
      reentrantLock.unlock();
    }
  }

  public void addCarToQueue(Car car) {
    reentrantLock.lock();
    try {
      carQueue.offer(car);
    } finally {
      reentrantLock.unlock();
    }
  }
}
