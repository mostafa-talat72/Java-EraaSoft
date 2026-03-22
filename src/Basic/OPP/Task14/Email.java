package Basic.OPP.Task14;
import Basic.OPP.Task13.BaseEntity;

public class Email extends BaseEntity {

    private String email;
    private Student student;

    public Email(int id) {
        super(id);
    }

    public Email(int id, Student student, String email) {
        this(id);
        this.setEmail(email);
        this.setStudent(student);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
