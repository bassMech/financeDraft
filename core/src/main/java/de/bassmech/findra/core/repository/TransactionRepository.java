package de.bassmech.findra.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.bassmech.findra.model.entity.AccountTransaction;

@Repository
public interface TransactionRepository extends JpaRepository<AccountTransaction, Long> {

}
