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
public class Car {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@ManyToMany
	@JoinTable(
				name = "car_driver",
				joinColumns = @JoinColumn(name = "car_id"),
				inverseJoinColumns = @JoinColumn(name = "driver_id"),
				uniqueConstraints = @UniqueConstraint(
							columnNames = {"car_id", "driver_id"}
						)
			)
	private List<Driver> drivers;
}
