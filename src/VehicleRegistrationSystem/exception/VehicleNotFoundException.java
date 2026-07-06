package VehicleRegistrationSystem.exception;

public class VehicleNotFoundException extends RuntimeException{
    public VehicleNotFoundException(String plateNumber) {
        super("No vehicle found with plate: " + plateNumber);
    }
}
