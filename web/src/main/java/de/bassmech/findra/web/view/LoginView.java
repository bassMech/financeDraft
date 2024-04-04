package de.bassmech.findra.web.view;


import java.io.IOException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.bassmech.findra.web.auth.SessionHandler;
import de.bassmech.findra.web.handler.FacesMessageHandler;
import de.bassmech.findra.web.service.exception.LoginException;
import de.bassmech.findra.web.util.statics.LoginErrorCode;
import de.bassmech.findra.web.util.statics.UrlFilterType;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Component
@SessionScoped
public class LoginView implements Serializable {
	private String name;
	private String password;
	
	protected Logger logger = LoggerFactory.getLogger(LoginView.class);
	
	@Inject
	private SessionHandler sessionHandler;
		
	@PostConstruct
	public void init() {
		name = "";
		password = "";
	}
	
	public void process() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		
		try {
			if (sessionHandler.create(name, password)) {
				try {
					externalContext.redirect(externalContext.getRequestContextPath() + UrlFilterType.DASHBOARD.getFullUrl());
				} catch (IOException e) {
					//TODO errorpage
					logger.error("error on redirection", e);
				}
				return;
			} 
			
		} catch (LoginException e) {
			logger.error("error on login", e);
			
		}
		FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_ERROR, "error.client.not.found.or.wrong.password");
		
//		facesContext.addMessage(null, FacesMessageUtil.getFacesMessage(FacesMessage.SEVERITY_ERROR, "error", "error.client.not.found.or.wrong.password"
//				, facesContext.getViewRoot().getLocale()));
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
	
}
