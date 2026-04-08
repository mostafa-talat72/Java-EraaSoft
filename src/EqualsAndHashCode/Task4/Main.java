package EqualsAndHashCode.Task4;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        HashSet<Product> products = new HashSet<>();
        products.add(new Product("1", 34));
        products.add(new Product("1", 32));
        System.out.println(products);
        System.out.println("-------------------------------------------------------------");
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

        HashMap<Car, String> map = new HashMap<>();

        map.put(new Car(111, "Red"), "Owner1");
        map.put(new Car(222, "Blue"), "Owner2");
        map.put(new Car(333, "Black"), "Owner3");

        map.put(new Car(111, "Green"), "OwnerX");

        System.out.println("HashMap contents:");
        for (Car car : map.keySet()) {
            System.out.println(car + " => " + map.get(car));
        }

        Car searchCar = new Car(222, "Blue");
        System.out.println("Retrieve with new object: " + map.get(searchCar));

        Car carToModify = new Car(444, "White");
        map.put(carToModify, "Owner4");
        carToModify.setPlateNumber(999);
        System.out.println("Retrieve after modifying key: " + map.get(carToModify));

    }
}
