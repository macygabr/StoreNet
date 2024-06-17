package ru.isands.test.estore.dto;

import java.io.Serializable;

public class EmpItemDTO implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	long id;
	String lastName;
	String firstName;
	String patronymic;
	String birthDate;
	String shop;
	String position;
	
	
	public EmpItemDTO(long id, String lastName, String firstName, String patronymic, String birthDate, String shop,
			String position) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.patronymic = patronymic;
		this.birthDate = birthDate;
		this.shop = shop;
		this.position = position;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getPatronymic() {
		return patronymic;
	}


	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}


	public String getBirthDate() {
		return birthDate;
	}


	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}


	public String getShop() {
		return shop;
	}


	public void setShop(String shop) {
		this.shop = shop;
	}


	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}

}
