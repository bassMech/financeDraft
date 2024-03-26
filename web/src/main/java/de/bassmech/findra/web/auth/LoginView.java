package de.bassmech.findra.web.auth;


import java.io.IOException;
import java.io.Serializable;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.bassmech.findra.web.util.FacesMessageUtil;
import de.bassmech.findra.web.util.statics.UrlFilterType;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@SessionScoped
public class LoginView implements Serializable {
	private String mail;
	private String password;
	
	protected Logger logger = LoggerFactory.getLogger(LoginView.class);
	
	@Inject
	private SessionHandler sessionHandler;
		
	@PostConstruct
	public void init() {
		mail = "";
		password = "";
	}
	
	public void process() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		

		try {
			if (sessionHandler.create(mail, password)) {
				try {
					externalContext.redirect(externalContext.getRequestContextPath() + UrlFilterType.DASHBOARD.getFullUrl());
				} catch (IOException e) {
					//TODO errorpage
					logger.error(e);
				}
				return;
			} 
			
		} catch (LoginException e) {
			logger.error(e);
		}
		
		facesContext.addMessage(null, FacesMessageUtil.getFacesMessage(FacesMessage.SEVERITY_ERROR, "error", "error.member.not.found.or.wrong.password"
				, facesContext.getViewRoot().getLocale()));
		
		
	}


	public String getMail() {
		return mail;
	}

	public void setMail(String identifier) {
		this.mail = identifier;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
