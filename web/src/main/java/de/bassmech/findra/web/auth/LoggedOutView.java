package de.bassmech.findra.web.auth;

import java.io.Serializable;

import de.bassmech.findra.web.util.LocalizationUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@SessionScoped
public class LoggedOutView implements Serializable {
	private String logoutMessageKey;
	
	@Inject
	SessionHandler sessionHandler;

	@PostConstruct
	public void init() {
//		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
//		logoutMessageKey = (String) flash.get(LogoutMessageKey.KEY);
//		System.out.println(flash.size());
		
		logoutMessageKey = sessionHandler.getAndClearLogoutMessage();

	}

//	@Override
//	public void checkLanguageChange() {
//		// TODO Auto-generated method stub
//	}
//
	public String getLocalizedLogoutMessage() {
		return LocalizationUtil.getMessage(logoutMessageKey, FacesContext.getCurrentInstance().getViewRoot().getLocale());
	}

    @PreDestroy
    public void destroy() {
        // Your code here.
    }
}
