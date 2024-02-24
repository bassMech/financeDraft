package de.bassmech.findra.web.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.bassmech.findra.web.util.statics.Statics;

public class LocalizedMessageUtil {
	private static Logger logger = LoggerFactory.getLogger(LocalizedMessageUtil.class);

	public static String getMessage(String key, Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle(Statics.MESSAGES_BUNDLE_PATH, locale);
		String value = null;
		try {
			value = bundle.getString(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return value;
	}
	
	public static String getMessage(String key, Locale locale, Object... params) {
		ResourceBundle bundle = ResourceBundle.getBundle(Statics.MESSAGES_BUNDLE_PATH, locale);
		String value = null;
		try {
			value = bundle.getString(key);
			value = value.formatted(params);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return value;
	}

	public static String getTag(String key, Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle(Statics.TAGS_BUNDLE_PATH, locale);

		String value = null;
		try {
			value = bundle.getString(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return value;
	}
}
