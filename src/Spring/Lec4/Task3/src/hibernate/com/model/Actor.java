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
public class Actor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@ManyToMany
	@JoinTable(
				name = "actor_movie",
				joinColumns = @JoinColumn(name = "actor_id"),
				inverseJoinColumns = @JoinColumn(name = "movie_id"),
				uniqueConstraints = @UniqueConstraint(
							columnNames = { "actor_id", "movie_id"}
						)
			)
	private List<Movie> movies;
}
