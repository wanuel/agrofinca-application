package co.com.cima.agrofinca.repository;

import co.com.cima.agrofinca.domain.Potrero;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Potrero entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PotreroRepository extends JpaRepository<Potrero, Long>, JpaSpecificationExecutor<Potrero> {
}
