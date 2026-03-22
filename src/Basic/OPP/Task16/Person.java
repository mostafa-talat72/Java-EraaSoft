package Basic.OPP.Task16;

import Basic.OPP.Task13.BaseEntity;

public class Person extends BaseEntity {

    private String name;


    public Person(int id) {
        super(id);
    }


    public Person(int id, String name) {
        this(id);
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
