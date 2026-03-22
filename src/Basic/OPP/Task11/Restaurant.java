package Basic.OPP.Task11;

public abstract class Restaurant {
    private int itemId;
    private String itemName;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void addOrder(){
        System.out.println("Order Added Successfully from Restaurant class");
    }

    public abstract void showOrder();
}
