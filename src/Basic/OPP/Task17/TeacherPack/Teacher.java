package Basic.OPP.Task17.TeacherPack;

import Basic.OPP.Task17.BaseEntity;

public class Teacher extends BaseEntity {

    private String active;

    public Teacher(int id) {
        super(id);
    }

    public Teacher(int id, String name) {
        super(id,name);
    }

    public Teacher(int id, String name, String active) {
        this(id, name);
        this.setActive(active);
    }

    protected String getActive() {
        return active;
    }

    protected void setActive(String active) {
        this.active = active;
    }
}
