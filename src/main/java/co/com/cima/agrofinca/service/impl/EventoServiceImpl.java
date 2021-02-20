package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.EventoService;
import co.com.cima.agrofinca.domain.Evento;
import co.com.cima.agrofinca.repository.EventoRepository;
import co.com.cima.agrofinca.repository.search.EventoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Evento}.
 */
@Service
@Transactional
public class EventoServiceImpl implements EventoService {

    private final Logger log = LoggerFactory.getLogger(EventoServiceImpl.class);

    private final EventoRepository eventoRepository;

    private final EventoSearchRepository eventoSearchRepository;

    public EventoServiceImpl(EventoRepository eventoRepository, EventoSearchRepository eventoSearchRepository) {
        this.eventoRepository = eventoRepository;
        this.eventoSearchRepository = eventoSearchRepository;
    }

    @Override
    public Evento save(Evento evento) {
        log.debug("Request to save Evento : {}", evento);
        Evento result = eventoRepository.save(evento);
        eventoSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Evento> findAll(Pageable pageable) {
        log.debug("Request to get all Eventos");
        return eventoRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Evento> findOne(Long id) {
        log.debug("Request to get Evento : {}", id);
        return eventoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Evento : {}", id);
        eventoRepository.deleteById(id);
        eventoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Evento> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Eventos for query {}", query);
        return eventoSearchRepository.search(queryStringQuery(query), pageable);    }
}
