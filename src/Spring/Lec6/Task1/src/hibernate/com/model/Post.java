package hibernate.com.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	private String header;
	
	private int content;
	
	@ManyToOne
	private User user;

	public Post() {}
	
	public Post(String header, int content) {
		this.header = header;
		this.content = content;
	}
	
	public Post( String header, int content, User user) {
		this.header = header;
		this.content = content;
		this.user = user;
	}
	
	
	public Post(long id, String header, int content, User user) {
		this.id = id;
		this.header = header;
		this.content = content;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public int getContent() {
		return content;
	}

	public void setContent(int content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", header=" + header + ", content=" + content + ", user=" + user + "]";
	}
	
	
}
