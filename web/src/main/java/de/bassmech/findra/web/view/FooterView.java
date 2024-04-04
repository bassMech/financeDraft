package de.bassmech.findra.web.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import de.bassmech.findra.model.statics.ConfigurationCode;
import de.bassmech.findra.web.service.ConfigurationService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;

@Component
@SessionScoped
public class FooterView {
	private Logger logger = LoggerFactory.getLogger(FooterView.class);
	
	private String version;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@PostConstruct
	@DependsOn(value = { "ConfigurationService"})
	public void init() {
		logger.debug("init called");
	}
	
	public String getVersion() {
		if (version == null || version.isBlank()) {
			version = configurationService.getByCode(ConfigurationCode.PROJECT_REVISION);
		}
		return version;
	}
}
