package hibernate.com.model;

import java.util.Arrays;
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
public class Model2 {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int Id;
	
	private String model2Name;

	@ManyToMany(mappedBy = "model2s",cascade = CascadeType.PERSIST)
	private List<Model1> model1s;

	public Model2() {}
	
	public Model2(int id, String model2Name, List<Model1> model1s) {
		Id = id;
		this.model2Name = model2Name;
		this.model1s = model1s;
	}
	
	public Model2( String model2Name) {
		this.model2Name = model2Name;
	}
	
	public Model2( String model2Name, List<Model1> model1s) {
		this.model2Name = model2Name;
		this.model1s = model1s;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getModel2Name() {
		return model2Name;
	}

	public void setModel2Name(String model2Name) {
		this.model2Name = model2Name;
	}

	public List<Model1> getModel1s() {
		return model1s;
	}

	public void setModel1s(List<Model1> model1s) {
		this.model1s = model1s;
	}

	@Override
	public String toString() {
		return "Model2 [Id=" + Id + ", model2Name=" + model2Name + ", model1s=" + model1s + "]";
	}
	
	
}
