package com.synechron.enbd.creditcard.enbdcreditcard.model;

public class AccountModel {

	
	private int id;
	private String name;
	private double totalBalance;
	
	public AccountModel() {
		super();
	}

	public AccountModel(int id, String name, double totalBalance) {
		super();
		this.id = id;
		this.name = name;
		this.totalBalance = totalBalance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}

	@Override
	public String toString() {
		return "Account{" +
				"id=" + id +
				", name='" + name + '\'' +
				", totalBalance=" + totalBalance +
				'}';
	}
}
