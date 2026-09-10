package hibernate.com.model;

import java.util.List;

import javax.persistence.*;

@Entity
public class Doctor {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@Column(nullable = false)
	private String userName;
	
	private double salary;
	
	@OneToOne(mappedBy = "doctor")
	private DoctorDetails doctorDetails;
	
	@ManyToOne
	private Hospital hospital;
	
	@OneToMany(mappedBy = "doctor")
	private List<Patient> patients;
}
