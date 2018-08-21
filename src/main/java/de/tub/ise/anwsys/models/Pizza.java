package de.tub.ise.anwsys.models;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="pizza")
public class Pizza implements Serializable {

	private static final long serialVersionUID = -6640481949420444264L;
	
	@Id
	int id;
	String name;
	String size;
	float price;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public enum size
	{
		STANDARD,
		LARGE;
	}
	

	public Pizza() {
		//empty constructor required by JPA
	}

	public Pizza(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}