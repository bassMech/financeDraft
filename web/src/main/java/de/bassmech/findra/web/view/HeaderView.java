package de.bassmech.findra.web.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.model.statics.ConfigurationCode;
import de.bassmech.findra.web.service.ConfigurationHolder;
import jakarta.faces.view.ViewScoped;

@Component
@ViewScoped
public class HeaderView extends ViewBase {
//	@Inject
//	LanguageService languageService;
	
	@Autowired
	private ConfigurationHolder configurationHolder;
	
	
//	private List<LanguageViewModel> languages;
//
//	protected LanguageViewModel selectedLanguage;

//	@PostConstruct
//	public void init() {
//		
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
//	}

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
	
	public String getVersion() {
		return configurationHolder.getByCode(ConfigurationCode.PROJECT_VERSION);
	}
}
