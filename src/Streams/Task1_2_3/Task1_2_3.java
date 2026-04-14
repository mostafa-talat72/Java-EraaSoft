package Streams.Task1_2_3;

import java.util.*;
import java.util.stream.Collectors;

public class Task1_2_3 {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(10, 5, 3, 7, 2, 10, 5, 8, 9, 0, -3, 4);
        List<String> names = Arrays.asList("Ali", "Mona", "Ahmed", "Sara", "Amr", "Laila", "Kareem", "Nada", "Nour", "Samy", "", null);

       List<Integer> evenList = numbers.stream().filter(num -> num%2==0).toList();

       System.out.println("Even numbers: " + evenList);

       List<String> startWithAList = names.stream().filter(name -> name != null && !name.isEmpty()).filter(name -> name.charAt(0) == 'a' || name.charAt(0) == 'A').collect(Collectors.toList());

       System.out.println("Names start with (a, A): " + startWithAList);

        List<String> upperCaseList = names.stream().filter(Objects::nonNull).map(name-> name.toUpperCase()).collect(Collectors.toList());
        System.out.println("Upper Case List: " + upperCaseList);

        List<Integer> sortedList = numbers.stream().sorted().collect(Collectors.toList());
        System.out.println("Sorted List: " + sortedList);

        List<Integer> distinctNumber = numbers.stream().distinct().collect(Collectors.toList());
        System.out.println("Distinct Number: " + distinctNumber);

        long numberOfStringsMoreThan5 = names.stream().filter(name -> name != null && name.length() > 5).count();
        System.out.println("Number of strings longer than 5 characters: " + numberOfStringsMoreThan5);

        Optional<String> firstElement = names.stream().filter(name -> name != null && name.length() > 4).findFirst();
        firstElement.ifPresent( name -> System.out.println("First Element: " + name));

        boolean divisibleBy5 = numbers.stream().anyMatch(number -> number % 5 ==0);
        System.out.println("Any number is divisible by 5 in a list: " + divisibleBy5);

        Set<Integer> setOfNumbers = numbers.stream().collect(Collectors.toSet());
        Set<String> setOfNames = names.stream().collect(Collectors.toSet());
        System.out.println("Set of Numbers: " + setOfNumbers);
        System.out.println("Set of Names: " + setOfNames);

        List<Integer> skipFirst3Number = numbers.stream().skip(3).collect(Collectors.toList());
        List<String> skipFirst3Name = names.stream().skip(3).collect(Collectors.toList());
        System.out.println("Skip the first 3 number: " + skipFirst3Number);
        System.out.println("Skip the first 3 name: " + skipFirst3Name);

        int sumOfNumbers = numbers.stream().reduce(0,(num1,num2) -> num1 + num2 );
        System.out.println("Sum of numbers: " + sumOfNumbers);

        Optional<Integer> maxValue = numbers.stream().max((num1,num2) -> num1.compareTo(num2));
        Optional<Integer> minValue = numbers.stream().min((num1, num2) -> num1.compareTo(num2));
        System.out.println("Max Number: " + (maxValue.isEmpty()? "not found" : maxValue.get()) + ", Min Number: " +  (minValue.isEmpty()? "not found" : minValue.get()));

        OptionalDouble averageNumber = numbers.stream().mapToInt(Integer::intValue).average();
        System.out.println("Average number: " + averageNumber.getAsDouble());

        long multiplyAllNumbers = numbers.stream().reduce(1, (num1, num2) -> num1 * num2);
        System.out.println("Multiply all integers in a list: " + multiplyAllNumbers);

        long countPositiveNumber = numbers.stream().filter(num -> num > 0).count();
        System.out.println("Numbers of positive in a list: " + countPositiveNumber);
    }
}
