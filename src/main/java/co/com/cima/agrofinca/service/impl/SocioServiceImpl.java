package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.SocioService;
import co.com.cima.agrofinca.domain.Socio;
import co.com.cima.agrofinca.repository.SocioRepository;
import co.com.cima.agrofinca.repository.search.SocioSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Socio}.
 */
@Service
@Transactional
public class SocioServiceImpl implements SocioService {

    private final Logger log = LoggerFactory.getLogger(SocioServiceImpl.class);

    private final SocioRepository socioRepository;

    private final SocioSearchRepository socioSearchRepository;

    public SocioServiceImpl(SocioRepository socioRepository, SocioSearchRepository socioSearchRepository) {
        this.socioRepository = socioRepository;
        this.socioSearchRepository = socioSearchRepository;
    }

    @Override
    public Socio save(Socio socio) {
        log.debug("Request to save Socio : {}", socio);
        Socio result = socioRepository.save(socio);
        socioSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Socio> findAll(Pageable pageable) {
        log.debug("Request to get all Socios");
        return socioRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Socio> findOne(Long id) {
        log.debug("Request to get Socio : {}", id);
        return socioRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Socio : {}", id);
        socioRepository.deleteById(id);
        socioSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Socio> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Socios for query {}", query);
        return socioSearchRepository.search(queryStringQuery(query), pageable);    }
}
