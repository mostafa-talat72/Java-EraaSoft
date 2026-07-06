package VehicleRegistrationSystem.service;

import VehicleRegistrationSystem.exception.DuplicatePlateException;
import VehicleRegistrationSystem.exception.VehicleNotFoundException;
import VehicleRegistrationSystem.model.Vehicle;

import java.util.*;

public class RegistrationService {
    private final List<Vehicle> vehicleList = new ArrayList<>();
    private final Map<String, Vehicle> plateIndex = new HashMap<>();
    private final Set<String> registeredPlates = new HashSet<>();

    public void registerVehicle(Vehicle v){
        String plateNumber = v.getPlateNumber().toUpperCase();
        if(registeredPlates.contains(plateNumber))
            throw new DuplicatePlateException(plateNumber);
        vehicleList.add(v);
        plateIndex.put(plateNumber,v);
        registeredPlates.add(plateNumber);
        System.out.println("✅ Registration successfully.");
    }

    public Vehicle findByPlate(String plate){
        Vehicle vehicle = vehicleList.stream()
                .filter(v-> v.getPlateNumber().equalsIgnoreCase(plate))
                .findAny().orElse(null);
        if(vehicle == null)
            throw new VehicleNotFoundException(plate);
        return vehicle;
    }

    public void deleteVehicle(String plate){
        Vehicle vehicle;
        try {
            vehicle = findByPlate(plate);
        }catch (VehicleNotFoundException vehicleNotFoundException)
        {
            System.out.println(vehicleNotFoundException.getMessage());
            return;
        }
        vehicleList.remove(vehicle);
        plateIndex.remove(vehicle.getPlateNumber().toUpperCase(), vehicle);
        registeredPlates.remove(vehicle.getPlateNumber().toUpperCase());
        System.out.println("✅ Deleted successfully.");
    }

    public void updateOwner(String plate, String newOwner){
        Vehicle vehicle;
        try {
            vehicle = findByPlate(plate);
        }catch (VehicleNotFoundException vehicleNotFoundException)
        {
            System.out.println(vehicleNotFoundException.getMessage());
            return;
        }
        vehicle.setOwnerName(newOwner);
        System.out.println("✅ Updated successfully.");
    }

    public List<Vehicle> getAllVehicles(){
        return Collections.unmodifiableList(vehicleList);
    }

}
