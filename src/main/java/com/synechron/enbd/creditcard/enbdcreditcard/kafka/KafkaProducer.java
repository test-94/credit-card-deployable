package com.synechron.enbd.creditcard.enbdcreditcard.kafka;



import com.synechron.enbd.creditcard.enbdcreditcard.schema.SchemaAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private KafkaTemplate<String, SchemaAccount> kafkaTemplate;
//    private KafkaTemplate<String, String> kafkaStringTemplate;

    public KafkaProducer(KafkaTemplate<String, SchemaAccount> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
//        this.kafkaStringTemplate = kafkaStringTemplate;
    }

    public void sendMessage(SchemaAccount data) {

        LOGGER.info(String.format("Message sent -> %s", data.toString()));

        this.kafkaTemplate.send("javaguides_json", String.valueOf(data.getId()), data);

//        ListenableFuture<SendResult<String, Account>> future = (ListenableFuture<SendResult<String, Account>>) kafkaTemplate.send("javaguides_json", String.valueOf(data.getId()), data);
//
//        future.addCallback(new ListenableFutureCallback<SendResult<String, Account>>() {
//            @Override
//            public void onFailure(Throwable ex) {
//                LOGGER.info(String.format("Failed to send the message!!"));
//            }
//
//            @Override
//            public void onSuccess(SendResult<String, Account> result) {
//                LOGGER.info(String.format("Message has been sent successfully!"));
//            }
//        });
    }

    public void sendStringMessage(String message) {

        LOGGER.info(String.format("Message sent -> %s", message));

//        kafkaStringTemplate.send("javaguides", message);

    }


}
