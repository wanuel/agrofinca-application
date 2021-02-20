package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.AnimalLoteService;
import co.com.cima.agrofinca.domain.AnimalLote;
import co.com.cima.agrofinca.repository.AnimalLoteRepository;
import co.com.cima.agrofinca.repository.search.AnimalLoteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link AnimalLote}.
 */
@Service
@Transactional
public class AnimalLoteServiceImpl implements AnimalLoteService {

    private final Logger log = LoggerFactory.getLogger(AnimalLoteServiceImpl.class);

    private final AnimalLoteRepository animalLoteRepository;

    private final AnimalLoteSearchRepository animalLoteSearchRepository;

    public AnimalLoteServiceImpl(AnimalLoteRepository animalLoteRepository, AnimalLoteSearchRepository animalLoteSearchRepository) {
        this.animalLoteRepository = animalLoteRepository;
        this.animalLoteSearchRepository = animalLoteSearchRepository;
    }

    @Override
    public AnimalLote save(AnimalLote animalLote) {
        log.debug("Request to save AnimalLote : {}", animalLote);
        AnimalLote result = animalLoteRepository.save(animalLote);
        animalLoteSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalLote> findAll(Pageable pageable) {
        log.debug("Request to get all AnimalLotes");
        return animalLoteRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AnimalLote> findOne(Long id) {
        log.debug("Request to get AnimalLote : {}", id);
        return animalLoteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnimalLote : {}", id);
        animalLoteRepository.deleteById(id);
        animalLoteSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalLote> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnimalLotes for query {}", query);
        return animalLoteSearchRepository.search(queryStringQuery(query), pageable);    }
}
