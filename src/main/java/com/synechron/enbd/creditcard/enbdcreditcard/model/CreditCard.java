package com.synechron.enbd.creditcard.enbdcreditcard.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "credit_card")
public class CreditCard {

	@Transient
	public final static String SEQUENCE_NAME = "credit_card_sequence";
	
	@Id
	private int id;
	private int accountId;
	private String accountName;
	private String type;
	private double limitCredit;
	
	public CreditCard() {
		super();
	}

	public CreditCard(int id, int accountId, String accountName, String type, double limitCredit) {
		super();
		this.id = id;
		this.accountId = accountId;
		this.accountName = accountName;
		this.type = type;
		this.limitCredit = limitCredit;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getLimitCredit() {
		return limitCredit;
	}

	public void setLimitCredit(double limitCredit) {
		this.limitCredit = limitCredit;
	}
	
	
	
}
