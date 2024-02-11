package de.bassmech.findra.web.view;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.web.service.SettingsService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;

@Component
@ViewScoped
public class SettingsView extends ViewBase {

    private static final long serialVersionUID = 1L;
    
    private Locale selectedLanguage;
    private Currency selectedCurrency;
    private List<Locale> selectableLanguages;
    private List<Currency> selectableCurrencies;
    

    @Autowired
    private SettingsService settingsService;

    @PostConstruct
    public void init() {
    	selectableLanguages = new ArrayList<>(SettingsService.LANGUAGES);
    	selectableCurrencies = new ArrayList<>(SettingsService.CURRENCIES);
    	
    	selectedLanguage = settingsService.getDbLocale();
    	selectedCurrency = settingsService.getDbCurrency();
    }
    
    public void saveSettings() {
    	logger.debug("Saving settings");
    	settingsService.saveDbCurrency(selectedCurrency);
    	settingsService.saveDbLocale(selectedLanguage);
    }
    
    public void onLanguageChange() {
    	logger.debug("new lang: " +  selectedLanguage.getDisplayLanguage());
    }
    
    public void onCurrencyChange() {
    	logger.debug("new curr: " +  selectedCurrency.getCurrencyCode());
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

	public List<Locale> getSelectableLanguages() {
		return selectableLanguages;
	}

	public void setSelectableLanguages(List<Locale> selectableLanguages) {
		this.selectableLanguages = selectableLanguages;
	}

	public List<Currency> getSelectableCurrencies() {
		return selectableCurrencies;
	}

	public void setSelectableCurrencies(List<Currency> selectableCurrencies) {
		this.selectableCurrencies = selectableCurrencies;
	}
    
}