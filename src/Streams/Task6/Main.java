package Streams.Task6;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(10, 5, 3, 7, 2, 10, 5, 8, 9, 0, -3, 4);
        List<String> names = Arrays.asList("Ali", "Mona", "Ahmed", "Sara", "Amr", "Laila", "Kareem", "Nada", "Nour", "Samy", "", null);

        List<Student> students = Arrays.asList(
                new Student("Ali", "IT", 85),
                new Student("Mona", "CS", 92),
                new Student("Ahmed", "IT", 60),
                new Student("Sara", "CS", 70),
                new Student("Omar", "IS", 45),
                new Student("Laila", "IS", 78)
        );

        List<Employee> employees = Arrays.asList(
                new Employee("Ali", 30, "HR", 5000),
                new Employee("Mona", 25, "IT", 7000),
                new Employee("Ahmed", 30, "HR", 5500),
                new Employee("Sara", 35, "IT", 7200),
                new Employee("Omar", 40, "Finance", 8000),
                new Employee("Laila", 35, "Finance", 8200)
        );

        List<List<String>> nestedWords = Arrays.asList(
                Arrays.asList("Java", "Stream"),
                Arrays.asList("API", "Lambda"),
                Arrays.asList("FlatMap", "Map")
        );

        List<Employee> employeeListSorted = employees.stream()
                .sorted(
                        Comparator.comparing(Employee::getSalary)
                                .thenComparing(Employee::getName)
                )
                .collect(Collectors.toList());
        System.out.println("Sort a list of employees by salary then by name: " + employeeListSorted);


        int highestNumber = numbers.stream().distinct().sorted(Comparator.reverseOrder()).skip(1).findFirst().get();
        System.out.println("The second highest number in a list: " + highestNumber);

        Set<Integer> duplicateElements = numbers.stream().collect(Collectors.groupingBy(n -> n,Collectors.counting()))
                .entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
                .collect(Collectors.toSet());
        System.out.println("Duplicate elements in a list of integers: " + duplicateElements);

        List<String> nonEmptyNames = names.stream().filter(name -> name != null && !name.isEmpty()).collect(Collectors.toList());
        System.out.println("Remove null or empty strings from a list: " +nonEmptyNames );

        Map<Boolean, List<Student>> passOrFailStudentPartitionMap = students.stream().collect(Collectors.partitioningBy(student -> student.getGrade() > 50));
        System.out.println("Partition students into pass/fail groups based on grade: ");
        System.out.println("\tPassing Students: " + passOrFailStudentPartitionMap.get(true));
        System.out.println("\tFailing Students: " + passOrFailStudentPartitionMap.get(false));

    }
}
