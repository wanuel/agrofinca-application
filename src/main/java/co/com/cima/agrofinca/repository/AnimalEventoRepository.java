package co.com.cima.agrofinca.repository;

import co.com.cima.agrofinca.domain.AnimalEvento;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AnimalEvento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnimalEventoRepository extends JpaRepository<AnimalEvento, Long>, JpaSpecificationExecutor<AnimalEvento> {
}
