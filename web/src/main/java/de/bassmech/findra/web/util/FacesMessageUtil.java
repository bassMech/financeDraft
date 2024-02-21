package de.bassmech.findra.web.util;

import java.util.Locale;
import java.util.ResourceBundle;

import de.bassmech.findra.web.util.statics.Statics;
import jakarta.faces.application.FacesMessage;

public class FacesMessageUtil {
	public static FacesMessage getFacesMessage(FacesMessage.Severity severity, String summary, String detail, Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle(Statics.MESSAGES_BUNDLE_PATH, locale);
	
		return new FacesMessage(severity, bundle.getString(summary), bundle.getString(detail));
	}
}
