package de.bassmech.findra.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.bassmech.findra.model.entity.Setting;
import de.bassmech.findra.model.statics.SettingCode;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Integer> {
	
	public Setting findByCode(SettingCode code);
		
}
