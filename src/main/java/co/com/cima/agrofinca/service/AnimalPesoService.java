package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.AnimalPeso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link AnimalPeso}.
 */
public interface AnimalPesoService {

    /**
     * Save a animalPeso.
     *
     * @param animalPeso the entity to save.
     * @return the persisted entity.
     */
    AnimalPeso save(AnimalPeso animalPeso);

    /**
     * Get all the animalPesos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnimalPeso> findAll(Pageable pageable);


    /**
     * Get the "id" animalPeso.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnimalPeso> findOne(Long id);

    /**
     * Delete the "id" animalPeso.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the animalPeso corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnimalPeso> search(String query, Pageable pageable);
}
