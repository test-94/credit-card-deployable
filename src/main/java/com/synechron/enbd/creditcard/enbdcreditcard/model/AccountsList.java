package com.synechron.enbd.creditcard.enbdcreditcard.model;

import java.util.List;

public class AccountsList {

	private List<AccountModel> accountsList;

	public AccountsList() {
	}

	public AccountsList(List<AccountModel> accountsList) {
		super();
		this.accountsList = accountsList;
	}

	public List<AccountModel> getAccountsList() {
		return accountsList;
	}

	public void setAccountsList(List<AccountModel> accountsList) {
		this.accountsList = accountsList;
	}
	
	
	
}
