package co.com.cima.agrofinca.repository;

import co.com.cima.agrofinca.domain.AnimalSalud;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AnimalSalud entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnimalSaludRepository extends JpaRepository<AnimalSalud, Long> {
}
