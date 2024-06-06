package ru.isands.test.estore.dto;


import java.io.Serializable;

public class ShopDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	long id;
	String name;
	String address;
	
	public ShopDTO() {
		
	}
	
	public ShopDTO(long id, String name, String address) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}
	
}