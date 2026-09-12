package hibernate.com.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

@Entity
public class Frinds extends InhertanceData{
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "frinds_users",
			joinColumns = @JoinColumn(name = "frind_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"),
			uniqueConstraints = @UniqueConstraint(
					columnNames = {"frind_id", "user_id"}
				)
		)
	private List<User> users;

	public Frinds() {};
	public Frinds(String name) {
		super(name);
	} 
	public Frinds(long id, String name) {
		super(id,name);
	} 
	
	public Frinds(String name, List<User> users) {
		this(name);
		this.users = users;
	} 
	
	public Frinds(long id, String name, List<User> users) {
		this(id,name);
		this.users = users;
	}
	
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	@Override
	public String toString() {
		return "Frinds [id=" + this.getId() + ", name=" + this.getName() + ", users=" + users + "]";
	} 
	
	
}
