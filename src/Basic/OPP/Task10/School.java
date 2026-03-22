package Basic.OPP.Task10;

public interface School {

    default void addStudent(){
        System.out.println("you are in the add student in school class");
    }
   default void showStudent(){
       System.out.println("you are in the show student in school class");
   }
}
