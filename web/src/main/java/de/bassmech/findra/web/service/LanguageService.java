package de.bassmech.findra.web.service;

import org.springframework.stereotype.Service;

import jakarta.enterprise.context.ApplicationScoped;

@Service
public class LanguageService {
//	private List<Language> languages;
//	private List<LanguageViewModel> languageViewModels;
//	
//	@Inject
//	private LanguageRepository languageRepository;
//	
//	@PostConstruct
//	public void init() {
//		languages = languageRepository.listAll();
//		
//		languageViewModels = new ArrayList<>();
//		for (Language language : languages) {
//			languageViewModels.add(new LanguageViewModel(language, FrontendLanguageType.hasCode(language.getAlpha2())));
//		}
//	}
//
//	public Language getLanguageByLocale(Locale locale) {
//		String isoCode = locale.getLanguage();
//		return languages.stream().filter(x -> x.getAlpha2().equals(isoCode)).findFirst().orElse(null);
//	}
//
//	public LanguageViewModel getLanguageByCode(String code) {
//		return languageViewModels.stream().filter(x -> x.getAlphaCode().equals(code)).findFirst().orElse(null);
//	}
//
//	public List<LanguageViewModel> getFrontendLanguages() {
//		return languageViewModels.stream().filter(lvm -> lvm.isFrontendImplented()).collect(Collectors.toList());
//	}
//	
}
