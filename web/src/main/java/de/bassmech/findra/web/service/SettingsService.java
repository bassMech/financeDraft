package de.bassmech.findra.web.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bassmech.findra.core.repository.SettingRepository;
import de.bassmech.findra.model.entity.Setting;
import de.bassmech.findra.model.statics.SettingCode;

@Service
public class SettingsService {
	private Logger logger = LoggerFactory.getLogger(SettingsService.class);
	
	public static final List<Locale> LANGUAGES = Arrays.asList(Locale.GERMAN, Locale.ENGLISH);
	public static final List<Currency> CURRENCIES = Arrays.asList(Currency.getInstance("EUR"),
			Currency.getInstance("USD"), Currency.getInstance("GBP"));

	@Autowired
	private SettingRepository settingRespoRepository;

	private Locale dbLocale;
	private Currency dbCurrency;

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
		Setting localeSetting = settingRespoRepository.findByCode(SettingCode.LOCALE);
		if (localeSetting == null) {
			localeSetting = new Setting();
			localeSetting.setCode(SettingCode.LOCALE);
			localeSetting.setUpdateAt(Instant.now());
		}
		localeSetting.setEntry(locale.getLanguage());
		
		localeSetting = settingRespoRepository.save(localeSetting);
		dbLocale = Locale.forLanguageTag(localeSetting.getEntry());
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
