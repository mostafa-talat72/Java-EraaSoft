package Streams.Task4;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // List of integers
        List<Integer> numbers = Arrays.asList(10, 5, 3, 7, 2, 10, 5, 8, 9, 0, -3, 4);

        // List of names (includes empty string and null)
        List<String> names = Arrays.asList("Ali", "Mona", "Ahmed", "Sara", "Amr", "Laila", "Kareem", "Nada", "Nour", "Samy", "", null);

        // List of students with department and grade
        List<Student> students = Arrays.asList(
                new Student("Ali", "IT", 85),
                new Student("Mona", "CS", 92),
                new Student("Ahmed", "IT", 60),
                new Student("Sara", "CS", 70),
                new Student("Omar", "IS", 45),
                new Student("Laila", "IS", 78)
        );

        // List of employees with name, age, department, and salary
        List<Employee> employees = Arrays.asList(
                new Employee("Ali", 30, "HR", 5000),
                new Employee("Mona", 25, "IT", 7000),
                new Employee("Ahmed", 30, "HR", 5500),
                new Employee("Sara", 35, "IT", 7200),
                new Employee("Omar", 40, "Finance", 8000),
                new Employee("Laila", 35, "Finance", 8200)
        );

        // Nested list of words
        List<List<String>> nestedWords = Arrays.asList(
                Arrays.asList("Java", "Stream"),
                Arrays.asList("API", "Lambda"),
                Arrays.asList("FlatMap", "Map")
        );

        // Group students by department
        Map<String, List<Student>> groupByDepartment = students.stream()
                .collect(Collectors.groupingBy(student -> student.getDepartment()));
        System.out.println("Group a list of students by their department: " + groupByDepartment);

        // Partition numbers into even and odd
        Map<Boolean, List<Integer>> partitionByEvenOdd = numbers.stream()
                .collect(Collectors.partitioningBy(num -> num % 2 == 0));
        System.out.println("Even Number: " + partitionByEvenOdd.get(true) + ", Odd Number: " + partitionByEvenOdd.get(false));

        // Join names into a comma-separated string
        String commaSeparatedNames = names.stream()
                .filter(name -> name != null && !name.isEmpty())
                .collect(Collectors.joining(", "));
        System.out.println("Comma separated Names: " + commaSeparatedNames);

        // Group employees by age
        Map<Integer, List<Employee>> groupEmployeeByAge = employees.stream()
                .collect(Collectors.groupingBy(employee -> employee.getAge()));
        System.out.println("Number of Employee Group by Age: " + groupEmployeeByAge.size());
        System.out.println("Employee Group by Age: " + groupEmployeeByAge);

        // Calculate average salary per department
        Map<String, Double> averageSalary = employees.stream()
                .collect(Collectors.groupingBy(employee -> employee.getDepartment(),
                        Collectors.averagingDouble(employee -> employee.getSalary())));
        System.out.println("Average salary per department: " + averageSalary);
    }
}
