package co.com.cima.agrofinca.repository;

import co.com.cima.agrofinca.domain.Sociedad;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Sociedad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SociedadRepository extends JpaRepository<Sociedad, Long>, JpaSpecificationExecutor<Sociedad> {
}
