package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.AnimalSaludService;
import co.com.cima.agrofinca.domain.AnimalSalud;
import co.com.cima.agrofinca.repository.AnimalSaludRepository;
import co.com.cima.agrofinca.repository.search.AnimalSaludSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link AnimalSalud}.
 */
@Service
@Transactional
public class AnimalSaludServiceImpl implements AnimalSaludService {

    private final Logger log = LoggerFactory.getLogger(AnimalSaludServiceImpl.class);

    private final AnimalSaludRepository animalSaludRepository;

    private final AnimalSaludSearchRepository animalSaludSearchRepository;

    public AnimalSaludServiceImpl(AnimalSaludRepository animalSaludRepository, AnimalSaludSearchRepository animalSaludSearchRepository) {
        this.animalSaludRepository = animalSaludRepository;
        this.animalSaludSearchRepository = animalSaludSearchRepository;
    }

    @Override
    public AnimalSalud save(AnimalSalud animalSalud) {
        log.debug("Request to save AnimalSalud : {}", animalSalud);
        AnimalSalud result = animalSaludRepository.save(animalSalud);
        animalSaludSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalSalud> findAll(Pageable pageable) {
        log.debug("Request to get all AnimalSaluds");
        return animalSaludRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AnimalSalud> findOne(Long id) {
        log.debug("Request to get AnimalSalud : {}", id);
        return animalSaludRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnimalSalud : {}", id);
        animalSaludRepository.deleteById(id);
        animalSaludSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalSalud> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnimalSaluds for query {}", query);
        return animalSaludSearchRepository.search(queryStringQuery(query), pageable);    }
}
