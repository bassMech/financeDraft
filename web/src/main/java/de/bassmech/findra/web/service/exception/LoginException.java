package de.bassmech.findra.web.service.exception;

import de.bassmech.findra.core.exception.FinDraException;
import de.bassmech.findra.web.util.statics.enums.LoginErrorCode;

public class LoginException extends FinDraException {

	public LoginException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public LoginException(LoginErrorCode errorCode) {
		//super(errorCode);
		super(errorCode.getLoggerMessage());
	}
//	
//	public LoginException(LoginErrorCode errorCode, String customMessage) {
//		super(errorCode, customMessage);
//	}
//
//	public LoginException(LoginErrorCode errorCode, String customMessage, Object arguments) {
//		super(errorCode, customMessage, arguments);
//	}

}
