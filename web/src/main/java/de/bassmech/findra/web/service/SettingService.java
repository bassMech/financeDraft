package de.bassmech.findra.web.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.core.repository.SettingRepository;
import de.bassmech.findra.model.entity.Setting;
import de.bassmech.findra.model.statics.SettingCode;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

@Component
public class SettingService {
	private Logger logger = LoggerFactory.getLogger(SettingService.class);
	
	public static final List<Locale> LANGUAGES = Arrays.asList(Locale.GERMAN, Locale.ENGLISH);
	public static final List<Currency> CURRENCIES = Arrays.asList(Currency.getInstance("EUR"),
			Currency.getInstance("USD"), Currency.getInstance("GBP"));

	@Autowired
	private SettingRepository settingRespoRepository;

	private Locale dbLocale;
	private Currency dbCurrency;
	
	@PostConstruct
	public void init() {
		getDbLocale();
		getDbCurrency();
		
		Locale.setDefault(dbLocale);
	}

	public Locale getDbLocale() {
		if (dbLocale == null) {
			Setting localeSetting = settingRespoRepository.findByCode(SettingCode.LOCALE);
			if (localeSetting == null) {
				localeSetting = new Setting();
				localeSetting.setCode(SettingCode.LOCALE);
				localeSetting.setEntry(Locale.ENGLISH.getLanguage());
				localeSetting.setUpdateAt(Instant.now());

				localeSetting = settingRespoRepository.save(localeSetting);
			}
			dbLocale = Locale.forLanguageTag(localeSetting.getEntry());
		}
		return dbLocale;
	}

	public void saveDbLocale(Locale locale) {
		logger.debug("Saving locale: " + locale.getLanguage());
		if (locale.equals(dbLocale)) {
			logger.debug("Locale already set. Skip saving");
			return;
		}
		
		Setting localeSetting = settingRespoRepository.findByCode(SettingCode.LOCALE);
		if (localeSetting == null) {
			localeSetting = new Setting();
			localeSetting.setCode(SettingCode.LOCALE);
			localeSetting.setUpdateAt(Instant.now());
		}
		localeSetting.setEntry(locale.getLanguage());
		
		localeSetting = settingRespoRepository.save(localeSetting);
		dbLocale = Locale.forLanguageTag(localeSetting.getEntry());
		
		FacesContext.getCurrentInstance().getViewRoot().setLocale(dbLocale);
		Locale.setDefault(dbLocale);
		logger.info("language updated to: " + FacesContext.getCurrentInstance()
		.getViewRoot().getLocale().getISO3Language());
	}
	
	public Currency getDbCurrency() {
		if (dbCurrency == null) {
			Setting currencySetting = settingRespoRepository.findByCode(SettingCode.CURRENCY);
			if (currencySetting == null) {
				currencySetting = new Setting();
				currencySetting.setCode(SettingCode.CURRENCY);
				currencySetting.setEntry(Currency.getInstance("EUR").getCurrencyCode());
				currencySetting.setUpdateAt(Instant.now());

				currencySetting = settingRespoRepository.save(currencySetting);
			}
			dbCurrency = Currency.getInstance(currencySetting.getEntry());
		}
		return dbCurrency;
	}
	
	public void saveDbCurrency(Currency currency) {
		logger.debug("Saving currency: " + currency.getCurrencyCode());
		if (currency.equals(dbCurrency)) {
			logger.debug("Currency already set. Skip saving");
			return;
		}
		Setting currencySetting = settingRespoRepository.findByCode(SettingCode.CURRENCY);
		if (currencySetting == null) {
			currencySetting = new Setting();
			currencySetting.setCode(SettingCode.CURRENCY);
			currencySetting.setUpdateAt(Instant.now());
		}
		currencySetting.setEntry(currency.getCurrencyCode());
		
		currencySetting = settingRespoRepository.save(currencySetting);
		dbCurrency = Currency.getInstance(currencySetting.getEntry());
		
	}
}
