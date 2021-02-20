package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.PersonaService;
import co.com.cima.agrofinca.domain.Persona;
import co.com.cima.agrofinca.repository.PersonaRepository;
import co.com.cima.agrofinca.repository.search.PersonaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Persona}.
 */
@Service
@Transactional
public class PersonaServiceImpl implements PersonaService {

    private final Logger log = LoggerFactory.getLogger(PersonaServiceImpl.class);

    private final PersonaRepository personaRepository;

    private final PersonaSearchRepository personaSearchRepository;

    public PersonaServiceImpl(PersonaRepository personaRepository, PersonaSearchRepository personaSearchRepository) {
        this.personaRepository = personaRepository;
        this.personaSearchRepository = personaSearchRepository;
    }

    @Override
    public Persona save(Persona persona) {
        log.debug("Request to save Persona : {}", persona);
        Persona result = personaRepository.save(persona);
        personaSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Persona> findAll(Pageable pageable) {
        log.debug("Request to get all Personas");
        return personaRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Persona> findOne(Long id) {
        log.debug("Request to get Persona : {}", id);
        return personaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Persona : {}", id);
        personaRepository.deleteById(id);
        personaSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Persona> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Personas for query {}", query);
        return personaSearchRepository.search(queryStringQuery(query), pageable);    }
}
