package by.grits.entities;

public enum CarType {
  PASSENGER(0),
  TRUCK(1);

  int type;

  CarType(int type) {
    this.type = type;
  }

  public static CarType getByIndex(int type) {
    for (CarType value : CarType.values()) {
      if (value.type == type) {
        return value;
      }
    }
    return null;
  }
}
