package by.grits.entities;

public class Car {
  private int carID;
  private CarType carType;
  private int carSize;
  private boolean isLoaded;

  public Car() {}

  public boolean isLoaded() {
    return isLoaded;
  }

  public void setLoaded(boolean loaded) {
    isLoaded = loaded;
  }

  public int getCarID() {
    return carID;
  }

  public void setCarID(int carID) {
    this.carID = carID;
  }

  public CarType getCarType() {
    return carType;
  }

  public void setCarType(CarType carType) {
    this.carType = carType;
  }

  public int getCarSize() {
    return carSize;
  }

  public void setCarSize(int carSize) {
    this.carSize = carSize;
  }
}
