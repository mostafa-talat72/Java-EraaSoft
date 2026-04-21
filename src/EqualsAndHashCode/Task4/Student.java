package EqualsAndHashCode.Task4;

public class Student {
    private int id;
    private String name;

    // Constructors
    public Student() {}
    public Student(int id) { this.setId(id); }
    public Student(int id, String name) {
        this.setId(id);
        this.setName(name);
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // equals() compares Student objects by id
    @Override
    public boolean equals(Object obj) {
        if (!this.getClass().equals(obj.getClass()))
            return false;
        Student student = (Student) obj;
        return this.getId() == student.getId();

        // Alternative comparisons (commented out):
        // return this.getName().equals(student.getName());
        // return this.getId() == student.getId() && this.getName().equals(student.getName());
    }

    // toString() for readable output
    @Override
    public String toString() {
        return "(id: " + this.getId() + ", name: " + this.getName() + ") ";
    }
}
