package de.bassmech.findra.web.service;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

@Service
public class SettingsService {
	public static final List<Locale> LANGUAGES = Arrays.asList(Locale.GERMAN, Locale.ENGLISH);
	public static final List<String> CURRENCIES = Arrays.asList(Currency.getInstance("EUR").getSymbol(), Currency.getInstance("USD").getSymbol(), Currency.getInstance("GBP").getSymbol());
	
}
