package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.Parametros;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Parametros}.
 */
public interface ParametrosService {

    /**
     * Save a parametros.
     *
     * @param parametros the entity to save.
     * @return the persisted entity.
     */
    Parametros save(Parametros parametros);

    /**
     * Get all the parametros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Parametros> findAll(Pageable pageable);


    /**
     * Get the "id" parametros.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Parametros> findOne(Long id);

    /**
     * Delete the "id" parametros.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the parametros corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Parametros> search(String query, Pageable pageable);
}
