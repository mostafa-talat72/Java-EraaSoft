package Basic.OPP.Task16;

import Basic.OPP.Task13.BaseEntity;

public class Food extends BaseEntity {

    private String name;
    private String type;

    public Food(int id) {
        super(id);
    }

    public Food(int id, String name) {
        this(id);
        this.setName(name);
    }


    public Food(int id, String name, String type) {
        this(id,name);
        this.setType(type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
