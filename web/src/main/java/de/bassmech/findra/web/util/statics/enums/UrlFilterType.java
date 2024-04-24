package de.bassmech.findra.web.util.statics.enums;

public enum UrlFilterType {
	START(UrlFilterType.PUBLIC, "index.xhtml"),
	LOGIN(UrlFilterType.PUBLIC, "login.xhtml"),
	LOGGED_OUT(UrlFilterType.PUBLIC, "loggedOut.xhtml"),
	
	DASHBOARD(UrlFilterType.CLIENT, "dashboard.xhtml"),
	;
	
	private static final String PUBLIC = "/template/public/";
	private static final String CLIENT = "/template/client/";
	
	private String permission;
	private String url;
	
	private UrlFilterType(String permission, String url) {
		this.permission = permission;
		this.url = url;
	}

	public String getPermission() {
		return permission;
	}

	public String getUrl() {
		return url;
	}
	
	public String getFullUrl() {
		return permission + url;
	}
}
