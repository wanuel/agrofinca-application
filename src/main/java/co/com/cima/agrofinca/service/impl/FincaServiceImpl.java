package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.FincaService;
import co.com.cima.agrofinca.domain.Finca;
import co.com.cima.agrofinca.repository.FincaRepository;
import co.com.cima.agrofinca.repository.search.FincaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Finca}.
 */
@Service
@Transactional
public class FincaServiceImpl implements FincaService {

    private final Logger log = LoggerFactory.getLogger(FincaServiceImpl.class);

    private final FincaRepository fincaRepository;

    private final FincaSearchRepository fincaSearchRepository;

    public FincaServiceImpl(FincaRepository fincaRepository, FincaSearchRepository fincaSearchRepository) {
        this.fincaRepository = fincaRepository;
        this.fincaSearchRepository = fincaSearchRepository;
    }

    @Override
    public Finca save(Finca finca) {
        log.debug("Request to save Finca : {}", finca);
        Finca result = fincaRepository.save(finca);
        fincaSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Finca> findAll(Pageable pageable) {
        log.debug("Request to get all Fincas");
        return fincaRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Finca> findOne(Long id) {
        log.debug("Request to get Finca : {}", id);
        return fincaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Finca : {}", id);
        fincaRepository.deleteById(id);
        fincaSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Finca> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Fincas for query {}", query);
        return fincaSearchRepository.search(queryStringQuery(query), pageable);    }
}
