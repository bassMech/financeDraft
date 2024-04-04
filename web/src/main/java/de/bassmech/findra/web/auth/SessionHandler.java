package de.bassmech.findra.web.auth;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.core.repository.ClientRepository;
import de.bassmech.findra.model.entity.Client;
import de.bassmech.findra.web.handler.FacesMessageHandler;
import de.bassmech.findra.web.service.exception.ClientFetchException;
import de.bassmech.findra.web.service.exception.LoginException;
import de.bassmech.findra.web.util.CryptUtil;
import de.bassmech.findra.web.util.statics.ClientFetchErrorCode;
import de.bassmech.findra.web.util.statics.LoginErrorCode;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Component
@SessionScoped
public class SessionHandler {
	protected Logger logger = LoggerFactory.getLogger(SessionHandler.class);

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	private HttpSession session;

	private String clientUuid;

	private String logoutMessage;

	@PostConstruct
	public void init() {

	}

	@Transactional
	public boolean create(String name, String passwordPlain) throws LoginException {
		Client loginClient = clientRepository.findByName(name);
		if (loginClient == null) {
			throw new LoginException(String.format(LoginErrorCode.LOGIN_001.getLoggerMessage(), name));
		}

		if (loginClient.getDeletedAt() != null) {
			throw new LoginException(String.format(LoginErrorCode.LOGIN_005.getLoggerMessage(), loginClient.getUuid()));
		}

		boolean loginCorrect = CryptUtil.areCredentialsCorrect(loginClient, passwordPlain);
		Instant now = Instant.now();

		clientUuid = loginClient.getUuid();
		loginClient.setLastLogin(now);
		loginClient.setUpdatedAt(now);
		loginClient.setSession(session.getId());

		session.setAttribute("clientUuid", clientUuid);

		clientRepository.saveAndFlush(loginClient);
		// timeOut = now.plus(timeoutValue, ChronoUnit.MINUTES);

		logger.debug("Login successful");
		return true;
	}

	@Transactional
	public Client getLoggedInClientWithSessionCheck() throws ClientFetchException {
		Client client = clientRepository.findByUuid(clientUuid);
		if (client == null) {
			throw new ClientFetchException(
					String.format(ClientFetchErrorCode.CLIENT_001.getLoggerMessage(), clientUuid));
		}

		// TODO unify client check
		if (!client.getSession().equals(session.getId())) {
			throw new ClientFetchException(
					String.format(ClientFetchErrorCode.CLIENT_002.getLoggerMessage(), clientUuid));
		}

		if (!client.getSession().equals(session.getId())) {
			FacesMessageHandler.addMessage(FacesMessage.SEVERITY_ERROR, "error.client.not.found.or.wrong.password");

//			FacesContext facesContext = FacesContext.getCurrentInstance();
//			facesContext.addMessage(null, FacesMessageUtil.getFacesMessage(FacesMessage.SEVERITY_ERROR, "error",
//					"error.client.not.found.or.wrong.password", facesContext.getViewRoot().getLocale()));
			destroy();
			return client;
		}

		return client;

	}

	public boolean isLoggedIn() throws ClientFetchException {
		if (clientUuid == null) {
			return false;
		}
		return getLoggedInClientWithSessionCheck() != null;
	}

//	private void refreshTimeout() {
//		Instant now = Instant.now();
//		timeOut = now.plus(timeoutValue, ChronoUnit.MINUTES);
//		client.setLastUpdate(now);
//		clientRepository.persistAndFlush(client);
//	}

	@Transactional
	public void destroy() {
		if (clientUuid != null) {
			Client tempClient = clientRepository.findByUuid(clientUuid);
			tempClient.setUpdatedAt(Instant.now());
			tempClient.setSession(null);
			clientRepository.saveAndFlush(tempClient);
//			
		}
		clientUuid = null;
		logoutMessage = null;
		session.invalidate();
		logger.debug("Session destroyed");
	}

	public String getAndClearLogoutMessage() {
		String temp = logoutMessage;
		if (temp == null) {
			temp = "";
		}

		logoutMessage = null;
		return temp;
	}

	public void setLogoutMessage(String logoutMessage) {
		this.logoutMessage = logoutMessage;
	}

	public String geClientUuid() {
		return clientUuid;
	}

}
