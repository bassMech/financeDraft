package de.bassmech.findra.web.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.bassmech.findra.web.util.statics.Statics;

public class LocalizationUtil {
	private static Logger logger = LoggerFactory.getLogger(LocalizationUtil.class);

	public static String getMessage(String key) {
		return getFromBundle( ResourceBundle.getBundle(Statics.MESSAGES_BUNDLE_PATH, Locale.getDefault()), key, Locale.getDefault(), null);
	}
	
	public static String getMessage(String key, Locale locale) {
		return getFromBundle( ResourceBundle.getBundle(Statics.MESSAGES_BUNDLE_PATH, locale), key, locale, null);
	}
	
	public static String getMessage(String key, Locale locale, Object... params) {
		return getFromBundle( ResourceBundle.getBundle(Statics.MESSAGES_BUNDLE_PATH, locale), key, locale, params);
	}

	public static String getTag(String key) {
		return getFromBundle( ResourceBundle.getBundle(Statics.TAGS_BUNDLE_PATH, Locale.getDefault()), key, Locale.getDefault(), null);
	}
	
	public static String getTag(String key, Locale locale) {
		return getFromBundle( ResourceBundle.getBundle(Statics.TAGS_BUNDLE_PATH, locale), key, locale, null);
	}
	
	private static String getFromBundle(ResourceBundle bundle, String key, Locale locale, Object... params) {
		String value = null;
		try {
			value = bundle.getString(key);
			if (params != null) {
				value = value.formatted(params);
			}
			
		} catch (Exception e) {
			logger.error("Error on key extraction. Key was: " + key, e);
		}

		return value;
	}
}
