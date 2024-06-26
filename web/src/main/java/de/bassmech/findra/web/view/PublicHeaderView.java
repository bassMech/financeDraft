package de.bassmech.findra.web.view;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.bassmech.findra.web.service.SettingService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;

@Component
@SessionScoped
public class PublicHeaderView {
	private Logger logger = LoggerFactory.getLogger(PublicHeaderView.class);
	private Map<String, String> selectableLanguages = new HashMap<>();
	private String selectedLanguage;
		
	@PostConstruct
	public void init() {
		selectedLanguage = Locale.getDefault().getDisplayLanguage(Locale.getDefault());
		for (Locale lang : SettingService.LANGUAGES) {
			selectableLanguages.put(lang.getLanguage(), lang.getDisplayLanguage(lang));
		}
	}
	
	public Locale getCurrentLocale() {
		return Locale.getDefault();
	}
	
	public void changeLanguage() {
		Locale.setDefault(Locale.forLanguageTag(selectedLanguage));
		logger.debug(String.format("Changing language to ", Locale.getDefault().getDisplayLanguage()));
	}

	public Map<String, String> getSelectableLanguages() {
		return selectableLanguages;
	}

	public void setSelectableLanguages(Map<String, String> selectableLanguages) {
		this.selectableLanguages = selectableLanguages;
	}

	public String getSelectedLanguage() {
		return selectedLanguage;
	}

	public void setSelectedLanguage(String selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}
	
	
}
