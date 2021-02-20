package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.AnimalLote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link AnimalLote}.
 */
public interface AnimalLoteService {

    /**
     * Save a animalLote.
     *
     * @param animalLote the entity to save.
     * @return the persisted entity.
     */
    AnimalLote save(AnimalLote animalLote);

    /**
     * Get all the animalLotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnimalLote> findAll(Pageable pageable);


    /**
     * Get the "id" animalLote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnimalLote> findOne(Long id);

    /**
     * Delete the "id" animalLote.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the animalLote corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnimalLote> search(String query, Pageable pageable);
}
