package Basic.OPP.Task8;

public class Facebook extends PostService{


    public Facebook(int id, String text) {
        super(id, text);
    }


    @Override
    public boolean createPost() {
        System.out.println("You are on application facebook");
        return true;
    }
}
