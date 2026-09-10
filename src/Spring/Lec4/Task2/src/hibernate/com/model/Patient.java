package hibernate.com.model;

import java.util.List;

import javax.persistence.*;

@Entity
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	private String name;
	
	private String typeOfDisease;
	
	@ManyToOne
	private Doctor doctor;
	
	@ManyToMany(mappedBy = "patients")
	private List<Hospital> hospitals;
}
