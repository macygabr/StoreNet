package ru.isands.test.estore.dto;


import java.io.Serializable;

public class EmployeeDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	long id;
	String lastName;
	String firstName;
	String patronymic;
	String birthDate;
	boolean gender;
	long shopId;
	long positionId;
	
	public EmployeeDTO() {
		
	}
	
	public EmployeeDTO(long id, String lastName, String firstName, String patronymic, String birthDate, boolean gender,
			long shopId, long positionId) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.patronymic = patronymic;
		this.birthDate = birthDate;
		this.gender = gender;
		this.shopId = shopId;
		this.positionId = positionId;
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


	public boolean isGender() {
		return gender;
	}


	public void setGender(boolean gender) {
		this.gender = gender;
	}


	public long getShopId() {
		return shopId;
	}


	public void setShopId(long shopId) {
		this.shopId = shopId;
	}


	public long getPositionId() {
		return positionId;
	}


	public void setPositionId(long positionId) {
		this.positionId = positionId;
	}
	
}