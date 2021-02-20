package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.Finca;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Finca}.
 */
public interface FincaService {

    /**
     * Save a finca.
     *
     * @param finca the entity to save.
     * @return the persisted entity.
     */
    Finca save(Finca finca);

    /**
     * Get all the fincas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Finca> findAll(Pageable pageable);


    /**
     * Get the "id" finca.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Finca> findOne(Long id);

    /**
     * Delete the "id" finca.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the finca corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Finca> search(String query, Pageable pageable);
}
