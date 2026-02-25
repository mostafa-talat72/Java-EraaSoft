package Basic.OPP.Task4;

public class Person {
    private int id;
    private String name;

    public Person(int id, String name) {
        if (checkId(id) && checkName(name)) {
            this.id = id;
            this.name = name;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (checkId(id)) {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (checkName(name)) {
             this.name = name;
        }
    }

    private boolean checkName(String name) {
        if(name.length() <= 2)
            throw new IllegalArgumentException("Invalid name, name should be more than 2 characters.");
        return true;
    }

    private boolean checkId(int id) {
        if(id <= 0)
            throw new IllegalArgumentException("Invalid id, id should be positive.");
        return true;
    }
}
