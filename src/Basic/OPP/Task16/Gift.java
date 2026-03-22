package Basic.OPP.Task16;

import Basic.OPP.Task13.BaseEntity;

import java.util.ArrayList;

public class Gift extends BaseEntity {

    private ArrayList<Food> foods;
    private ArrayList<Person> persons;

    public Gift(int id, ArrayList<Food> foods,  ArrayList<Person> persons) {
        super(id);
        this.setFoods(foods);
        this.setPersons(persons);
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }
}
