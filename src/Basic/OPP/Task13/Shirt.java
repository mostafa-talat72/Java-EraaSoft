package Basic.OPP.Task13;

public class Shirt extends BaseEntity{
    private int shirtSize;
    private int shirtNumber;
    private Player player;


    public Shirt(int id) {
        super(id);
    }

    public Shirt(int id,Player player,int shirtSize, int shirtNumber) {
        this(id);
        this.setShirtSize(shirtSize);
        this.setShirtNumber(shirtNumber);
    }

    public int getShirtSize() {
        return shirtSize;
    }

    public void setShirtSize(int shirtSize) {
        this.shirtSize = shirtSize;
    }

    public int getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
