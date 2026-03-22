package Basic.OPP.Task8;

public abstract class PostService extends SocialAccount{
    private String text;
    private String imageUrl;

    public PostService(int id, String text) {
        super(id);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public abstract boolean createPost();

}
