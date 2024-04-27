package de.bassmech.findra.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.bassmech.findra.model.entity.AccountItemGroup;

@Repository
public interface AccountItemGroupRepository extends JpaRepository<AccountItemGroup, Integer> {
	
	@Query("SELECT group FROM AccountItemGroup group WHERE group.account.id = :accountId")
	List<AccountItemGroup> findByAccountId(@Param("accountId") int accountId);
	
}
