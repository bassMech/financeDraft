package de.bassmech.findra.web.view;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import de.bassmech.findra.model.statics.ConfigurationCode;
import de.bassmech.findra.web.service.ConfigurationService;
import de.bassmech.findra.web.service.SettingService;
import jakarta.annotation.PostConstruct;
//import jakarta.faces.bean.SessionScoped;
import jakarta.enterprise.context.SessionScoped;

@Component
@SessionScoped
public class HeaderView {
	private Logger logger = LoggerFactory.getLogger(HeaderView.class);
//	@Inject
//	LanguageService languageService;
	
	@Autowired
	private ConfigurationService configurationHolder;
	
	@Autowired
	private SettingService settingService;
	
//	private List<LanguageViewModel> languages;
//
//	protected LanguageViewModel selectedLanguage;

	@PostConstruct
	@DependsOn("SettingService")
	public void init() {
//		currentLocale = settingService.getDbLocale();
////		Locale browserLocale = FacesContext.getCurrentInstance()
////				.getViewRoot().getLocale();
////		Optional<LanguageViewModel> language= languages.stream().filter(x -> x.getAlphaCode().equals(browserLocale.getLanguage())).findFirst();
////		if (language.isEmpty()) {
////			selectedLanguage = languages.stream().filter(x -> x.getAlphaCode().equals("en")).findFirst().get();
////		} else {
////			selectedLanguage = language.get();
////		}
////		
////		logger.info(pollRepository.count());
//		
//				
//		logger.debug("init called");
	}

//	public LanguageViewModel getSelectedLanguage() {
//		//logger.debug("getSelectedLanguage called" + selectedLanguage.getLocale().getISO3Language());
//		return selectedLanguage;
//	}
//
//	public void setSelectedLanguage(LanguageViewModel selectedLanguage) {
//		this.selectedLanguage = selectedLanguage;
//		onLanguageChange();
//	}
//	
//	public List<LanguageViewModel> getLanguages() {
//		return languages;
//	}
		
//	public void onLanguageChange() {
//		FacesContext.getCurrentInstance().getViewRoot().setLocale(selectedLanguage.getLocale());
//		Locale.setDefault(selectedLanguage.getLocale());
//		logger.info("language updated to: " + FacesContext.getCurrentInstance()
//		.getViewRoot().getLocale().getISO3Language());
//	}
	
	public Locale getCurrentLocale() {
		return Locale.getDefault();
	}
	
	public String getVersion() {
		return configurationHolder.getByCode(ConfigurationCode.PROJECT_VERSION);
	}
}
