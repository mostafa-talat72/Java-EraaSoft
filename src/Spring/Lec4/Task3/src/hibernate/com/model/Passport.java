package hibernate.com.model;

import javax.persistence.*;

@Entity
public class Passport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@OneToOne
	@JoinColumn(unique = true, nullable = false)
	private Student student;
	
}
