package VehicleRegistrationSystem.model;

import java.util.Objects;

public abstract class Vehicle {
    private final String plateNumber;
    private String ownerName;
    private final String vehicleType;
    private final int registrationYear;
    private String status;


    public Vehicle(String plateNumber, String ownerName, int registrationYear,String vehicleType, String status) {
        this.plateNumber = plateNumber;
        this.ownerName = ownerName;
        this.vehicleType = vehicleType;
        this.registrationYear = registrationYear;
        this.status = status;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public int getRegistrationYear() {
        return registrationYear;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "["+ this.getPlateNumber() +"]" + " | "  + this.getVehicleType()
                + " | Owner: " + this.getOwnerName() + " | Year: " + this.getRegistrationYear()
                + " | Status: " + this.getStatus();
    }

    public abstract String getRegistrationLabel();

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || !this.getClass().equals(obj.getClass()))
            return false;

        Vehicle vehicle = (Vehicle) obj;
        return this.getPlateNumber().equalsIgnoreCase(vehicle.getPlateNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlateNumber().toLowerCase());
    }

}
