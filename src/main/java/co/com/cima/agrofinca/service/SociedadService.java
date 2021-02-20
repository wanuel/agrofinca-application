package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.Sociedad;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Sociedad}.
 */
public interface SociedadService {

    /**
     * Save a sociedad.
     *
     * @param sociedad the entity to save.
     * @return the persisted entity.
     */
    Sociedad save(Sociedad sociedad);

    /**
     * Get all the sociedads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sociedad> findAll(Pageable pageable);


    /**
     * Get the "id" sociedad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sociedad> findOne(Long id);

    /**
     * Delete the "id" sociedad.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the sociedad corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sociedad> search(String query, Pageable pageable);
}
