package Basic.OPP.Task17;

public class BaseEntity {
    private int id;
    private String name;

    public BaseEntity(int id) {
        this.setId(id);
    }

    public BaseEntity(int id, String name) {
        this(id);
        this.setName(name);
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
}
