package com.foa.driver.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Driver {
	@SerializedName("id")
	private String id;
	@SerializedName("phoneNumber")
	private String phoneNumber;
	@SerializedName("email")
	private String email;
	@SerializedName("name")
	private String name;
	@SerializedName("city")
	private String city;
	@SerializedName("dateOfBirth")
	private Date dateOfBirth;
	@SerializedName("IDNumber")
	private String IDNumber;
	@SerializedName("licensePlate")
	private String licensePlate;
	@SerializedName("avatar")
	private String avatar;
	@SerializedName("identityCardImageUrl")
	private String identityCardImageUrl;
	@SerializedName("driverLicenseImageUrl")
	private String driverLicenseImageUrl;
	@SerializedName("vehicleRegistrationCertificateImageUrl")
	private String vehicleRegistrationCertificateImageUrl;
	@SerializedName("isVerified")
	private boolean isVerified;
	@SerializedName("isBanned")
	private boolean isBanned;
	@SerializedName("beforeUpdatePassword")
	private String beforeUpdatePassword;

	public Driver(String id, String phoneNumber, String email, String name, String city, Date dateOfBirth, String IDNumber, String licensePlate, String avatar, String identityCardImageUrl, String driverLicenseImageUrl, String vehicleRegistrationCertificateImageUrl, boolean isVerified, boolean isBanned, String beforeUpdatePassword) {
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.name = name;
		this.city = city;
		this.dateOfBirth = dateOfBirth;
		this.IDNumber = IDNumber;
		this.licensePlate = licensePlate;
		this.avatar = avatar;
		this.identityCardImageUrl = identityCardImageUrl;
		this.driverLicenseImageUrl = driverLicenseImageUrl;
		this.vehicleRegistrationCertificateImageUrl = vehicleRegistrationCertificateImageUrl;
		this.isVerified = isVerified;
		this.isBanned = isBanned;
		this.beforeUpdatePassword = beforeUpdatePassword;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getIDNumber() {
		return IDNumber;
	}

	public void setIDNumber(String IDNumber) {
		this.IDNumber = IDNumber;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getIdentityCardImageUrl() {
		return identityCardImageUrl;
	}

	public void setIdentityCardImageUrl(String identityCardImageUrl) {
		this.identityCardImageUrl = identityCardImageUrl;
	}

	public String getDriverLicenseImageUrl() {
		return driverLicenseImageUrl;
	}

	public void setDriverLicenseImageUrl(String driverLicenseImageUrl) {
		this.driverLicenseImageUrl = driverLicenseImageUrl;
	}

	public String getVehicleRegistrationCertificateImageUrl() {
		return vehicleRegistrationCertificateImageUrl;
	}

	public void setVehicleRegistrationCertificateImageUrl(String vehicleRegistrationCertificateImageUrl) {
		this.vehicleRegistrationCertificateImageUrl = vehicleRegistrationCertificateImageUrl;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean verified) {
		isVerified = verified;
	}

	public boolean isBanned() {
		return isBanned;
	}

	public void setBanned(boolean banned) {
		isBanned = banned;
	}

	public String getBeforeUpdatePassword() {
		return beforeUpdatePassword;
	}

	public void setBeforeUpdatePassword(String beforeUpdatePassword) {
		this.beforeUpdatePassword = beforeUpdatePassword;
	}
}
