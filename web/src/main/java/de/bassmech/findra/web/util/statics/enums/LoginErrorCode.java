package de.bassmech.findra.web.util.statics.enums;

public enum LoginErrorCode implements FindraErrorCode {
	/** Client not present*/
	LOGIN_001("Client with name %s not present", "error.client.not.found.or.wrong.password"),
	/** Password not matching*/
	LOGIN_002("Password not matching for client: %s", "error.client.not.found.or.wrong.password"),
	/** No password provided */
	LOGIN_003("No password provided", "error occured"),
	/** Client is missing password or uuid*/
	LOGIN_004("Client is missing password or uuid", "error occured"),
	/** Client deleted or suspended*/
	LOGIN_005("Client with uuid %s is deleted", "error.client.not.found.or.wrong.password"),
	;

	private String loggerMessage;
	private String localizedMessageKey;

	private LoginErrorCode(String loggerMessage, String localizedMessageKey) {
		this.loggerMessage = loggerMessage;
		this.localizedMessageKey = localizedMessageKey;
	}

	@Override
	public String getCode() {
		return this.name();
	}

	@Override
	public String getLoggerMessage() {
		return this.loggerMessage;
	}

	@Override
	public String getLocalizedMessageKey() {
		return localizedMessageKey;
	}

}
