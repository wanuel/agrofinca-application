package co.com.cima.agrofinca.repository;

import co.com.cima.agrofinca.domain.Finca;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Finca entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FincaRepository extends JpaRepository<Finca, Long>, JpaSpecificationExecutor<Finca> {
}
