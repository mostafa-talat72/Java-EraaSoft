package hibernate.com.model;

import javax.persistence.*;

@Entity
public class DoctorDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	private String fullAddress;
	
	private String firstName;
	
	private String lastName;
	
	private int age;

	@OneToOne
	@JoinColumn(unique = true, nullable = false)
	private Doctor doctor;

}
