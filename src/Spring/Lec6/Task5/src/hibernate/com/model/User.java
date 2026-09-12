package hibernate.com.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity(name = "Users")
public class User extends Frinds {

	private int age;
	
	@OneToOne(mappedBy = "user")
	private UserDetails userDetails;
	
	@ManyToMany(mappedBy = "users")
	private List<Frinds> frinds;
	
	@OneToMany(mappedBy = "user")
	private List<Post> posts;
	public User() {}
	
	public User( String name, int age) {
		super(name);
		this.age = age;
	}
	
	public User( String name, int age, UserDetails userDetails, List<Frinds> frinds) {
		super(name);
		this.age = age;
		this.userDetails = userDetails;
		this.frinds = frinds;
	}
	
	public User( String name, int age,  List<Post> posts, UserDetails userDetails) {
		super(name);
		this.age = age;
		this.userDetails = userDetails;
		this.posts = posts;
	}
	
	public User( String name, int age, UserDetails userDetails, List<Frinds> frinds, List<Post> posts) {
		super(name);
		this.age = age;
		this.userDetails = userDetails;
		this.frinds = frinds;
		this.posts = posts;
	}
	
	
	public User(long id, String name, int age, UserDetails userDetails, List<Frinds> frinds, List<Post> posts) {
		super(id,name);
		this.age = age;
		this.userDetails = userDetails;
		this.frinds = frinds;
		this.posts = posts;
	}


	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public List<Frinds> getFrinds() {
		return frinds;
	}

	public void setFrinds(List<Frinds> frinds) {
		this.frinds = frinds;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	@Override
	public String toString() {
		return "User [id=" + this.getId() + ", name=" + this.getName() + ", age=" + age + ", userDetails=" + userDetails + ", frinds="
				+ frinds + ", posts=" + posts + "]";
	}
	
	
}
