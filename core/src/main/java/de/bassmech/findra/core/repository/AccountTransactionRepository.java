package de.bassmech.findra.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.bassmech.findra.model.entity.AccountTransaction;
import de.bassmech.findra.model.entity.AccountTransactionDraft;

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {

}
