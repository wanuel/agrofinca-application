package co.com.cima.agrofinca.service.impl;

import co.com.cima.agrofinca.service.AnimalImagenService;
import co.com.cima.agrofinca.domain.AnimalImagen;
import co.com.cima.agrofinca.repository.AnimalImagenRepository;
import co.com.cima.agrofinca.repository.search.AnimalImagenSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link AnimalImagen}.
 */
@Service
@Transactional
public class AnimalImagenServiceImpl implements AnimalImagenService {

    private final Logger log = LoggerFactory.getLogger(AnimalImagenServiceImpl.class);

    private final AnimalImagenRepository animalImagenRepository;

    private final AnimalImagenSearchRepository animalImagenSearchRepository;

    public AnimalImagenServiceImpl(AnimalImagenRepository animalImagenRepository, AnimalImagenSearchRepository animalImagenSearchRepository) {
        this.animalImagenRepository = animalImagenRepository;
        this.animalImagenSearchRepository = animalImagenSearchRepository;
    }

    @Override
    public AnimalImagen save(AnimalImagen animalImagen) {
        log.debug("Request to save AnimalImagen : {}", animalImagen);
        AnimalImagen result = animalImagenRepository.save(animalImagen);
        animalImagenSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalImagen> findAll(Pageable pageable) {
        log.debug("Request to get all AnimalImagens");
        return animalImagenRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AnimalImagen> findOne(Long id) {
        log.debug("Request to get AnimalImagen : {}", id);
        return animalImagenRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnimalImagen : {}", id);
        animalImagenRepository.deleteById(id);
        animalImagenSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalImagen> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnimalImagens for query {}", query);
        return animalImagenSearchRepository.search(queryStringQuery(query), pageable);    }
}
