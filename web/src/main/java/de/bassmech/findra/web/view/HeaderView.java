package de.bassmech.findra.web.view;

import java.io.IOException;
import java.util.Locale;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.web.auth.SessionHandler;
import de.bassmech.findra.web.util.statics.UrlFilterType;
import jakarta.annotation.PostConstruct;
//import jakarta.faces.bean.SessionScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;

@Component
@SessionScoped
public class HeaderView {
	private Logger logger = LoggerFactory.getLogger(HeaderView.class);
	
	@Autowired
	private SessionHandler sessionHandler;
	
	@PostConstruct
	public void init() {
		logger.debug("init called");
	}

	public void onLogout() throws IOException {
		sessionHandler.destroy();
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect(externalContext.getRequestContextPath() + UrlFilterType.LOGGED_OUT.getFullUrl());
	}
	
	public Locale getCurrentLocale() {
		return Locale.getDefault();
	}
	
}
