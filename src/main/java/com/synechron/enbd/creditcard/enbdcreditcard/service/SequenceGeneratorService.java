package com.synechron.enbd.creditcard.enbdcreditcard.service;



import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.synechron.enbd.creditcard.enbdcreditcard.model.DbSequence;



@Service
public class SequenceGeneratorService {

	@Autowired
	private MongoOperations mongoOperations;
	
	public int getSequenctNumber(String sequenceName) {
		
		// get sequence number
		Query query = new Query(Criteria.where("id").is(sequenceName));
		
		//update the sequence number
		Update update = new Update().inc("seq", 1);
		
		//modify the sequence in document
		DbSequence counter = mongoOperations
                .findAndModify(query,
                        update, options().returnNew(true).upsert(true),
                        DbSequence.class);		
		return !Objects.isNull(counter) ? counter.getSeq() : 1;
		
	}
	
}
