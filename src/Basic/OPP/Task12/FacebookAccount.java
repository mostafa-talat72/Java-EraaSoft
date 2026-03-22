package Basic.OPP.Task12;

public class FacebookAccount {
    private int id;
    private String text;
    private String image;

    public FacebookAccount(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public FacebookAccount(int id, String text, String image) {
        this.id = id;
        this.text = text;
        this.image = image;
    }


    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }
}
