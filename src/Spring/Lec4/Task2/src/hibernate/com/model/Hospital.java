package hibernate.com.model;

import java.util.List;

import javax.persistence.*;

@Entity
public class Hospital {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	private String name;
	
	private int numberOfDoctors;
	
	private long numberOfPatient;
	
	@OneToMany(mappedBy = "hospital")
	private List<Doctor> doctors;
	
	@ManyToMany
	@JoinTable(
				name = "hospital_patient",
				joinColumns = @JoinColumn(name = "hospital_id"),
				inverseJoinColumns = @JoinColumn(name = "patient_id")
			)
	private List<Patient> patients;
	
}
