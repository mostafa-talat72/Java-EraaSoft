package EqualsAndHashCode.Task4;

import java.util.Objects;

public class Car {
    private int plateNumber;
    private String color;

    public Car() {
    }

    public Car(int plateNumber) {
        this.setPlateNumber(plateNumber);
    }

    public Car(int plateNumber, String color) {
        this.setPlateNumber(plateNumber);
        this.setColor(color);
    }

    public int getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(int plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        if(!this.getClass().equals(obj.getClass()))
            return false;

        Car car = (Car) obj;
        return this.getPlateNumber() == car.getPlateNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPlateNumber());
    }

    @Override
    public String toString() {
        return "(plateNumber: " + this.getPlateNumber() + ", color: " + this.getColor() +") ";
    }
}
