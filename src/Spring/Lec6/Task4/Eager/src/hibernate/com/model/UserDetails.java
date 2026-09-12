package hibernate.com.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	private String address;
	
	private String phone;
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
	@JoinColumn(unique = true, nullable = false)
	private User user;

	public UserDetails() {}

	public UserDetails(String address, String phone) {
		this.address = address;
		this.phone = phone;
	}
	
	public UserDetails(String address, String phone, User user) {
		this.address = address;
		this.phone = phone;
		this.user = user;
	}
	
	public UserDetails(long id, String address, String phone, User user) {
		this.id = id;
		this.address = address;
		this.phone = phone;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "UserDetails [id=" + id + ", address=" + address + ", phone=" + phone + "]";
	}

}
