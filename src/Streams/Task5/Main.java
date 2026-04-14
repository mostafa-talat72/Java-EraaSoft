package Streams.Task5;

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

        List<String> flattenList = nestedWords.stream().flatMap(list -> list.stream()).collect(Collectors.toList());
        System.out.println("Flatten List: " + flattenList);

        Set<Character> distinctCharacter = nestedWords.stream().flatMap(list -> list.stream())
                .filter(word -> word != null).flatMap(word -> word.chars().mapToObj(c -> (char) c)).collect(Collectors.toSet());
        System.out.println("Distincat Character: " + distinctCharacter);

        List<Optional<String>> optionalsList = names.stream().map(name -> Optional.ofNullable(name)).collect(Collectors.toList());
        List<String> nonEmptyValues = optionalsList.stream().flatMap(list -> list.stream()).filter(name -> !name.isEmpty()).collect(Collectors.toList());
        System.out.println("Non empty values: " + nonEmptyValues);

        Map<Integer, List<String>> mapListOfLength = nestedWords.stream().flatMap(list -> list.stream()).collect(Collectors.groupingBy(string -> string.length()));
        System.out.println("Map a list of strings to their lengths: " + mapListOfLength);

        List<String> uppercasedStringStartWithA = nestedWords.stream().flatMap(list -> list.stream()).filter(string -> string.charAt(0) == 'A' || string.charAt(0) == 'a').map(string -> string.toUpperCase()).collect(Collectors.toList());
        System.out.println("List of uppercased words that start with “A”: " + uppercasedStringStartWithA);


    }
}
