package VehicleRegistrationSystem.model;

public class Car extends Vehicle{
    private int numberOfDoors = 4;

    public Car(String plateNumber, String ownerName, int registrationYear, String status, int numberOfDoors) {
        super(plateNumber, ownerName, registrationYear, "Car", status);
        this.setNumberOfDoors(numberOfDoors);
    }


    @Override
    public String getRegistrationLabel() {
        return "Car: \"Passenger Car — Doors: " + this.getNumberOfDoors() + "\"";
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    @Override
    public String toString() {
        return super.toString() + " | Number Of Doors: " + this.getNumberOfDoors();
    }
}
