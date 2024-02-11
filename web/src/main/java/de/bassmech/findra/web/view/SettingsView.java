package de.bassmech.findra.web.view;

import java.time.Year;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.web.service.SettingsService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;

@Component
@ViewScoped
public class SettingsView extends ViewBase {

	private static final long serialVersionUID = 1L;

	private String selectedLanguage;
	private String selectedCurrency;
	private Map<String, String> selectableLanguages = new HashMap<>();
	private Map<String, String> selectableCurrencies = new HashMap<>();

	private List<Integer> selectableYears = new ArrayList<>();
	private int selectedYear;

	@Autowired
	private SettingsService settingsService;

	@PostConstruct
	public void init() {
		for (Locale lang : SettingsService.LANGUAGES) {
			selectableLanguages.put(lang.getLanguage(), lang.getDisplayLanguage(lang));
		}
		for (Currency currency : SettingsService.CURRENCIES) {
			selectableCurrencies.put(currency.getCurrencyCode(), currency.getSymbol());
		}

		selectedLanguage = settingsService.getDbLocale().getLanguage();
		selectedCurrency = settingsService.getDbCurrency().getCurrencyCode();

		int startYear = Year.now().getValue();
		for (int i = 1; i <= 12; i++) {
			selectableYears.add(startYear);
			startYear++;
		}
	}

	public void saveSettings() {
		logger.debug("Saving settings");
		settingsService.saveDbCurrency(Currency.getInstance(selectedCurrency));
		settingsService.saveDbLocale(Locale.forLanguageTag(selectedLanguage));
	}

	public void onLanguageChange() {
		logger.debug("new lang: " + selectedLanguage);
	}

	public void onCurrencyChange() {
		logger.debug("new curr: " + selectedCurrency);
	}

	public void onYearChanged() {
		logger.debug("onYearChanged");
		logger.debug("Year was changed to: " + selectedYear);
	}

	public String getSelectedLanguage() {
		return selectedLanguage;
	}

	public void setSelectedLanguage(String selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}

	public Map<String, String> getSelectableLanguages() {
		return selectableLanguages;
	}

	public void setSelectableLanguages(Map<String, String> selectableLanguages) {
		this.selectableLanguages = selectableLanguages;
	}

	public String getSelectedCurrency() {
		return selectedCurrency;
	}

	public void setSelectedCurrency(String selectedCurrency) {
		this.selectedCurrency = selectedCurrency;
	}

	public Map<String, String> getSelectableCurrencies() {
		return selectableCurrencies;
	}

	public void setSelectableCurrencies(Map<String, String> selectableCurrencies) {
		this.selectableCurrencies = selectableCurrencies;
	}

	public List<Integer> getSelectableYears() {
		return selectableYears;
	}

	public void setSelectableYears(List<Integer> selectableYears) {
		this.selectableYears = selectableYears;
	}

	public int getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(int selectedYear) {
		this.selectedYear = selectedYear;
	}

}