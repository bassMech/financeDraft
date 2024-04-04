package de.bassmech.findra.web.handler;

import org.primefaces.PrimeFaces;

import de.bassmech.findra.web.util.LocalizationUtil;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

public class FacesMessageHandler {
	private static void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		PrimeFaces.current().ajax().update("mainForm:growlMessage");
	}
	
	public static void addMessageFromKey(FacesMessage.Severity severity, String messageKey) {
		addMessage(severity, LocalizationUtil.getMessage(messageKey));
	}
	
	public static void addMessageFromKey(FacesMessage.Severity severity, String messageKey, Object... arguments) {
		addMessage(severity, LocalizationUtil.getMessage(messageKey, arguments));
	}
	
	public static void addMessageFromKeyWithTagArguments(FacesMessage.Severity severity
			, String messageKey, Object... arguments) {
		String[] localizedTagArguments = new String[arguments.length];
		for (int iTag = 0; iTag < arguments.length; iTag++  ) {
			String tag = (String) arguments[iTag];
			localizedTagArguments[iTag] = LocalizationUtil.getTag(tag);
		}
		addMessage(severity, LocalizationUtil.getMessage(messageKey, arguments));
	}
	
	public static void addMessage(FacesMessage.Severity severity, String detail) {
		String summaryTagKey;
		if (FacesMessage.SEVERITY_INFO.getOrdinal() == severity.getOrdinal()) {
			summaryTagKey = "hint";
		} else if (FacesMessage.SEVERITY_WARN.getOrdinal() == severity.getOrdinal()) {
			summaryTagKey = "warning";
		} else {
			summaryTagKey = "error";
		}
		String summary = LocalizationUtil.getTag(summaryTagKey);
		
		addMessage(severity, summary, detail);
	}
}
