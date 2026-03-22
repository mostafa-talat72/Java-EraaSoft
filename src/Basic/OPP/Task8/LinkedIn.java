package Basic.OPP.Task8;

public class LinkedIn extends PostService{
    public LinkedIn(int id, String text) {
        super(id, text);
    }


    @Override
    public boolean createPost() {
        System.out.println("You are on application linkedin");
        return true;
    }
}
