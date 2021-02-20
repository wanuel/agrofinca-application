package co.com.cima.agrofinca.repository;

import co.com.cima.agrofinca.domain.PotreroPastoreo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PotreroPastoreo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PotreroPastoreoRepository extends JpaRepository<PotreroPastoreo, Long> {
}
