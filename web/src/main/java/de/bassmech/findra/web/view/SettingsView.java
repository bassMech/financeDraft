package de.bassmech.findra.web.view;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import de.bassmech.findra.web.service.SettingsService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class SettingsView extends ViewBase {

    private static final long serialVersionUID = 1L;
    
    private Locale selectedLanguage;
    private String selectedCurrency;

    @Autowired
    private SettingsService settingsService;

    @PostConstruct
    public void init() {
    	selectedLanguage = SettingsService.LANGUAGES.get(0);
    	selectedCurrency = SettingsService.CURRENCIES.get(0);
    }

	public Locale getSelectedLanguage() {
		return selectedLanguage;
	}

	public String getSelectedCurrency() {
		return selectedCurrency;
	}
	
	public List<Locale> getLanguages() {
		return SettingsService.LANGUAGES;
	}
	
	public List<String> getCurrencies() {
		return SettingsService.CURRENCIES;
	}
    
}