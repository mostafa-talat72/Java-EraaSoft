package hibernate.com.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

@Entity
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@ManyToMany
	@JoinTable(
				name = "patient_doctor",
				joinColumns = @JoinColumn(name = "patient_id"),
				inverseJoinColumns = @JoinColumn(name = "doctor_id"),
				uniqueConstraints = @UniqueConstraint(
							columnNames = {"patient_doctor", "doctor_id"}
						)
			)
	private List<Doctor> doctors;
}
