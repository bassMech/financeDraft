package de.bassmech.findra.web.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.bassmech.findra.web.view.ViewBase;
import jakarta.faces.context.FacesContext;

public class LocalizedMessageUtil {
	private static Logger logger = LoggerFactory.getLogger(LocalizedMessageUtil.class);
	public static String getMessage(String key, Locale locale) {
//		ResourceBundle bundle = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance()
//				, Statics.MESSAGES_BUNDLE_PATH);
		ResourceBundle bundle = ResourceBundle.getBundle(Statics.MESSAGES_BUNDLE_PATH, locale);
		String value = null;
		try {
			value = bundle.getString(key);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		
		return value;
	}
	
	public static String getTag(String key, Locale locale) {
//		ResourceBundle bundle = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance()
//				, Statics.TAGS_BUNDLE_PATH);
		ResourceBundle bundle = ResourceBundle.getBundle(Statics.TAGS_BUNDLE_PATH, locale);
		
		String value = null;
		try {
			value = bundle.getString(key);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		
		return value;
	}
}
