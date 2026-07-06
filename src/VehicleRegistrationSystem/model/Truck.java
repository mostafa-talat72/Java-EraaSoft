package VehicleRegistrationSystem.model;

public class Truck extends Vehicle{

    private double cargoCapacityTons;

    public Truck(String plateNumber, String ownerName, int registrationYear, String status, double cargoCapacityTons) {
        super(plateNumber, ownerName, registrationYear, "Truck", status);
        this.setCargoCapacityTons(cargoCapacityTons);
    }


    @Override
    public String getRegistrationLabel() {
        return "Truck: \"Commercial Truck — Cargo: " + this.getCargoCapacityTons() + " tons\"";
    }

    public double getCargoCapacityTons() {
        return cargoCapacityTons;
    }

    public void setCargoCapacityTons(double cargoCapacityTons) {
        this.cargoCapacityTons = cargoCapacityTons;
    }

    @Override
    public String toString() {
        return super.toString() + " | Cargo Capacity Tons: " + this.getCargoCapacityTons() + " tons";

    }
}
