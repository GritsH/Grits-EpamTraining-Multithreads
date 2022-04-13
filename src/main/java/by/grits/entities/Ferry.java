package by.grits.entities;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Ferry {
  private int id;
  private final int max_capacity = 10;
  private List<Car> cars;
  private boolean isFull;
  private boolean isEmpty;

  public boolean isEmpty() {
    return isEmpty;
  }

  public void setEmpty(boolean empty) {
    isEmpty = empty;
  }

  public Ferry() {
    cars = new CopyOnWriteArrayList<>();
  }

  public List<Car> getCars() {
    return cars;
  }

  public void setCars(List<Car> cars) {
    this.cars = cars;
  }

  public int getId() {
    return id;
  }

  public int getMax_capacity() {
    return max_capacity;
  }

  public boolean isFull() {
    return isFull;
  }

  public void setFull(boolean full) {
    isFull = full;
  }

  public int getMaxCapacity() {
    return max_capacity;
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }
}
