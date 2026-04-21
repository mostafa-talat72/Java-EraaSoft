package EqualsAndHashCode.Task4;

import java.util.Objects;

public class Car {
    private int plateNumber;
    private String color;

    // Default constructor
    public Car() {
    }

    // Constructor with plateNumber only
    public Car(int plateNumber) {
        this.setPlateNumber(plateNumber);
    }

    // Constructor with plateNumber and color
    public Car(int plateNumber, String color) {
        this.setPlateNumber(plateNumber);
        this.setColor(color);
    }

    // Getter and Setter for plateNumber
    public int getPlateNumber() {
        return plateNumber;
    }
    public void setPlateNumber(int plateNumber) {
        this.plateNumber = plateNumber;
    }

    // Getter and Setter for color
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    // equals() compares Car objects by plateNumber
    @Override
    public boolean equals(Object obj) {
        if (!this.getClass().equals(obj.getClass()))
            return false;

        Car car = (Car) obj;
        return this.getPlateNumber() == car.getPlateNumber();
    }

    // hashCode() generates hash based on plateNumber
    @Override
    public int hashCode() {
        return Objects.hash(this.getPlateNumber());
    }

    // toString() for readable output
    @Override
    public String toString() {
        return "(plateNumber: " + this.getPlateNumber() + ", color: " + this.getColor() + ") ";
    }
}
