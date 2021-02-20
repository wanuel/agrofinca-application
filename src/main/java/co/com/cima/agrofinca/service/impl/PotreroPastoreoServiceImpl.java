package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.PotreroPastoreoService;
import co.com.cima.agrofinca.domain.PotreroPastoreo;
import co.com.cima.agrofinca.repository.PotreroPastoreoRepository;
import co.com.cima.agrofinca.repository.search.PotreroPastoreoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link PotreroPastoreo}.
 */
@Service
@Transactional
public class PotreroPastoreoServiceImpl implements PotreroPastoreoService {

    private final Logger log = LoggerFactory.getLogger(PotreroPastoreoServiceImpl.class);

    private final PotreroPastoreoRepository potreroPastoreoRepository;

    private final PotreroPastoreoSearchRepository potreroPastoreoSearchRepository;

    public PotreroPastoreoServiceImpl(PotreroPastoreoRepository potreroPastoreoRepository, PotreroPastoreoSearchRepository potreroPastoreoSearchRepository) {
        this.potreroPastoreoRepository = potreroPastoreoRepository;
        this.potreroPastoreoSearchRepository = potreroPastoreoSearchRepository;
    }

    @Override
    public PotreroPastoreo save(PotreroPastoreo potreroPastoreo) {
        log.debug("Request to save PotreroPastoreo : {}", potreroPastoreo);
        PotreroPastoreo result = potreroPastoreoRepository.save(potreroPastoreo);
        potreroPastoreoSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PotreroPastoreo> findAll(Pageable pageable) {
        log.debug("Request to get all PotreroPastoreos");
        return potreroPastoreoRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PotreroPastoreo> findOne(Long id) {
        log.debug("Request to get PotreroPastoreo : {}", id);
        return potreroPastoreoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PotreroPastoreo : {}", id);
        potreroPastoreoRepository.deleteById(id);
        potreroPastoreoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PotreroPastoreo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PotreroPastoreos for query {}", query);
        return potreroPastoreoSearchRepository.search(queryStringQuery(query), pageable);    }
}
