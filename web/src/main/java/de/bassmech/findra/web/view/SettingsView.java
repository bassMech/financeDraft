package de.bassmech.findra.web.view;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import de.bassmech.findra.web.service.SettingsService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class SettingsView extends ViewBase {

    private static final long serialVersionUID = 1L;
    
    private Locale selectedLanguage;
    private Currency selectedCurrency;

    @Autowired
    private SettingsService settingsService;

    @PostConstruct
    public void init() {
    	selectedLanguage = settingsService.getDbLocale();
    	selectedCurrency = settingsService.getDbCurrency();
    }
    
    public void saveSettings() {
    	logger.debug("Saving settings");
    	settingsService.saveDbCurrency(selectedCurrency);
    	settingsService.saveDbLocale(selectedLanguage);
    }
	
	public Locale getSelectedLanguage() {
		return selectedLanguage;
	}

	public void setSelectedLanguage(Locale selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}

	public Currency getSelectedCurrency() {
		return selectedCurrency;
	}

	public void setSelectedCurrency(Currency selectedCurrency) {
		this.selectedCurrency = selectedCurrency;
	}

	public List<Locale> getLanguages() {
		return SettingsService.LANGUAGES;
	}
	
	public List<Currency> getCurrencies() {
		return SettingsService.CURRENCIES;
	}
    
}