package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.ParametrosService;
import co.com.cima.agrofinca.domain.Parametros;
import co.com.cima.agrofinca.repository.ParametrosRepository;
import co.com.cima.agrofinca.repository.search.ParametrosSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Parametros}.
 */
@Service
@Transactional
public class ParametrosServiceImpl implements ParametrosService {

    private final Logger log = LoggerFactory.getLogger(ParametrosServiceImpl.class);

    private final ParametrosRepository parametrosRepository;

    private final ParametrosSearchRepository parametrosSearchRepository;

    public ParametrosServiceImpl(ParametrosRepository parametrosRepository, ParametrosSearchRepository parametrosSearchRepository) {
        this.parametrosRepository = parametrosRepository;
        this.parametrosSearchRepository = parametrosSearchRepository;
    }

    @Override
    public Parametros save(Parametros parametros) {
        log.debug("Request to save Parametros : {}", parametros);
        Parametros result = parametrosRepository.save(parametros);
        parametrosSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Parametros> findAll(Pageable pageable) {
        log.debug("Request to get all Parametros");
        return parametrosRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Parametros> findOne(Long id) {
        log.debug("Request to get Parametros : {}", id);
        return parametrosRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Parametros : {}", id);
        parametrosRepository.deleteById(id);
        parametrosSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Parametros> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Parametros for query {}", query);
        return parametrosSearchRepository.search(queryStringQuery(query), pageable);    }
}
