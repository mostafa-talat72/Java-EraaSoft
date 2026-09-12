package hibernate.com.model;

import javax.persistence.*;

@MappedSuperclass
public class InhertanceData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String name;

    public InhertanceData() {}

    public InhertanceData(String name) {
        this.name = name;
    }

    public InhertanceData(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}