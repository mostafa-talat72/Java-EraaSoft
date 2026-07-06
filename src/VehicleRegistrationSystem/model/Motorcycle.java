package VehicleRegistrationSystem.model;

public class Motorcycle extends Vehicle{
    private String engineType;

    public Motorcycle(String plateNumber, String ownerName, int registrationYear, String status, String engineType) {
        super(plateNumber, ownerName, registrationYear, "Motorcycle", status);
        this.setEngineType(engineType);
    }


    @Override
    public String getRegistrationLabel() {
        return "Motorcycle: \"Motorcycle — Engine: " + this.getEngineType() + "\"";
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    @Override
    public String toString() {
        return super.toString() + " | Motorcycle Engine: " + this.getEngineType();
    }
}
