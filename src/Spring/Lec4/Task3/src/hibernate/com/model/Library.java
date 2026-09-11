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
public class Library {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@ManyToMany
	@JoinTable(
				name = "library_book",
				joinColumns = @JoinColumn(name = "library_id"),
				inverseJoinColumns = @JoinColumn(name = "book_id"),
				uniqueConstraints = @UniqueConstraint(
							columnNames = {"library_id", "book_id"}
						)
			)
	private List<Book> books;
}
