package de.bassmech.findra.web.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizedMessageUtil {
	public static String getMessage(String key, Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle(Statics.MESSAGES_BUNDLE_PATH, locale);
		
		String value = null;
		try {
			bundle.getString(key);
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		
		return value;
	}
}
