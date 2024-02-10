package de.bassmech.findra.web.view;

import java.io.Serializable;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.context.FacesContext;

public abstract class ViewBase implements Serializable {
	protected Logger logger = LoggerFactory.getLogger(ViewBase.class);
	
	protected Locale currentLocale;
	
	public void checkLanguageChange() {
		currentLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

	public Locale getCurrentLocale() {
		return currentLocale;
	}
	
	
}
