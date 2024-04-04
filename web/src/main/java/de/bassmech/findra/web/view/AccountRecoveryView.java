package de.bassmech.findra.web.view;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.core.repository.ClientRepository;
import de.bassmech.findra.model.entity.Client;
import de.bassmech.findra.web.handler.FacesMessageHandler;
import de.bassmech.findra.web.service.exception.ClientFetchException;
import de.bassmech.findra.web.util.CryptUtil;
import de.bassmech.findra.web.util.statics.ClientFetchErrorCode;
import de.bassmech.findra.web.util.statics.UrlFilterType;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;

@Component
@ViewScoped
public class AccountRecoveryView implements Serializable {
	private String name;
	private String recoveryCode;
	private String password;
	private String passwordRepeat;

	protected Logger logger = LoggerFactory.getLogger(AccountRecoveryView.class);
	
	@Autowired
	ClientRepository clientRepository;

	@PostConstruct
	public void init() {
		name = "";
		recoveryCode = "";
		password = "";
		passwordRepeat = "";
	}
	
	public void onRecover() throws IOException {
		logger.debug("onRecover");
		if (isInputValid()) {
			Instant now = Instant.now();
			Client client = clientRepository.findByName(name);
			if (client == null) {
				FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_ERROR
						, ClientFetchErrorCode.CLIENT_003.getLocalizedMessageKey());

				throw new ClientFetchException(String.format(ClientFetchErrorCode.CLIENT_003.getLoggerMessage(), name) );	
			}
			if (!client.getRecoveryCode().equals(recoveryCode)) {
				FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_ERROR
						, ClientFetchErrorCode.CLIENT_003.getLocalizedMessageKey());
				throw new ClientFetchException(String.format(ClientFetchErrorCode.CLIENT_003.getLoggerMessage(), recoveryCode) );					
			}
			client.setUpdatedAt(now);
			client.setPasswordHash(CryptUtil.toPasswordHash(password));
			
			client = clientRepository.save(client);
			
			FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_INFO, "accound.recovered.success");
			redirectToLogin();
		}
	}
	
	private void redirectToLogin() throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect(externalContext.getRequestContextPath() + UrlFilterType.LOGIN.getFullUrl());
	}
	
	private boolean isInputValid() {
		boolean result = true;
		
		if (name.isBlank()) {
			FacesMessageHandler.addMessageFromKeyWithTagArguments(FacesMessage.SEVERITY_ERROR
				, "must.not.be.empty", "name");
			result = false;
		}
		
		if (recoveryCode.isBlank()) {
			FacesMessageHandler.addMessageFromKeyWithTagArguments(FacesMessage.SEVERITY_ERROR
				, "must.not.be.empty", "recovery.code");
			result = false;
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
