package de.bassmech.findra.web.view;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.core.repository.ClientRepository;
import de.bassmech.findra.model.entity.Client;
import de.bassmech.findra.web.handler.FacesMessageHandler;
import de.bassmech.findra.web.util.CryptUtil;
import de.bassmech.findra.web.util.statics.enums.FormIds;
import de.bassmech.findra.web.util.statics.enums.UrlFilterType;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;

@Component
@ViewScoped
public class NewClientView implements Serializable {
	private String name;
	private String password;
	private String passwordRepeat;
	private String recoveryCode;

	private Logger logger = LoggerFactory.getLogger(NewClientView.class);
	
	@Autowired
	private ClientRepository clientRepository;

	@PostConstruct
	public void init() {
		name = "";
		password = "";
		passwordRepeat = "";
	}
	
	public void onCreate() {
		logger.debug("onCreate");
		if (isInputValid()) {
			Instant now = Instant.now();
			Client client = new Client();
			client.setName(name);
			client.setCreatedAt(now);
			client.setUpdatedAt(now);
			client.setUuid(UUID.randomUUID().toString());
			client.setPasswordHash(CryptUtil.toPasswordHash(password));
			recoveryCode = RandomStringUtils.randomAlphanumeric(8);
			client.setRecoveryCode(recoveryCode);
			
			client = clientRepository.save(client);
			
			PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
			PrimeFaces.current().executeScript("PF('showRecoveryCodeDialog').show()");

		}
	}
		
	private boolean isInputValid() {
		boolean result = true;
		
		if (name.isBlank()) {
			FacesMessageHandler.addMessageFromKeyWithTagArguments (FacesMessage.SEVERITY_ERROR
					, "must.not.be.empty", "name");
			result = false;
		} else {
			if (clientRepository.findByName(name) != null) {
				FacesMessageHandler.addMessageFromKeyWithTagArguments (FacesMessage.SEVERITY_ERROR
						, "user.with.name.already.exists");
				result = false;
			}
		}
		
		if (password.isBlank()) {
			FacesMessageHandler.addMessageFromKeyWithTagArguments(FacesMessage.SEVERITY_ERROR
				, "must.not.be.empty", "password");
			result = false;
		} else {
			if (!password.equals(passwordRepeat)) {
				FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_ERROR
						, "password.input.not.equal");
				result = false;
			}
		}
		
		return result;
	}
	
	public void closeDialogAndRedirectToLogin() throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect(externalContext.getRequestContextPath() + UrlFilterType.LOGIN.getFullUrl());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordRepeat() {
		return passwordRepeat;
	}

	public void setPasswordRepeat(String passwordRepeat) {
		this.passwordRepeat = passwordRepeat;
	}

	public String getRecoveryCode() {
		return recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

}
