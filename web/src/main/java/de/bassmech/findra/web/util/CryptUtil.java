package de.bassmech.findra.web.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

import de.bassmech.findra.model.entity.Client;
import de.bassmech.findra.web.service.exception.LoginException;
import de.bassmech.findra.web.util.statics.LoginErrorCode;

public class CryptUtil {
	private final static int rounds = 16;

	public static String toPasswordHash(String toHash) {
		return BCrypt.hashpw(toHash, BCrypt.gensalt(rounds));
	}

	public static boolean areCredentialsCorrect(Client client, String password) throws LoginException {
		if (client == null) {
			throw new LoginException(LoginErrorCode.LOGIN_001);
		}
		if (client.getPasswordHash() == null || client.getUuid() == null) {
			throw new LoginException(LoginErrorCode.LOGIN_004);
		}
		if (password == null) {
			throw new LoginException(LoginErrorCode.LOGIN_003);
		}
		
		if (BCrypt.checkpw(password, client.getPasswordHash())) {
			return true;
		}
		throw new LoginException(String.format(LoginErrorCode.LOGIN_002.getLoggerMessage(), client.getName()));
	}
}
