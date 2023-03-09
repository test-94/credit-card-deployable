package com.synechron.enbd.creditcard.enbdcreditcard.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.synechron.enbd.creditcard.enbdcreditcard.model.CreditCard;

@Repository
public interface CreditCardRepository extends MongoRepository<CreditCard, Integer>{

	public List<CreditCard> findByAccountId(int accountId);
	
}
