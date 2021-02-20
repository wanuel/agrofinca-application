package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.PotreroService;
import co.com.cima.agrofinca.domain.Potrero;
import co.com.cima.agrofinca.repository.PotreroRepository;
import co.com.cima.agrofinca.repository.search.PotreroSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Potrero}.
 */
@Service
@Transactional
public class PotreroServiceImpl implements PotreroService {

    private final Logger log = LoggerFactory.getLogger(PotreroServiceImpl.class);

    private final PotreroRepository potreroRepository;

    private final PotreroSearchRepository potreroSearchRepository;

    public PotreroServiceImpl(PotreroRepository potreroRepository, PotreroSearchRepository potreroSearchRepository) {
        this.potreroRepository = potreroRepository;
        this.potreroSearchRepository = potreroSearchRepository;
    }

    @Override
    public Potrero save(Potrero potrero) {
        log.debug("Request to save Potrero : {}", potrero);
        Potrero result = potreroRepository.save(potrero);
        potreroSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Potrero> findAll(Pageable pageable) {
        log.debug("Request to get all Potreros");
        return potreroRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Potrero> findOne(Long id) {
        log.debug("Request to get Potrero : {}", id);
        return potreroRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Potrero : {}", id);
        potreroRepository.deleteById(id);
        potreroSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Potrero> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Potreros for query {}", query);
        return potreroSearchRepository.search(queryStringQuery(query), pageable);    }
}
