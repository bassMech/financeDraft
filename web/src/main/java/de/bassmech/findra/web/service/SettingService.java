package de.bassmech.findra.web.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.core.repository.SettingRepository;
import de.bassmech.findra.model.entity.Client;
import de.bassmech.findra.model.entity.Setting;
import de.bassmech.findra.model.statics.SettingCode;
import de.bassmech.findra.web.util.statics.enums.SettingHolder;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

@Component
public class SettingService {
	private Logger logger = LoggerFactory.getLogger(SettingService.class);

	public static final List<Locale> LANGUAGES = Arrays.asList(Locale.GERMAN, Locale.ENGLISH);
	public static final List<Currency> CURRENCIES = Arrays.asList(Currency.getInstance("EUR"),
			Currency.getInstance("USD"), Currency.getInstance("GBP"));
	public static final List<String> DATE_FORMATS = Arrays.asList("yyyy/MM/dd", "dd.MM.yyyy");
	public static final List<String> NUMBER_GROUPINGS = Arrays.asList("._,", ",_.");
	public static final List<String> CURRENCY_SYMBOL_POSITION = Arrays.asList("p", "s");

	@Autowired
	private SettingRepository settingRespoRepository;
	
	@PostConstruct
	public void init() {
		initAll();
//
//		Locale.setDefault(dbLocale);
	}

	private void initAll() {
		logger.debug("init called");
		List<SettingCode> settingCodes = Arrays.asList(SettingCode.values());
		List<Setting> settings = settingRespoRepository.findAll();
		for (Setting setting : settings) {
			switch (setting.getCode()) {
			case ACCOUNT_TRANSACTION_LAYOUT:
				logger.warn("Implementation needed");
				break;
			case CURRENCY:
				SettingHolder.getInstance().setDbCurrency(Currency.getInstance(setting.getEntry()));
				break;
			case CURRENCY_SYMBOL_POSITION:
				SettingHolder.getInstance().setDbCurrencySymbolPosition(setting.getEntry());
				break;
			case DATE_FORMAT:
				SettingHolder.getInstance().setDbDateFormat(setting.getEntry());
				break;
			case LOCALE:
				SettingHolder.getInstance().setDbLocale(Locale.forLanguageTag(setting.getEntry()));
				break;
			case NUMBER_GROUPING:
				SettingHolder.getInstance().setDbNumberGrouping(setting.getEntry());
				splitAndProvideNumberGrouping();
				break;
			default:
				throw new IllegalStateException(String.format("No implementation for code:", setting.getCode()));

			}
		}
	}

	public Locale getDbLocale(Client client) {
		if (SettingHolder.getInstance().getDbLocale() == null) {
			Setting localeSetting = settingRespoRepository.findByCode(SettingCode.LOCALE);
			if (localeSetting == null) {
				localeSetting = new Setting();
				localeSetting.setCode(SettingCode.LOCALE);
				localeSetting.setEntry(Locale.ENGLISH.getLanguage());
				localeSetting.setUpdateAt(Instant.now());
				localeSetting.setClient(client);

				localeSetting = settingRespoRepository.save(localeSetting);
			}
			SettingHolder.getInstance().setDbLocale(Locale.forLanguageTag(localeSetting.getEntry()));
		}
		return SettingHolder.getInstance().getDbLocale();
	}

	public void saveDbLocale(Client client, Locale locale) {
		logger.debug("Saving locale: " + locale.getLanguage());
		if (locale.equals(SettingHolder.getInstance().getDbLocale())) {
			logger.debug("Locale already set. Skip saving");
			return;
		}

		SettingHolder.getInstance().setDbLocale(Locale.forLanguageTag(saveDbSetting(client, SettingCode.DATE_FORMAT, locale.getLanguage()).getEntry()));

		FacesContext.getCurrentInstance().getViewRoot().setLocale(SettingHolder.getInstance().getDbLocale());
		Locale.setDefault(SettingHolder.getInstance().getDbLocale());
		logger.info("language updated to: "
				+ FacesContext.getCurrentInstance().getViewRoot().getLocale().getISO3Language());
		// PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
	}

	public Currency getDbCurrency(Client client) {
		if (SettingHolder.getInstance().getDbCurrency() == null) {
			Setting currencySetting = settingRespoRepository.findByCode(SettingCode.CURRENCY);
			if (currencySetting == null) {
				currencySetting = new Setting();
				currencySetting.setCode(SettingCode.CURRENCY);
				currencySetting.setEntry(Currency.getInstance("EUR").getCurrencyCode());
				currencySetting.setUpdateAt(Instant.now());
				currencySetting.setClient(client);

				currencySetting = settingRespoRepository.save(currencySetting);
			}
			SettingHolder.getInstance().setDbCurrency(Currency.getInstance(currencySetting.getEntry()));
		}
		return SettingHolder.getInstance().getDbCurrency();
	}

	public void saveDbCurrency(Client client, Currency currency) {
		logger.debug("Saving currency: " + currency.getCurrencyCode());
		if (currency.equals(SettingHolder.getInstance().getDbCurrency())) {
			logger.debug("Currency already set. Skip saving");
			return;
		}

		SettingHolder.getInstance().setDbCurrency(Currency.getInstance(saveDbSetting(client, SettingCode.CURRENCY, currency.getCurrencyCode()).getEntry()));
	}

	public String getDbDateFormat(Client client) {
		if (SettingHolder.getInstance().getDbDateFormat() == null) {
			Setting dateFormatSetting = settingRespoRepository.findByCode(SettingCode.DATE_FORMAT);
			if (dateFormatSetting == null) {
				dateFormatSetting = new Setting();
				dateFormatSetting.setCode(SettingCode.DATE_FORMAT);
				dateFormatSetting.setEntry(DATE_FORMATS.get(0));
				dateFormatSetting.setUpdateAt(Instant.now());
				dateFormatSetting.setClient(client);

				dateFormatSetting = settingRespoRepository.save(dateFormatSetting);
			}
			SettingHolder.getInstance().setDbDateFormat(dateFormatSetting.getEntry());
		}
		return SettingHolder.getInstance().getDbDateFormat();
	}

	public void saveDbDateFormat(Client client, String dateFormat) {
		logger.debug("Saving date format: " + dateFormat);
		if (dateFormat.equals(SettingHolder.getInstance().getDbDateFormat())) {
			logger.debug("Date format already set. Skip saving");
			return;
		}

		SettingHolder.getInstance().setDbDateFormat(saveDbSetting(client, SettingCode.DATE_FORMAT, dateFormat).getEntry());
	}

	public Setting saveDbSetting(Client client, SettingCode settingCode, String valueToSave) {

		Setting setting = settingRespoRepository.findByCode(settingCode);
		if (setting == null) {
			setting = new Setting();
			setting.setCode(settingCode);
			setting.setUpdateAt(Instant.now());
			setting.setClient(client);
		}
		setting.setEntry(valueToSave);

		return settingRespoRepository.save(setting);
	}

	public String getDbNumberThousandSepartor(Client client) {
		if (SettingHolder.getInstance().getDbNumberThousandSeparator() == null) {
			getDbNumberGrouping(client);
		}
		return SettingHolder.getInstance().getDbNumberThousandSeparator();
	}

	public String getDbNumberDigitSeparator(Client client) {
		if (SettingHolder.getInstance().getDbNumberDecimalSeparator() == null) {
			getDbNumberGrouping(client);
		}
		return SettingHolder.getInstance().getDbNumberDecimalSeparator();
	}

	public String getDbNumberGrouping(Client client) {
		if (SettingHolder.getInstance().getDbNumberGrouping() == null) {
			Setting setting = settingRespoRepository.findByCode(SettingCode.NUMBER_GROUPING);
			if (setting == null) {
				setting = new Setting();
				setting.setCode(SettingCode.NUMBER_GROUPING);
				setting.setEntry(NUMBER_GROUPINGS.get(0));
				setting.setUpdateAt(Instant.now());
				setting.setClient(client);

				setting = settingRespoRepository.save(setting);
			}
			SettingHolder.getInstance().setDbNumberGrouping(setting.getEntry());
		}

		splitAndProvideNumberGrouping();
		return SettingHolder.getInstance().getDbNumberGrouping();
	}

	private void splitAndProvideNumberGrouping() {
		String[] split = SettingHolder.getInstance().getDbNumberGrouping().split("_");
		SettingHolder.getInstance().setDbNumberThousandSeparator(split[0]);
		SettingHolder.getInstance().setDbNumberDecimalSeparator(split[1]);
	}

	public void saveDbNumberGrouping(Client client, String numberSeparator) {
		logger.debug("Saving number grouping: " + numberSeparator);
		if (numberSeparator.equals(SettingHolder.getInstance().getDbNumberGrouping())) {
			logger.debug("Number grouping already set. Skip saving");
			return;
		}

		SettingHolder.getInstance().setDbNumberGrouping(saveDbSetting(client, SettingCode.NUMBER_GROUPING
				, numberSeparator).getEntry());
	}

	public String getDbCurrencySymbolPosition(Client client) {
		if (SettingHolder.getInstance().getDbCurrencySymbolPosition() == null) {
			Setting setting = settingRespoRepository.findByCode(SettingCode.CURRENCY_SYMBOL_POSITION);
			if (setting == null) {
				setting = new Setting();
				setting.setCode(SettingCode.CURRENCY_SYMBOL_POSITION);
				setting.setEntry(CURRENCY_SYMBOL_POSITION.get(1));
				setting.setUpdateAt(Instant.now());
				setting.setClient(client);

				setting = settingRespoRepository.save(setting);
			}
			SettingHolder.getInstance().setDbCurrencySymbolPosition(setting.getEntry());
		}
		return SettingHolder.getInstance().getDbCurrencySymbolPosition();
	}

	public void saveDbCurrencySymbolPosition(Client client, String currencySymbolPosition) {
		logger.debug("Saving currency symbol position: " + currencySymbolPosition);
		if (currencySymbolPosition.equals(SettingHolder.getInstance().getDbCurrencySymbolPosition())) {
			logger.debug("Currency symbol position already set. Skip saving");
			return;
		}

		SettingHolder.getInstance().setDbCurrencySymbolPosition(saveDbSetting(client, SettingCode.CURRENCY_SYMBOL_POSITION, currencySymbolPosition)
				.getEntry());
	}
}
