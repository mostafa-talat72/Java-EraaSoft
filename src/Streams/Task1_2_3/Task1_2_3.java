package Streams.Task1_2_3;

import java.util.*;
import java.util.stream.Collectors;

public class Task1_2_3 {
    public static void main(String[] args) {
        // Create a list of integers
        List<Integer> numbers = Arrays.asList(10, 5, 3, 7, 2, 10, 5, 8, 9, 0, -3, 4);

        // Create a list of names (with empty string and null included)
        List<String> names = Arrays.asList("Ali", "Mona", "Ahmed", "Sara", "Amr", "Laila", "Kareem", "Nada", "Nour", "Samy", "", null);

        // Filter even numbers
        List<Integer> evenList = numbers.stream().filter(num -> num % 2 == 0).toList();
        System.out.println("Even numbers: " + evenList);

        // Filter names starting with 'a' or 'A'
        List<String> startWithAList = names.stream()
                .filter(name -> name != null && !name.isEmpty())
                .filter(name -> name.charAt(0) == 'a' || name.charAt(0) == 'A')
                .collect(Collectors.toList());
        System.out.println("Names start with (a, A): " + startWithAList);

        // Convert all names to uppercase (ignoring nulls)
        List<String> upperCaseList = names.stream()
                .filter(Objects::nonNull)
                .map(name -> name.toUpperCase())
                .collect(Collectors.toList());
        System.out.println("Upper Case List: " + upperCaseList);

        // Sort numbers
        List<Integer> sortedList = numbers.stream().sorted().collect(Collectors.toList());
        System.out.println("Sorted List: " + sortedList);

        // Remove duplicates
        List<Integer> distinctNumber = numbers.stream().distinct().collect(Collectors.toList());
        System.out.println("Distinct Number: " + distinctNumber);

        // Count names longer than 5 characters
        long numberOfStringsMoreThan5 = names.stream().filter(name -> name != null && name.length() > 5).count();
        System.out.println("Number of strings longer than 5 characters: " + numberOfStringsMoreThan5);

        // Find first name longer than 4 characters
        Optional<String> firstElement = names.stream().filter(name -> name != null && name.length() > 4).findFirst();
        firstElement.ifPresent(name -> System.out.println("First Element: " + name));

        // Check if any number is divisible by 5
        boolean divisibleBy5 = numbers.stream().anyMatch(number -> number % 5 == 0);
        System.out.println("Any number is divisible by 5 in a list: " + divisibleBy5);

        // Convert lists to sets
        Set<Integer> setOfNumbers = numbers.stream().collect(Collectors.toSet());
        Set<String> setOfNames = names.stream().collect(Collectors.toSet());
        System.out.println("Set of Numbers: " + setOfNumbers);
        System.out.println("Set of Names: " + setOfNames);

        // Skip first 3 elements
        List<Integer> skipFirst3Number = numbers.stream().skip(3).collect(Collectors.toList());
        List<String> skipFirst3Name = names.stream().skip(3).collect(Collectors.toList());
        System.out.println("Skip the first 3 number: " + skipFirst3Number);
        System.out.println("Skip the first 3 name: " + skipFirst3Name);

        // Sum all numbers
        int sumOfNumbers = numbers.stream().reduce(0, (num1, num2) -> num1 + num2);
        System.out.println("Sum of numbers: " + sumOfNumbers);

        // Find max and min values
        Optional<Integer> maxValue = numbers.stream().max((num1, num2) -> num1.compareTo(num2));
        Optional<Integer> minValue = numbers.stream().min((num1, num2) -> num1.compareTo(num2));
        System.out.println("Max Number: " + (maxValue.isEmpty() ? "not found" : maxValue.get()) +
                ", Min Number: " + (minValue.isEmpty() ? "not found" : minValue.get()));

        // Calculate average
        OptionalDouble averageNumber = numbers.stream().mapToInt(Integer::intValue).average();
        System.out.println("Average number: " + averageNumber.getAsDouble());

        // Multiply all numbers
        long multiplyAllNumbers = numbers.stream().reduce(1, (num1, num2) -> num1 * num2);
        System.out.println("Multiply all integers in a list: " + multiplyAllNumbers);

        // Count positive numbers
        long countPositiveNumber = numbers.stream().filter(num -> num > 0).count();
        System.out.println("Numbers of positive in a list: " + countPositiveNumber);
    }
}
