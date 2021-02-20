package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.AnimalImagen;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link AnimalImagen}.
 */
public interface AnimalImagenService {

    /**
     * Save a animalImagen.
     *
     * @param animalImagen the entity to save.
     * @return the persisted entity.
     */
    AnimalImagen save(AnimalImagen animalImagen);

    /**
     * Get all the animalImagens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnimalImagen> findAll(Pageable pageable);


    /**
     * Get the "id" animalImagen.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnimalImagen> findOne(Long id);

    /**
     * Delete the "id" animalImagen.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the animalImagen corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnimalImagen> search(String query, Pageable pageable);
}
