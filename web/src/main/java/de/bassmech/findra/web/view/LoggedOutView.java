package de.bassmech.findra.web.view;

import org.springframework.stereotype.Component;

import de.bassmech.findra.web.util.LocalizationUtil;
import de.bassmech.findra.web.util.statics.LogoutMessageKey;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.SessionScoped;

@Component
@SessionScoped
public class LoggedOutView {
	//private String logoutMessageKey;
	
//	@Autowired
//	SessionHandler sessionHandler;

//	@PostConstruct
//	public void init() {	
//		logoutMessageKey = sessionHandler.getAndClearLogoutMessage();
//	}

	public String getLocalizedLogoutMessage() {
		return LocalizationUtil.getMessage(LogoutMessageKey.SUCCESSFUL.getMessage());
	}

    @PreDestroy
    public void destroy() {
        
    }
}
