package de.bassmech.findra.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.bassmech.findra.model.entity.Allocation;
import de.bassmech.findra.model.entity.Configuration;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {

}
