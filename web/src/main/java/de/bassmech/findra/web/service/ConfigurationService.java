package de.bassmech.findra.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bassmech.findra.core.repository.ConfigurationRepository;
import de.bassmech.findra.model.entity.Configuration;
import de.bassmech.findra.model.statics.ConfigurationCode;
import jakarta.annotation.PostConstruct;

@Service
public class ConfigurationService {
	private Map<ConfigurationCode, String> configurationMap;
	
	@Autowired
	private ConfigurationRepository configurationRepository;
	
	@PostConstruct
	public void init() {
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
