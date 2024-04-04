package de.bassmech.findra.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.bassmech.findra.model.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
	@Query("SELECT client FROM Client client WHERE client.name = :name AND client.passwordHash = :password")
	public Client findByNameAndPassword(@Param("name")String name, @Param("password") String password);
	
	@Query("SELECT client FROM Client client WHERE client.name = :name")
	public Client findByName(@Param("name") String name);
	
	@Query("SELECT client FROM Client client WHERE client.uuid = :uuid")
	public Client findByUuid(@Param("uuid")String uuid);
}
