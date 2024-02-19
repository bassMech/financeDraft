package de.bassmech.findra.web.handler;

import org.primefaces.PrimeFaces;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

public class FacesMessageHandler {
	public static void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
		PrimeFaces.current().ajax().update("mainForm:growlMessage");
	}
}
