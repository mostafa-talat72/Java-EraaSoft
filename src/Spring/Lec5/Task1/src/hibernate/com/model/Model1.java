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
public class Model1 {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int Id;
	
	private String model1Name;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "model1_model2",
			joinColumns = @JoinColumn(name = "model1_id"),
			inverseJoinColumns = @JoinColumn(name = "model2_id"),
			uniqueConstraints = @UniqueConstraint(
						columnNames = {"model1_id", "model2_id"}
					)
		)
	private List<Model2> model2s;
	
	public Model1() {}
	
	public Model1(int id, String model1Name, List<Model2> model2s) {
		Id = id;
		this.model1Name = model1Name;
		this.model2s = model2s;
	}
	
	public Model1( String model1Name) {
		this.model1Name = model1Name;
	}
	
	public Model1( String model1Name, List<Model2> model2s) {
		this.model1Name = model1Name;
		this.model2s = model2s;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getModel1Name() {
		return model1Name;
	}

	public void setModel1Name(String model1Name) {
		this.model1Name = model1Name;
	}

	public List<Model2> getModel2s() {
		return model2s;
	}

	public void setModel2s(List<Model2> model2s) {
		this.model2s = model2s;
	}

	@Override
	public String toString() {
		return "Model1 [Id=" + Id + ", model1Name=" + model1Name + ", model2s=" + model2s + "]";
	}
	
}
