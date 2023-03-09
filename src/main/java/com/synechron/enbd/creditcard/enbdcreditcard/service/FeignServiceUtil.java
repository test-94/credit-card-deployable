package com.synechron.enbd.creditcard.enbdcreditcard.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.synechron.enbd.creditcard.enbdcreditcard.model.AccountModel;
import com.synechron.enbd.creditcard.enbdcreditcard.model.AccountsList;

@FeignClient(value = "ENBD-account-application")
public interface FeignServiceUtil {
	
	@GetMapping("/demo")
	public String getDemo();
	
	@GetMapping("/accounts/{id}")
	public AccountModel getAccountById(@PathVariable int id);
	
	@GetMapping("/accounts")
	public AccountsList getAccounts();
	
	@PostMapping("/accounts")
	public AccountModel createAccount(AccountModel accountModel);
	
	@PutMapping("/accounts/{accountId}")
	public AccountModel updateAccount(@PathVariable int accountId, AccountModel accountModel);

	@DeleteMapping("/accounts/{accountId}")
	public String deleteAccount(@PathVariable int accountId);
	
}
