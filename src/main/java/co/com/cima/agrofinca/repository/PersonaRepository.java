package co.com.cima.agrofinca.repository;

import co.com.cima.agrofinca.domain.Persona;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Persona entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long>, JpaSpecificationExecutor<Persona> {
}
