package de.bassmech.findra.web.view;

import java.io.IOException;
import java.util.Locale;

import org.springframework.stereotype.Component;

import de.bassmech.findra.web.util.statics.enums.UrlFilterType;
//import jakarta.faces.bean.SessionScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;

@Component
@SessionScoped
public class HeaderView {
		
	public void onLogout() throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect(externalContext.getRequestContextPath() + UrlFilterType.LOGGED_OUT.getFullUrl());
	}
	
	public Locale getCurrentLocale() {
		return Locale.getDefault();
	}
	
}
