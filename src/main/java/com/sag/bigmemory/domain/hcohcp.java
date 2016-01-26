package com.sag.bigmemory.domain;

import java.io.Serializable;

public class hcohcp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1902L;
	private String professionalEnrollmentID;
	private String firstName;
	private String lastName;
	private String suffix;
	private String primarySpeciality;
	private String organization;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zipcode;

	public String getProfessionalEnrollmentID() {
		return professionalEnrollmentID;
	}

	public void setProfessionalEnrollmentID(String professionalEnrollmentID) {
		this.professionalEnrollmentID = professionalEnrollmentID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getPrimarySpeciality() {
		return primarySpeciality;
	}

	public void setPrimarySpeciality(String primarySpeciality) {
		this.primarySpeciality = primarySpeciality;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getDisplayName() {
		return firstName + " " + lastName + " " + suffix;
	}

	public hcohcp(String professionalEnrollmentID, String firstName, String lastName, String suffix,
			String primarySpeciality, String organization, String address1, String address2, String city, String state,
			String zipcode) {
		super();
		this.professionalEnrollmentID = professionalEnrollmentID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.suffix = suffix;
		this.primarySpeciality = primarySpeciality;
		this.organization = organization;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		if (zipcode.length() > 5)
			this.zipcode = zipcode.substring(0, 4);
		else
			this.zipcode = zipcode;
	}

}
