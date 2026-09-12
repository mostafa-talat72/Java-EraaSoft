package hibernate.com.model;

import javax.persistence.CascadeType;
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
	
	private String content;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private User user;

	public Post() {}
	
	public Post(String header, String content) {
		this.header = header;
		this.content = content;
	}
	
	public Post( String header, String content, User user) {
		this.header = header;
		this.content = content;
		this.user = user;
	}
	
	
	public Post(long id, String header, String content, User user) {
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
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
