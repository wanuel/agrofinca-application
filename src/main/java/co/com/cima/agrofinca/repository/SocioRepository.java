package co.com.cima.agrofinca.repository;

import co.com.cima.agrofinca.domain.Socio;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Socio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocioRepository extends JpaRepository<Socio, Long>, JpaSpecificationExecutor<Socio> {
}
