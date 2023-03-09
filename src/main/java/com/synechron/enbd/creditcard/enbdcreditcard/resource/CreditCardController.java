package com.synechron.enbd.creditcard.enbdcreditcard.resource;

import com.synechron.enbd.creditcard.enbdcreditcard.kafka.KafkaProducer;
import com.synechron.enbd.creditcard.enbdcreditcard.schema.SchemaAccount;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.synechron.enbd.creditcard.enbdcreditcard.model.AccountModel;
import com.synechron.enbd.creditcard.enbdcreditcard.model.CreditCard;
import com.synechron.enbd.creditcard.enbdcreditcard.service.CreditCardService;
import com.synechron.enbd.creditcard.enbdcreditcard.service.FeignServiceUtil;

import java.io.IOException;

@RestController
@RefreshScope
public class CreditCardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreditCardController.class);
    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private FeignServiceUtil feignServiceUtil;

    @PostMapping("/creditCards")
    public ResponseEntity<Object> createCreditCard(@RequestBody CreditCard creditCard) throws IOException {
        return new ResponseEntity<>(creditCardService.createCreditCard(creditCard), HttpStatus.OK);
    }

    @GetMapping("/creditCards")
    public ResponseEntity<Object> getAllCreditCards() throws IOException {
        return new ResponseEntity<>(creditCardService.getAllCreditCards(), HttpStatus.OK);
    }

    @GetMapping("/creditCards/accounts/{accountId}")
    public ResponseEntity<Object> getCreditCardsListByAccountId(@PathVariable int accountId) {
        return new ResponseEntity<>(creditCardService.getCreditCardsListByAccountId(accountId), HttpStatus.OK);
    }

    @GetMapping("/creditCards/{id}")
    public ResponseEntity<Object> getCreditCardById(@PathVariable int id) throws IOException {
        return new ResponseEntity<>(creditCardService.getCreditCardtById(id), HttpStatus.OK);
    }

    @DeleteMapping("creditCards/{id}")
    public ResponseEntity<Object> deleteCreditCardById(@PathVariable int id) {
        return new ResponseEntity<>(creditCardService.deleteCreditCardById(id), HttpStatus.OK);
    }


    /////////////////////////////////////////////////////////////////////////

    /*
     * RestTemplate operations
     * */

    // Get All Accounts using rest template
    @GetMapping("/restTemplateAccounts")
    public ResponseEntity<Object> restTemplateGetAllAccounts() throws IOException {
        return new ResponseEntity<>(creditCardService.getAccountsListRestTemplate(), HttpStatus.OK);
    }

    // Get Account By its ID using rest template
    @GetMapping("/restTemplateAccounts/{accountId}")
    public ResponseEntity<Object> restTemplategetAccountById(@PathVariable int accountId) throws IOException {
        return new ResponseEntity<>(creditCardService.getAccountByIdRestTemplate(accountId), HttpStatus.OK);
    }

    // Add new Account using rest template
    @PostMapping("/restTemplateAccounts")
    public ResponseEntity<Object> restTemplateCreateAccount(@RequestBody AccountModel accountModel) throws IOException {
        return new ResponseEntity<>(creditCardService.createAccountRestTemplate(accountModel), HttpStatus.OK);
    }

    // Update Account using rest template
    @PutMapping("/restTemplateAccounts/{id}")
    public ResponseEntity<Object> restTemplateUpdateAccount(@PathVariable int id, @RequestBody AccountModel accountModel) throws IOException {
        return ResponseEntity.ok(creditCardService.updateAccountRestTemplate(id, accountModel));
//		return ResponseEntity.ok("Credit with ID: " + id + "has been updated successfully");
    }

    // Delete Account using rest template
    @DeleteMapping("/restTemplateAccounts/{accountId}")
    public ResponseEntity<Object> restTemplateDeleteAccountById(@PathVariable int accountId) {
        return ResponseEntity.ok(creditCardService.deleteAccountByIdRestTemplate(accountId));
    }

    /*
     * End of RestTemplate operations
     * */

    //////////////////////////////////////////////////////////////////////

    /*
     * WebClient operations
     * */

    // Get All Accounts using WebClient
    @GetMapping("/webClientAccounts/{accountId}")
    public ResponseEntity<Object> webClientGetAccountById(@PathVariable int accountId) throws IOException {
        return new ResponseEntity<>(creditCardService.getAccountByIdWebClient(accountId), HttpStatus.OK);
    }

    // Get Account By its ID using WebClient
    @GetMapping("/webClientAccounts")
    public ResponseEntity<Object> webClientGetAccounts() throws IOException {
        return new ResponseEntity<>(creditCardService.getAccountsWebClient(), HttpStatus.OK);
    }

    // Add new Account using WebClient
    @PostMapping("/webClientAccounts")
    public ResponseEntity<Object> webClientCreateAccount(@RequestBody AccountModel accountModel) throws IOException {
        return new ResponseEntity<>(creditCardService.createAccountWebClient(accountModel), HttpStatus.OK);
    }

    // Update Account using WebClient
    @PutMapping("/webClientAccounts/{accountId}")
    public ResponseEntity<Object> webClientUpdateAccount(@PathVariable int accountId, @RequestBody AccountModel accountModel) throws IOException {
        return new ResponseEntity<>(creditCardService.updateAccountWebClient(accountId, accountModel), HttpStatus.OK);
    }

    // Delete Account using WebClient
    @DeleteMapping("/webClientAccounts/{accountId}")
    public ResponseEntity<Object> webClientDeleteAccountById(@PathVariable int accountId) {
        return new ResponseEntity<>(creditCardService.webClientDeleteAccountById(accountId), HttpStatus.OK);
    }

    /*
     * End of WebClient operations
     * */


    /////////////////////////////////////////////////////////////////////////////////

    /*
     * FeignTemplate operations
     * */

    //	Get Account by ID using FeignClient
    @GetMapping("/feignClientAccounts/{id}")
    public ResponseEntity<Object> getAccountByIdFeignClient(@PathVariable int id) {
        return new ResponseEntity<>(creditCardService.getAccountByIdFeignClient(id), HttpStatus.OK);
//		return new ResponseEntity<>(feignServiceUtil.getAccountById(id),HttpStatus.OK);
    }

    //	Get All Accounts using FeignClient
    @GetMapping("/feignClientAccounts")
    public ResponseEntity<Object> feignClientGetAccounts() throws IOException {
        return new ResponseEntity<>(creditCardService.getAccountsFeignClient(), HttpStatus.OK);

    }

    //	Add new Account using FeignClient
    @PostMapping("/feignClientAccounts")
    public ResponseEntity<Object> feignClientCreateAccount(@RequestBody AccountModel accountModel) throws IOException {
        return new ResponseEntity<>(creditCardService.createAccountFeignClient(accountModel), HttpStatus.OK);
//		return new ResponseEntity<>(feignServiceUtil.createAccount(accountModel), HttpStatus.OK);
    }

    //	Update Account using FeignClient
    @PutMapping("/feignClientAccounts/{accountId}")
    public ResponseEntity<Object> feignClientUpdateAccount(@PathVariable int accountId, @RequestBody AccountModel accountModel) throws IOException {
        return ResponseEntity.ok(creditCardService.UpdateAccountFeignClient(accountId, accountModel));
//		return new ResponseEntity<>(feignServiceUtil.updateAccount(accountId, accountModel), HttpStatus.OK);
    }

    //	Delete Account using FeignClient
    @DeleteMapping("/feignClientAccounts/{accountId}")
    public ResponseEntity<Object> feignClientDeleteAccount(@PathVariable int accountId) {
        return new ResponseEntity<>(creditCardService.deleteAccountFeignClient(accountId), HttpStatus.OK);
//		return new ResponseEntity<>(feignServiceUtil.deleteAccount(accountId), HttpStatus.OK);
    }

    /*
     * End of FeignClient operations
     * */

    ////////////////////////////////////////////////////////////////////////////////


    @PostMapping("/publish")
    public ResponseEntity<String> publishMessageToKafka(@RequestBody AccountModel accountModel) {

        SchemaAccount schemaAccount = SchemaAccount.newBuilder().build();
        schemaAccount.setId(accountModel.getId());
        schemaAccount.setName(accountModel.getName());
        schemaAccount.setTotalBalance(accountModel.getTotalBalance());

        kafkaProducer.sendMessage(schemaAccount);
        LOGGER.info(String.format("Message sent to kafka -> %s", accountModel.toString()));
        return ResponseEntity.ok("Message has been sent to kafka topic!");
    }

    @GetMapping("/publish")
    public ResponseEntity<String> publishStringMessageToKafka(@RequestParam("message") String message) {

        kafkaProducer.sendStringMessage(message);
        LOGGER.info(String.format("Message sent to kafka -> %s", message));
        return ResponseEntity.ok("Message has been sent to kafka topic!");
    }

    @GetMapping("/sampleCircuitBreaker")
    public ResponseEntity<String> sampleCircuitBreaker() {

        String result = creditCardService.getSampleCircuitBreakerMessage();
//		String result = new RestTemplate().getForObject("http://ENBD-account-/accounts", String.class);;

        return ResponseEntity.ok(result);
    }

    public String hardCodedFallBackMethod(Exception ex) {
        return "Fall back hard coded response";
    }

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    @Value("${my.app.name}")
    private String title;
    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        return new ResponseEntity<String>("Value of title from Config Server: "+ title + " --------- " + topicName, HttpStatus.OK);
    }

}
