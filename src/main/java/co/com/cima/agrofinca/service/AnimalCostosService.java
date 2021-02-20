package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.AnimalCostos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link AnimalCostos}.
 */
public interface AnimalCostosService {

    /**
     * Save a animalCostos.
     *
     * @param animalCostos the entity to save.
     * @return the persisted entity.
     */
    AnimalCostos save(AnimalCostos animalCostos);

    /**
     * Get all the animalCostos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnimalCostos> findAll(Pageable pageable);


    /**
     * Get the "id" animalCostos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnimalCostos> findOne(Long id);

    /**
     * Delete the "id" animalCostos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the animalCostos corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnimalCostos> search(String query, Pageable pageable);
}
