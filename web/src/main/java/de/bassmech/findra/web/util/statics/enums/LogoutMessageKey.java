package de.bassmech.findra.web.util.statics.enums;

public enum LogoutMessageKey {
	SUCCESSFUL("logged.out.successfully"),
	TIMEOUT("logged.out.timeout"),
	SESSION_INVALIDATED("logged.out.session.invalidated"),
	;
	
	public static final String KEY = "logoutMessageKey";
	
	private String message;
	
	private LogoutMessageKey(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
