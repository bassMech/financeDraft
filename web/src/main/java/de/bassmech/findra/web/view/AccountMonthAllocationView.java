package de.bassmech.findra.web.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

import de.bassmech.findra.core.repository.AccountRepository;
import de.bassmech.findra.core.repository.AccountingMonthRepository;
import de.bassmech.findra.core.repository.ConfigurationRepository;
import de.bassmech.findra.model.entity.Account;
import de.bassmech.findra.model.entity.AccountingMonth;
import de.bassmech.findra.model.entity.Configuration;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class AccountMonthAllocationView implements Serializable {

	private Logger logger = LoggerFactory.getLogger(AccountMonthAllocationView.class);

	@Autowired
	private ConfigurationRepository configurationRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountingMonthRepository accountingMonthRepository;
	
	private List<Account> accounts = new ArrayList();
	
	private List<AccountingMonth> products = new ArrayList();

	public void call() {
		List<Configuration> configs = configurationRepository.findAll();
		System.out.println(configs.size());
	}
	
    @PostConstruct
    public void init() {
    	accounts = accountRepository.findAll();
    	
//    	AccountingMonth month = accountingMonthRepository.findAll().get(0);
    	products = accountingMonthRepository.findAll();
    }
}
