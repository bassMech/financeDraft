package de.bassmech.findra.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.core.repository.ConfigurationRepository;
import de.bassmech.findra.model.entity.Configuration;
import de.bassmech.findra.model.statics.ConfigurationCode;
import jakarta.annotation.PostConstruct;

@Component
public class ConfigurationService {
	private Map<ConfigurationCode, String> configurationMap;
	
	private Logger logger = LoggerFactory.getLogger(ConfigurationService.class);
	
	@Autowired
	private ConfigurationRepository configurationRepository;
	
	@PostConstruct
	public void init() {
		logger.info("init called");
		configurationMap = new HashMap<>();
		
		List<Configuration> dbConfigs = configurationRepository.findAll();
		for (Configuration config : dbConfigs) {
			configurationMap.put(config.getCode(), config.getEntry());
		}
	}
	
	public String getByCode(ConfigurationCode code) {
		return configurationMap.get(code);
	}
}
