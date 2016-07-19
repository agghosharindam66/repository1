package com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "member_dtls")
public class MemberDtlsEntity {
	@Id
	@Column(name = "MEMBER_ID")
	private String memberId = "";
	@Column(name = "MEMBER_NAME")
	private String memberName = "";
	@Column(name = "MEMBER_ADDRESS")
	private String memberAddress = "";
	@Column(name = "CONTACT_NO")
	private String contactNo = "";
	@Column(name = "EMAIL_ADDRESS")
	private String emailId = "";

	/**
	 * @return the memberId
	 */
	public String getMemberId() {
		return memberId;
	}

	/**
	 * @param memberId
	 *            the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	/**
	 * @return the memberName
	 */
	public String getMemberName() {
		return memberName;
	}

	/**
	 * @param memberName
	 *            the memberName to set
	 */
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	/**
	 * @return the memberAddress
	 */
	public String getMemberAddress() {
		return memberAddress;
	}

	/**
	 * @param memberAddress
	 *            the memberAddress to set
	 */
	public void setMemberAddress(String memberAddress) {
		this.memberAddress = memberAddress;
	}

	/**
	 * @return the contactNo
	 */
	public String getContactNo() {
		return contactNo;
	}

	/**
	 * @param contactNo
	 *            the contactNo to set
	 */
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

}
