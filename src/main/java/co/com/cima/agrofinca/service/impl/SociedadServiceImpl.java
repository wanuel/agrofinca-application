package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.SociedadService;
import co.com.cima.agrofinca.domain.Sociedad;
import co.com.cima.agrofinca.repository.SociedadRepository;
import co.com.cima.agrofinca.repository.search.SociedadSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Sociedad}.
 */
@Service
@Transactional
public class SociedadServiceImpl implements SociedadService {

    private final Logger log = LoggerFactory.getLogger(SociedadServiceImpl.class);

    private final SociedadRepository sociedadRepository;

    private final SociedadSearchRepository sociedadSearchRepository;

    public SociedadServiceImpl(SociedadRepository sociedadRepository, SociedadSearchRepository sociedadSearchRepository) {
        this.sociedadRepository = sociedadRepository;
        this.sociedadSearchRepository = sociedadSearchRepository;
    }

    @Override
    public Sociedad save(Sociedad sociedad) {
        log.debug("Request to save Sociedad : {}", sociedad);
        Sociedad result = sociedadRepository.save(sociedad);
        sociedadSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sociedad> findAll(Pageable pageable) {
        log.debug("Request to get all Sociedads");
        return sociedadRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Sociedad> findOne(Long id) {
        log.debug("Request to get Sociedad : {}", id);
        return sociedadRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sociedad : {}", id);
        sociedadRepository.deleteById(id);
        sociedadSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sociedad> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Sociedads for query {}", query);
        return sociedadSearchRepository.search(queryStringQuery(query), pageable);    }
}
