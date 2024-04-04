package de.bassmech.findra.web.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.web.auth.SessionHandler;
import de.bassmech.findra.web.util.LocalizationUtil;
import de.bassmech.findra.web.util.statics.LogoutMessageKey;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;

@Component
@SessionScoped
public class LoggedOutView {
	
	@Autowired
	SessionHandler sessionHandler;

	@PostConstruct
	public void init() {	
		sessionHandler.destroy();
	}

	public String getLocalizedLogoutMessage() {
		return LocalizationUtil.getMessage(LogoutMessageKey.SUCCESSFUL.getMessage());
	}

}
