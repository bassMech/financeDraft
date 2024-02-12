package de.bassmech.findra.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.bassmech.findra.model.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	
	public List<Account> findAllByDeletedAtIsNull();

}
