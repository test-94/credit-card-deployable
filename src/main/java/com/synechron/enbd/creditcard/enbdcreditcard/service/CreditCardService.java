package com.synechron.enbd.creditcard.enbdcreditcard.service;

import java.io.IOException;
import java.util.*;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.synechron.enbd.creditcard.enbdcreditcard.model.AccountModel;
import com.synechron.enbd.creditcard.enbdcreditcard.model.AccountsList;
import com.synechron.enbd.creditcard.enbdcreditcard.model.CreditCard;
import com.synechron.enbd.creditcard.enbdcreditcard.repository.CreditCardRepository;


@Service
public class CreditCardService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreditCardService.class);
	private final static String CREATE_NEW_ACCOUNT_URL = "http://ENBD-account-application/accounts";
	private final static String UPDATE_ACCOUNT_URL = "http://ENBD-account-application/accounts/";
	private static final String DELETE_ACCOUNT_URL = "http://ENBD-account-application/accounts/{accountId}";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private CreditCardRepository creditCardRepository;

	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	@Autowired
	private FeignServiceUtil feignServiceUtil;

	public CreditCard createCreditCard(CreditCard creditCard) throws IOException {

		// set auto increment id
		creditCard.setId(sequenceGeneratorService.getSequenctNumber(CreditCard.SEQUENCE_NAME));

		LOGGER.info(String.format("Credit Card Service: createCreditCard request call {%s}", new ObjectMapper().writeValueAsString(creditCard)));
		return creditCardRepository.save(creditCard);
	}

	public List<CreditCard> getAllCreditCards() throws IOException {


		List<CreditCard> creditCardsList = creditCardRepository.findAll();
		LOGGER.info(String.format("Credit Card Service: getAllCreditCards request call {%s}", new ObjectMapper().writeValueAsString(creditCardsList)));
		return creditCardsList;
	}
	
	public List<CreditCard> getCreditCardsListByAccountId(int accountId) {
		LOGGER.info(String.format("Credit Card Service: getCreditCardsListByAccountId request call"));
		return creditCardRepository.findByAccountId(accountId);
	}
	
	public CreditCard getCreditCardtById(int id) throws IOException {
		Optional<CreditCard> creditCard = creditCardRepository.findById(id);
		LOGGER.info(String.format("Credit Card Service: getCreditCardtById request call {%s}", new ObjectMapper().writeValueAsString(creditCard.get())));
		LOGGER.info("{}", creditCard);
		return creditCard.get();
	}
	
	public String deleteCreditCardById(int id) {
		Optional<CreditCard> creditCard = creditCardRepository.findById(id);
		creditCardRepository.deleteById(id);
		LOGGER.info(String.format("Credit Card Service: deleteCreditCardById request call"));
		return "Credit Card ID: (" + id + ") has been deleted successfuly!";
	}

	// Services Using RestTemplate section
	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "getAccountsListFallBackMethod")
	public AccountsList getAccountsListRestTemplate() throws IOException {
		AccountsList accountsList = restTemplate.getForObject("http://ENBD-account-application/accounts", AccountsList.class);
		LOGGER.info(String.format("Credit Card Service: getAccountsListRestTemplate request call {%s}", new ObjectMapper().writeValueAsString(accountsList)));
		return accountsList;
	}

	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "getAccountByIdFallBackMethod")
	public AccountModel getAccountByIdRestTemplate(int accountId) throws IOException {
		AccountModel accountModel = restTemplate.getForObject("http://ENBD-account-application/accounts/" + accountId, AccountModel.class);
		LOGGER.info(String.format("Credit Card Service: getAccountByIdRestTemplate request call {%s}", new ObjectMapper().writeValueAsString(accountModel)));
		return accountModel;
	}

	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "createAccountFallBackMethod")
	public AccountModel createAccountRestTemplate(AccountModel accountModel) throws IOException {
		LOGGER.info(String.format("Credit Card Service: createAccountRestTemplate request call {%s}", new ObjectMapper().writeValueAsString(accountModel)));
		return (restTemplate.postForEntity(CREATE_NEW_ACCOUNT_URL, accountModel, AccountModel.class)).getBody();
	}

	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "updateAccountFallBackMethod")
	public String updateAccountRestTemplate(int accountId, AccountModel accountModel) throws IOException {
		restTemplate.put(UPDATE_ACCOUNT_URL + accountId, accountModel);
		LOGGER.info(String.format("Credit Card Service: updateAccountRestTemplate request call {%s}", new ObjectMapper().writeValueAsString(accountModel)));
		return "Account with ID: (" + accountId + ") has been updated successfully";
	}

	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "deleteAccountFallBackMethod")
	public String deleteAccountByIdRestTemplate(int accountId) {
		Map<String, Integer> params = new HashMap<>();
		params.put("accountId", accountId);
		restTemplate.delete(DELETE_ACCOUNT_URL, params);
		LOGGER.info(String.format("Credit Card Service: deleteAccountByIdRestTemplate request call"));
		return "Account with ID: " + accountId + "has been deleted successfully";
	}
	
	// End of Services Using RestTemplate section

	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "getAccountByIdFallBackMethod")
	public AccountModel getAccountByIdWebClient(int accountId) throws IOException {
//		Account account = restTemplate.getForObject("http://localhost:9091/accounts/" + accountId, Account.class);
		AccountModel accountModel = webClientBuilder.build().get().uri("http://ENBD-account-application/accounts/" + accountId).retrieve().bodyToMono(AccountModel.class).block();
		LOGGER.info(String.format("Credit Card Service: getAccountByIdWebClient request call {%s}", new ObjectMapper().writeValueAsString(accountModel)));
		return accountModel;
	}

	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "getAccountsListFallBackMethod")
	public AccountsList getAccountsWebClient() throws IOException {
		AccountsList accountList = webClientBuilder.build().get().uri("http://ENBD-account-application/accounts")
				.retrieve()
				.bodyToMono(AccountsList.class)
				.block();
		LOGGER.info(String.format("Credit Card Service: getAccountsWebClient request call {%s}", new ObjectMapper().writeValueAsString(accountList)));
		return accountList;
	}

	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "createAccountFallBackMethod")
	public AccountModel createAccountWebClient(AccountModel accountModel) throws IOException {
//		Account account = restTemplate.getForObject("http://localhost:9091/accounts/" + accountId, Account.class);
		AccountModel addedAccountModel = webClientBuilder.build()
				.post()
				.uri("http://ENBD-account-application/accounts")
				.syncBody(accountModel)
				.retrieve()
				.bodyToMono(AccountModel.class)
				.block();
		LOGGER.info(String.format("Credit Card Service: createAccountWebClient request call {%s}", new ObjectMapper().writeValueAsString(accountModel)));
		return addedAccountModel;
	}

	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "updateAccountWebClientAndFeignClientFallBackMethod")
	public AccountModel updateAccountWebClient(int accountId, AccountModel accountModel) throws IOException {
		AccountModel updatedAccountModel = webClientBuilder.build()
				.put()
				.uri("http://ENBD-account-application/accounts/{accountId}", accountId)
				.syncBody(accountModel)
				.retrieve()
				.bodyToMono(AccountModel.class)
				.block();
		LOGGER.info(String.format("Credit Card Service: updateAccountWebClient request call {%s}", new ObjectMapper().writeValueAsString(accountModel)));
		return updatedAccountModel;
	}

	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "deleteAccountFallBackMethod")
	public String webClientDeleteAccountById(int accountId) {
		LOGGER.info(String.format("Credit Card Service: webClientDeleteAccountById request call"));
		return webClientBuilder.build().delete()
		.uri("http://ENBD-account-application/accounts/{accountId}", accountId)
		.retrieve()
		.bodyToMono(String.class)
		.block();

	}

	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "getAccountByIdFallBackMethod")
	public AccountModel getAccountByIdFeignClient(int id) {
		LOGGER.info(String.format("Credit Card Service: getAccountByIdFeignClient request call"));
		return feignServiceUtil.getAccountById(id);
	}

	@Retry(name="sampleCircuitBreaker", fallbackMethod = "getAccountsListFallBackMethod")
	public AccountsList getAccountsFeignClient() throws IOException {
		LOGGER.info(String.format("getAccountsFeignClient has been called"));
		AccountsList accountList = feignServiceUtil.getAccounts();
		LOGGER.info(String.format("Credit Card Service: getAccountsFeignClient request call {%s}", new ObjectMapper().writeValueAsString(accountList)));
		return accountList;
	}

	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "createAccountFallBackMethod")
	public AccountModel createAccountFeignClient(AccountModel accountModel) throws IOException {
		LOGGER.info(String.format("Credit Card Service: createAccountFeignClient request call {%s}", new ObjectMapper().writeValueAsString(accountModel)));
		return feignServiceUtil.createAccount(accountModel);
	}

	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "updateAccountWebClientAndFeignClientFallBackMethod")
	public AccountModel UpdateAccountFeignClient(int accountId, AccountModel accountModel) throws IOException {
		LOGGER.info(String.format("Credit Card Service: UpdateAccountFeignClient request call {%s}", new ObjectMapper().writeValueAsString(accountModel)));
		return feignServiceUtil.updateAccount(accountId, accountModel);
	}

	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "deleteAccountFallBackMethod")
	public String deleteAccountFeignClient(int accountId) {
		LOGGER.info(String.format("Credit Card Service: deleteAccountFeignClient request call"));
		return feignServiceUtil.deleteAccount(accountId);
	}


	//	@CircuitBreaker(name = "sampleCircuitBreaker", fallbackMethod = "hardCodedFallBackMethod")
//	@RateLimiter(name = "sampleCircuitBreaker")
//	@Bulkhead(name="sampleCircuitBreaker")
	@CircuitBreaker(name="sampleCircuitBreaker", fallbackMethod = "hardCodedFallBackMethod")
	public String getSampleCircuitBreakerMessage() {
		LOGGER.info("Alaa: Sample Circuit Breaker Received !");
		String result = restTemplate.getForObject("http://ENBD-account-/accounts", String.class);
//		String result = "getSampleCircuitBreakerMessage";
		LOGGER.info(String.format("Credit Card Service: UpdateAccountFeignClient request call {%s}", result));
		return result;

	}

	public String hardCodedFallBackMethod(Exception ex) {
		LOGGER.info(String.format("Credit Card Service: hardCodedFallBackMethod request call {Fall back hard coded response}"));
		return "Fall back hard coded response";
	}


	public AccountsList getAccountsListFallBackMethod(Exception ex) throws IOException {
		AccountsList accountsList = new AccountsList();
		accountsList.setAccountsList(Arrays.asList(
				new AccountModel(0, "No Response", 0)
		));
		LOGGER.info(String.format("Credit Card Service: getAccountsListFallBackMethod request call {%s}", new ObjectMapper().writeValueAsString(accountsList)));
		return accountsList;
	}

	public AccountModel getAccountByIdFallBackMethod(int accountId, Exception ex ){
		LOGGER.info(String.format("Credit Card Service: getAccountByIdFallBackMethod request call"));
		return new AccountModel(0, "No Response",0);
	}

	public AccountModel createAccountFallBackMethod(AccountModel accountModel, Exception ex ){
		LOGGER.info(String.format("Credit Card Service: createAccountFallBackMethod request call"));
		return new AccountModel(0, "No Response",0);
	}

	public String updateAccountFallBackMethod(int accountId, AccountModel accountModel, Exception ex) {
		LOGGER.info(String.format("Credit Card Service: updateAccountFallBackMethod request call"));
		return "No response from update fallback method";
	}

	public AccountModel updateAccountWebClientAndFeignClientFallBackMethod(int accountId, AccountModel accountModel, Exception ex){
		LOGGER.info(String.format("Credit Card Service: updateAccountWebClientAndFeignClientFallBackMethod request call"));
		return new AccountModel(0, "No Response",0);
	}

	public String deleteAccountFallBackMethod(int accountId, Exception ex){
		LOGGER.info(String.format("Credit Card Service: deleteAccountFallBackMethod request call"));
		return "No response from delete fallback method";
	}



}
