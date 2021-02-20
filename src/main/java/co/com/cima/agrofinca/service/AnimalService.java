package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.Animal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Animal}.
 */
public interface AnimalService {

    /**
     * Save a animal.
     *
     * @param animal the entity to save.
     * @return the persisted entity.
     */
    Animal save(Animal animal);

    /**
     * Get all the animals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Animal> findAll(Pageable pageable);


    /**
     * Get the "id" animal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Animal> findOne(Long id);

    /**
     * Delete the "id" animal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the animal corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Animal> search(String query, Pageable pageable);
}
