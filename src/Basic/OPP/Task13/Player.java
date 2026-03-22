package Basic.OPP.Task13;

public class Player extends BaseEntity {
    private String playerName;
    private int playedAge;
    private Shirt shirt;

    public Player(int id) {
        super(id);
    }


    public Player(int id, String playerName, int playedAge, Shirt shirt) {
        this(id);
        this.setPlayerName(playerName);
        this.setPlayedAge(playedAge);
        this.setShirt(shirt);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayedAge() {
        return playedAge;
    }

    public void setPlayedAge(int playedAge) {
        this.playedAge = playedAge;
    }

    public Shirt getShirt() {
        return shirt;
    }

    public void setShirt(Shirt shirt) {
        this.shirt = shirt;
    }
}
