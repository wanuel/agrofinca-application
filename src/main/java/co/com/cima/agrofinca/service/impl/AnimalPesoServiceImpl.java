package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.AnimalPesoService;
import co.com.cima.agrofinca.domain.AnimalPeso;
import co.com.cima.agrofinca.repository.AnimalPesoRepository;
import co.com.cima.agrofinca.repository.search.AnimalPesoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link AnimalPeso}.
 */
@Service
@Transactional
public class AnimalPesoServiceImpl implements AnimalPesoService {

    private final Logger log = LoggerFactory.getLogger(AnimalPesoServiceImpl.class);

    private final AnimalPesoRepository animalPesoRepository;

    private final AnimalPesoSearchRepository animalPesoSearchRepository;

    public AnimalPesoServiceImpl(AnimalPesoRepository animalPesoRepository, AnimalPesoSearchRepository animalPesoSearchRepository) {
        this.animalPesoRepository = animalPesoRepository;
        this.animalPesoSearchRepository = animalPesoSearchRepository;
    }

    @Override
    public AnimalPeso save(AnimalPeso animalPeso) {
        log.debug("Request to save AnimalPeso : {}", animalPeso);
        AnimalPeso result = animalPesoRepository.save(animalPeso);
        animalPesoSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalPeso> findAll(Pageable pageable) {
        log.debug("Request to get all AnimalPesos");
        return animalPesoRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AnimalPeso> findOne(Long id) {
        log.debug("Request to get AnimalPeso : {}", id);
        return animalPesoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnimalPeso : {}", id);
        animalPesoRepository.deleteById(id);
        animalPesoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalPeso> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnimalPesos for query {}", query);
        return animalPesoSearchRepository.search(queryStringQuery(query), pageable);    }
}
