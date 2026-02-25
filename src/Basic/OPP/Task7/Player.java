package Basic.OPP.Task7;

public class Player {

    private int id;
    private String name;
    private int number;

    public Player(int id, String name, int number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void displayInfo() {
        System.out.println("Player ID: " + id);
        System.out.println("Player Name: " + name);
        System.out.println("Player Number: " + number);
    }
}
