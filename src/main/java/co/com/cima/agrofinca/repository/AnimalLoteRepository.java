package co.com.cima.agrofinca.repository;

import co.com.cima.agrofinca.domain.AnimalLote;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AnimalLote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnimalLoteRepository extends JpaRepository<AnimalLote, Long>, JpaSpecificationExecutor<AnimalLote> {
}
