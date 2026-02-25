package Basic.OPP.Task5;

public class Player extends SharedData {

    private int number;
    public Player(int id, String name,String phoneNumber,int number) {
        super(id, name, phoneNumber);
        if (checkNumber(number))
            this.number = number;
    }

    public Player(int id, String name, int number) {
        super(id, name);
        if (checkNumber(number))
            this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        if (checkNumber(number))
            this.number = number;
    }

    private boolean checkNumber(int number) {
        if(number < 0 || number > 99)
            throw new IllegalArgumentException("Invalid player number, number should be between 0 and 99.");
        return true;
    }
}
