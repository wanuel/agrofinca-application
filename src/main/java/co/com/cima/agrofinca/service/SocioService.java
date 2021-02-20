package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.Socio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Socio}.
 */
public interface SocioService {

    /**
     * Save a socio.
     *
     * @param socio the entity to save.
     * @return the persisted entity.
     */
    Socio save(Socio socio);

    /**
     * Get all the socios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Socio> findAll(Pageable pageable);


    /**
     * Get the "id" socio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Socio> findOne(Long id);

    /**
     * Delete the "id" socio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the socio corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Socio> search(String query, Pageable pageable);
}
