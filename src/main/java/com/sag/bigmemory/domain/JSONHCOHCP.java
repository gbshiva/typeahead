package com.sag.bigmemory.domain;

import java.io.Serializable;

public class JSONHCOHCP implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1402L;
	private String professionalEnrollmentID;
	private String displayName;
	private String primarySpeciality;
	private String organization;
	private String address1;
	private String city;
	private String state;
	private String zipcode;
	public JSONHCOHCP(String professionalEnrollmentID, String displayName, String primarySpeciality,
			String organization, String address1, String city, String state, String zipcode) {
		super();
		this.professionalEnrollmentID = professionalEnrollmentID;
		this.displayName = displayName;
		this.primarySpeciality = primarySpeciality;
		this.organization = organization;
		this.address1 = address1;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
	}
	public String getProfessionalEnrollmentID() {
		return professionalEnrollmentID;
	}
	public void setProfessionalEnrollmentID(String professionalEnrollmentID) {
		this.professionalEnrollmentID = professionalEnrollmentID;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
	
	
	
	
	
	
}
