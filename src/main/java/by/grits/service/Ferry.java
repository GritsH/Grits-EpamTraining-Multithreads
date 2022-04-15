package by.grits.service;

import by.grits.entities.Car;
import by.grits.state.FerryState;
import by.grits.state.FerryStateType;
import by.grits.state.LoadedState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Ferry {
  private static final Logger LOGGER = LogManager.getLogger(Ferry.class);

  private FerryState state;
  private FerryStateType ferryStateType;
  private static final Integer MAX_CAPACITY = 10;
  private static Ferry INSTANCE;
  private final ReentrantLock reentrantLock;
  private final Condition loadCondition;
  private final Condition unloadCondition;
  private final Queue<Car> cars;

  private Ferry() {
    state = new LoadedState();
    reentrantLock = new ReentrantLock();
    loadCondition = reentrantLock.newCondition();
    unloadCondition = reentrantLock.newCondition();
    cars = new LinkedList<>();
  }

  public static Ferry getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Ferry();
    }
    return INSTANCE;
  }

  public void setState(FerryState state){
    this.state = state;
  }

  public FerryState getState() {
    return state;
  }

  public void previousState(){
    state.prev(this);
  }
  public void nextState(){
    state.next(this);
  }

  public void printStatus(){
    state.printStatus();
  }

  public void load(Car car) throws InterruptedException {
    reentrantLock.lock();
    try {
      int leftSpace = estimateLeftSpace();
      if (leftSpace < car.getCarSize()) {
//        nextState();
//        printStatus();
//
//        nextState();
//        printStatus();
        ferryStateType = FerryStateType.LOADED;
        unloadCondition.signal();
        loadCondition.await();
      }
      //if(getState().equals(LoadedState.class)){
        cars.add(car);
        car.setLoaded(true);
        leftSpace = estimateLeftSpace();
        LOGGER.info("Car " + car.getCarID() + " was loaded on ferry");
        LOGGER.info("Current ferry capacity = " + (leftSpace));
      //}
    } finally {
      reentrantLock.unlock();
    }
  }

  public void unload() throws InterruptedException {
    reentrantLock.lock();
    try {
      int leftSpace = estimateLeftSpace();
      if (leftSpace == MAX_CAPACITY) {
//        nextState();
//        printStatus();
//        nextState();
//        printStatus();
        ferryStateType = FerryStateType.UNLOADED;
        loadCondition.signal();
        unloadCondition.await();
      } else if (ferryStateType == FerryStateType.LOADED){
        if (cars.element().isLoaded()) {
          LOGGER.info("------------------------------------------");
          LOGGER.info("Car " + cars.element().getCarID() + " was unloaded.");
          LOGGER.info("Current ferry capacity = " + (leftSpace + cars.element().getCarSize()));
          LOGGER.info("------------------------------------------");
        }
        cars.remove();
      }
    } finally {
      reentrantLock.unlock();
    }
  }

  private int estimateLeftSpace() {
    reentrantLock.lock();
    try {
      int occupiedSpace = 0;
      for (Car car : cars) {
        occupiedSpace = occupiedSpace + car.getCarSize();
      }
      return MAX_CAPACITY - occupiedSpace;
    } finally {
      reentrantLock.unlock();
    }
  }
}
