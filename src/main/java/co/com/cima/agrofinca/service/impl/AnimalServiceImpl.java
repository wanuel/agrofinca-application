package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.AnimalService;
import co.com.cima.agrofinca.domain.Animal;
import co.com.cima.agrofinca.repository.AnimalRepository;
import co.com.cima.agrofinca.repository.search.AnimalSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Animal}.
 */
@Service
@Transactional
public class AnimalServiceImpl implements AnimalService {

    private final Logger log = LoggerFactory.getLogger(AnimalServiceImpl.class);

    private final AnimalRepository animalRepository;

    private final AnimalSearchRepository animalSearchRepository;

    public AnimalServiceImpl(AnimalRepository animalRepository, AnimalSearchRepository animalSearchRepository) {
        this.animalRepository = animalRepository;
        this.animalSearchRepository = animalSearchRepository;
    }

    @Override
    public Animal save(Animal animal) {
        log.debug("Request to save Animal : {}", animal);
        Animal result = animalRepository.save(animal);
        animalSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Animal> findAll(Pageable pageable) {
        log.debug("Request to get all Animals");
        return animalRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Animal> findOne(Long id) {
        log.debug("Request to get Animal : {}", id);
        return animalRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Animal : {}", id);
        animalRepository.deleteById(id);
        animalSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Animal> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Animals for query {}", query);
        return animalSearchRepository.search(queryStringQuery(query), pageable);    }
}
