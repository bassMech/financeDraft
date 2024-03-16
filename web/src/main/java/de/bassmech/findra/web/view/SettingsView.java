package de.bassmech.findra.web.view;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.Year;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import de.bassmech.findra.web.service.SettingService;
import de.bassmech.findra.web.util.FormatterUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.bean.SessionScoped;

@Component
@SessionScoped
public class SettingsView {

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(SettingsView.class);

	private String selectedLanguage;
	private String selectedCurrency;
	private String selectedDateFormat;
	private String selectedNumberGrouping;

	private Map<String, String> selectableLanguages = new HashMap<>();
	private Map<String, String> selectableCurrencies = new HashMap<>();
	private Map<String, String> selectableDateFormats = new HashMap<>();
	private Map<String, String> selectableNumberGroupings = new HashMap<>();

	private List<Integer> selectableYears = new ArrayList<>();
	private int selectedYear;

	private String currencyPreview;
	private boolean selectedCurrencySymbolPositionToggle;

	@Autowired
	private SettingService settingsService;

	@PostConstruct
	@DependsOn("SettingService")
	public void init() {
		selectedLanguage = settingsService.getDbLocale().getLanguage();
		selectedCurrency = settingsService.getDbCurrency().getCurrencyCode();
		selectedDateFormat = settingsService.getDbDateFormat();
		selectedNumberGrouping = settingsService.getDbNumberGrouping();
		selectedCurrencySymbolPositionToggle = settingsService.getDbCurrencySymbolPosition() != "p";

		for (Locale lang : SettingService.LANGUAGES) {
			selectableLanguages.put(lang.getLanguage(), lang.getDisplayLanguage(lang));
		}
		for (Currency currency : SettingService.CURRENCIES) {
			selectableCurrencies.put(currency.getCurrencyCode(), currency.getSymbol());
		}
		for (String dateFormat : SettingService.DATE_FORMATS) {
			selectableDateFormats.put(dateFormat, new SimpleDateFormat(dateFormat).format(Date.from(Instant.now())));
		}
		for (String numberGrouping : SettingService.NUMBER_GROUPINGS) {
			selectableNumberGroupings.put(numberGrouping, FormatterUtil.getCurrencyNumberFormatted(new BigDecimal(123456789), numberGrouping));
		}

		int startYear = Year.now().getValue();
		for (int i = 1; i <= 12; i++) {
			selectableYears.add(startYear);
			startYear++;
		}
		
		updateCurrencyFormatPreview();
	}

	

	public void saveSettings() {
		logger.debug("Saving settings");
		settingsService.saveDbCurrency(Currency.getInstance(selectedCurrency));
		settingsService.saveDbLocale(Locale.forLanguageTag(selectedLanguage));
		settingsService.saveDbDateFormat(selectedDateFormat);
		settingsService.saveDbNumberGrouping(selectedNumberGrouping);
		settingsService.saveDbCurrencySymbolPosition(selectedCurrencySymbolPositionToggle ? "s" : "p");
	}

	public void updateCurrencyFormatPreview() {
		currencyPreview = FormatterUtil.getCurrencyNumberFormattedWithSymbol(new BigDecimal(123456789), selectedNumberGrouping, selectableCurrencies.get(selectedCurrency), !selectedCurrencySymbolPositionToggle);
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

	public String getSelectedDateFormat() {
		return selectedDateFormat;
	}

	public void setSelectedDateFormat(String selectedDateFormat) {
		this.selectedDateFormat = selectedDateFormat;
	}

	public Map<String, String> getSelectableDateFormats() {
		return selectableDateFormats;
	}

	public void setSelectableDateFormats(Map<String, String> selectableDateFormats) {
		this.selectableDateFormats = selectableDateFormats;
	}

	public String getSelectedNumberGrouping() {
		return selectedNumberGrouping;
	}

	public void setSelectedNumberGrouping(String selectedNumberSeparator) {
		this.selectedNumberGrouping = selectedNumberSeparator;
	}

	public Map<String, String> getSelectableNumberGroupings() {
		return selectableNumberGroupings;
	}

	public void setSelectableNumberGroupings(Map<String, String> selectableNumberSeperators) {
		this.selectableNumberGroupings = selectableNumberSeperators;
	}

	public String getCurrencyPreview() {
		return currencyPreview;
	}

	public void setCurrencyPreview(String currencyPreview) {
		this.currencyPreview = currencyPreview;
	}

	public boolean isSelectedCurrencySymbolPositionToggle() {
		return selectedCurrencySymbolPositionToggle;
	}

	public void setSelectedCurrencySymbolPositionToggle(boolean selectedCurrencyPositionToggle) {
		this.selectedCurrencySymbolPositionToggle = selectedCurrencyPositionToggle;
	}

}