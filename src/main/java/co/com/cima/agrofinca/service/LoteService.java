package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.Lote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Lote}.
 */
public interface LoteService {

    /**
     * Save a lote.
     *
     * @param lote the entity to save.
     * @return the persisted entity.
     */
    Lote save(Lote lote);

    /**
     * Get all the lotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Lote> findAll(Pageable pageable);


    /**
     * Get the "id" lote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Lote> findOne(Long id);

    /**
     * Delete the "id" lote.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the lote corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Lote> search(String query, Pageable pageable);
}
