package Basic.OPP.Task16;

import Basic.OPP.Task13.BaseEntity;

import java.util.ArrayList;

public class Bill extends BaseEntity {

    private ArrayList<Order> orders;
    private Person person;

    public Bill(int id, ArrayList<Order> orders, Person person) {
        super(id);
        this.setOrders(orders);
        this.setPerson(person);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
