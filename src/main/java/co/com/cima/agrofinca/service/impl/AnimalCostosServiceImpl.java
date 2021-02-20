package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.AnimalCostosService;
import co.com.cima.agrofinca.domain.AnimalCostos;
import co.com.cima.agrofinca.repository.AnimalCostosRepository;
import co.com.cima.agrofinca.repository.search.AnimalCostosSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link AnimalCostos}.
 */
@Service
@Transactional
public class AnimalCostosServiceImpl implements AnimalCostosService {

    private final Logger log = LoggerFactory.getLogger(AnimalCostosServiceImpl.class);

    private final AnimalCostosRepository animalCostosRepository;

    private final AnimalCostosSearchRepository animalCostosSearchRepository;

    public AnimalCostosServiceImpl(AnimalCostosRepository animalCostosRepository, AnimalCostosSearchRepository animalCostosSearchRepository) {
        this.animalCostosRepository = animalCostosRepository;
        this.animalCostosSearchRepository = animalCostosSearchRepository;
    }

    @Override
    public AnimalCostos save(AnimalCostos animalCostos) {
        log.debug("Request to save AnimalCostos : {}", animalCostos);
        AnimalCostos result = animalCostosRepository.save(animalCostos);
        animalCostosSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalCostos> findAll(Pageable pageable) {
        log.debug("Request to get all AnimalCostos");
        return animalCostosRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AnimalCostos> findOne(Long id) {
        log.debug("Request to get AnimalCostos : {}", id);
        return animalCostosRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnimalCostos : {}", id);
        animalCostosRepository.deleteById(id);
        animalCostosSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalCostos> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnimalCostos for query {}", query);
        return animalCostosSearchRepository.search(queryStringQuery(query), pageable);    }
}
