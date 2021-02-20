package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.AnimalEventoService;
import co.com.cima.agrofinca.domain.AnimalEvento;
import co.com.cima.agrofinca.repository.AnimalEventoRepository;
import co.com.cima.agrofinca.repository.search.AnimalEventoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link AnimalEvento}.
 */
@Service
@Transactional
public class AnimalEventoServiceImpl implements AnimalEventoService {

    private final Logger log = LoggerFactory.getLogger(AnimalEventoServiceImpl.class);

    private final AnimalEventoRepository animalEventoRepository;

    private final AnimalEventoSearchRepository animalEventoSearchRepository;

    public AnimalEventoServiceImpl(AnimalEventoRepository animalEventoRepository, AnimalEventoSearchRepository animalEventoSearchRepository) {
        this.animalEventoRepository = animalEventoRepository;
        this.animalEventoSearchRepository = animalEventoSearchRepository;
    }

    @Override
    public AnimalEvento save(AnimalEvento animalEvento) {
        log.debug("Request to save AnimalEvento : {}", animalEvento);
        AnimalEvento result = animalEventoRepository.save(animalEvento);
        animalEventoSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalEvento> findAll(Pageable pageable) {
        log.debug("Request to get all AnimalEventos");
        return animalEventoRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AnimalEvento> findOne(Long id) {
        log.debug("Request to get AnimalEvento : {}", id);
        return animalEventoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnimalEvento : {}", id);
        animalEventoRepository.deleteById(id);
        animalEventoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalEvento> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnimalEventos for query {}", query);
        return animalEventoSearchRepository.search(queryStringQuery(query), pageable);    }
}
