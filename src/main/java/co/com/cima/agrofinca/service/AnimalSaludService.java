package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.AnimalSalud;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link AnimalSalud}.
 */
public interface AnimalSaludService {

    /**
     * Save a animalSalud.
     *
     * @param animalSalud the entity to save.
     * @return the persisted entity.
     */
    AnimalSalud save(AnimalSalud animalSalud);

    /**
     * Get all the animalSaluds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnimalSalud> findAll(Pageable pageable);


    /**
     * Get the "id" animalSalud.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnimalSalud> findOne(Long id);

    /**
     * Delete the "id" animalSalud.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the animalSalud corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnimalSalud> search(String query, Pageable pageable);
}
