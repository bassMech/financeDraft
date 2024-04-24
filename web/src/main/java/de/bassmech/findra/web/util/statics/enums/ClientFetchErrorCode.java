package de.bassmech.findra.web.util.statics.enums;

public enum ClientFetchErrorCode implements FindraErrorCode {
	/** Client not found*/
	CLIENT_001("Client %s not found", "error.occured"),
	/** Client with wrong session*/
	CLIENT_002("Client %s with wrong session", "error.occured"),
	/** Client not found*/
	CLIENT_003("Client %s not found or recovery code incorrect", "error.occured"),
	;

	private String loggerMessage;
	private String localizedMessageKey;

	private ClientFetchErrorCode(String loggerMessage, String localizedMessageKey) {
		this.loggerMessage = loggerMessage;
		this.localizedMessageKey = localizedMessageKey;
	}

	@Override
	public String getCode() {
		return this.name();
	}

	@Override
	public String getLocalizedMessageKey() {
		return localizedMessageKey;
	}

	@Override
	public String getLoggerMessage() {
		return loggerMessage;
	}

}
