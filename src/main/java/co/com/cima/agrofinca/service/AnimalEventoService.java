package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.AnimalEvento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link AnimalEvento}.
 */
public interface AnimalEventoService {

    /**
     * Save a animalEvento.
     *
     * @param animalEvento the entity to save.
     * @return the persisted entity.
     */
    AnimalEvento save(AnimalEvento animalEvento);

    /**
     * Get all the animalEventos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnimalEvento> findAll(Pageable pageable);


    /**
     * Get the "id" animalEvento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnimalEvento> findOne(Long id);

    /**
     * Delete the "id" animalEvento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the animalEvento corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnimalEvento> search(String query, Pageable pageable);
}
