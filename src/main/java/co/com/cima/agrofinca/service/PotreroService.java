package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.Potrero;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Potrero}.
 */
public interface PotreroService {

    /**
     * Save a potrero.
     *
     * @param potrero the entity to save.
     * @return the persisted entity.
     */
    Potrero save(Potrero potrero);

    /**
     * Get all the potreros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Potrero> findAll(Pageable pageable);


    /**
     * Get the "id" potrero.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Potrero> findOne(Long id);

    /**
     * Delete the "id" potrero.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the potrero corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Potrero> search(String query, Pageable pageable);
}
