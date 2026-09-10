package hibernate.com.model;

import javax.persistence.*;

@Entity
public class Passport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@OneToOne
	private Student student;
	
}
