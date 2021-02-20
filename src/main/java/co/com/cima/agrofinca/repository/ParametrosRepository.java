package co.com.cima.agrofinca.repository;

import co.com.cima.agrofinca.domain.Parametros;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Parametros entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParametrosRepository extends JpaRepository<Parametros, Long>, JpaSpecificationExecutor<Parametros> {
}
