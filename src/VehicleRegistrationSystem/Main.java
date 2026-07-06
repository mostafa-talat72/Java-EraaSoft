package VehicleRegistrationSystem;

import VehicleRegistrationSystem.model.Car;
import VehicleRegistrationSystem.model.Motorcycle;
import VehicleRegistrationSystem.model.Truck;
import VehicleRegistrationSystem.model.Vehicle;
import VehicleRegistrationSystem.service.RegistrationService;
import VehicleRegistrationSystem.service.VehicleRegistrationSystem;

public class Main {
    public static void main(String[] args) {
        new VehicleRegistrationSystem().start();
    }
}