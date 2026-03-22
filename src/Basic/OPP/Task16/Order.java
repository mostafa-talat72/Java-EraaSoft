package Basic.OPP.Task16;

import Basic.OPP.Task13.BaseEntity;

import java.util.ArrayList;

public class Order extends BaseEntity {

    private ArrayList<Food> foods;
    private Bill bill;

    public Order(int id, ArrayList<Food> foods, Bill bill) {
        super(id);
        this.setFoods(foods);
        this.setBill(bill);
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
