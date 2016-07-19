package com.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;

@Entity
@Table(name="login_dtls")
public class LoginDtlsEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -819447051342746314L;

	@Id
	@Column(name="MEMBER_ID")
	private String memberId="";
	
	@Column(name="PASSWORD")
	private String password="";
	
	@Column(name="ROLE")
	private String role="";

	/**
	 * @return the memberId
	 */
	public String getMemberId() {
		return memberId;
	}

	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	
}
