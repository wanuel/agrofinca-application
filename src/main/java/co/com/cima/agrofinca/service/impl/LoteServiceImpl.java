package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.LoteService;
import co.com.cima.agrofinca.domain.Lote;
import co.com.cima.agrofinca.repository.LoteRepository;
import co.com.cima.agrofinca.repository.search.LoteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Lote}.
 */
@Service
@Transactional
public class LoteServiceImpl implements LoteService {

    private final Logger log = LoggerFactory.getLogger(LoteServiceImpl.class);

    private final LoteRepository loteRepository;

    private final LoteSearchRepository loteSearchRepository;

    public LoteServiceImpl(LoteRepository loteRepository, LoteSearchRepository loteSearchRepository) {
        this.loteRepository = loteRepository;
        this.loteSearchRepository = loteSearchRepository;
    }

    @Override
    public Lote save(Lote lote) {
        log.debug("Request to save Lote : {}", lote);
        Lote result = loteRepository.save(lote);
        loteSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Lote> findAll(Pageable pageable) {
        log.debug("Request to get all Lotes");
        return loteRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Lote> findOne(Long id) {
        log.debug("Request to get Lote : {}", id);
        return loteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lote : {}", id);
        loteRepository.deleteById(id);
        loteSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Lote> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Lotes for query {}", query);
        return loteSearchRepository.search(queryStringQuery(query), pageable);    }
}
