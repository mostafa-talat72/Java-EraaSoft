package hibernate.com.model;

import java.util.List;

import javax.persistence.*;

@Entity
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@OneToOne(mappedBy = "student")
	private Passport passport;
	
	@ManyToMany(mappedBy = "students")
	private List<Course> courses;
}
