package EqualsAndHashCode.Task4;

import java.util.HashMap;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        // HashSet of Product objects (duplicates based on code will be ignored)
        HashSet<Product> products = new HashSet<>();
        products.add(new Product("1", 34));
        products.add(new Product("1", 32));
        System.out.println(products);

        System.out.println("-------------------------------------------------------------");

        // HashSet of Student objects (duplicates based on id will be ignored)
        HashSet<Student> students = new HashSet<>();
        students.add(new Student(1, "Ahmed"));
        students.add(new Student(1, "Ali"));
        students.add(new Student(2, "Sara"));
        students.add(new Student(3, "Omar"));
        students.add(new Student(3, "Nada"));
        students.add(new Student(4, "Mona"));
        students.add(new Student(5, "Youssef"));
        students.add(new Student(5, "Hassan"));
        students.add(new Student(6, "Salma"));
        students.add(new Student(7, "Khaled"));

        System.out.println(students);

        System.out.println("-------------------------------------------------------------");

        // HashMap with Car objects as keys
        HashMap<Car, String> map = new HashMap<>();
        map.put(new Car(111, "Red"), "Owner1");
        map.put(new Car(222, "Blue"), "Owner2");
        map.put(new Car(333, "Black"), "Owner3");

        // Adding another Car with same plateNumber (111) but different color → overwrites Owner1
        map.put(new Car(111, "Green"), "OwnerX");

        // Print HashMap contents
        System.out.println("HashMap contents:");
        for (Car car : map.keySet()) {
            System.out.println(car + " => " + map.get(car));
        }

        // Retrieve value using a new Car object with same plateNumber
        Car searchCar = new Car(222, "Blue");
        System.out.println("Retrieve with new object: " + map.get(searchCar));

        // Demonstrate issue when modifying key after insertion
        Car carToModify = new Car(444, "White");
        map.put(carToModify, "Owner4");
        carToModify.setPlateNumber(999); // Modifying key after insertion
        System.out.println("Retrieve after modifying key: " + map.get(carToModify));
    }
}
