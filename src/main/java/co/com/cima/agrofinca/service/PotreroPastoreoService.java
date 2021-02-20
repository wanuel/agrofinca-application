package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.PotreroPastoreo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link PotreroPastoreo}.
 */
public interface PotreroPastoreoService {

    /**
     * Save a potreroPastoreo.
     *
     * @param potreroPastoreo the entity to save.
     * @return the persisted entity.
     */
    PotreroPastoreo save(PotreroPastoreo potreroPastoreo);

    /**
     * Get all the potreroPastoreos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PotreroPastoreo> findAll(Pageable pageable);


    /**
     * Get the "id" potreroPastoreo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PotreroPastoreo> findOne(Long id);

    /**
     * Delete the "id" potreroPastoreo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the potreroPastoreo corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PotreroPastoreo> search(String query, Pageable pageable);
}
