package co.com.cima.agrofinca.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import co.com.cima.agrofinca.domain.AnimalImagen;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.AnimalImagenRepository;
import co.com.cima.agrofinca.repository.search.AnimalImagenSearchRepository;
import co.com.cima.agrofinca.service.dto.AnimalImagenCriteria;

/**
 * Service for executing complex queries for {@link AnimalImagen} entities in the database.
 * The main input is a {@link AnimalImagenCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnimalImagen} or a {@link Page} of {@link AnimalImagen} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnimalImagenQueryService extends QueryService<AnimalImagen> {

    private final Logger log = LoggerFactory.getLogger(AnimalImagenQueryService.class);

    private final AnimalImagenRepository animalImagenRepository;

    private final AnimalImagenSearchRepository animalImagenSearchRepository;

    public AnimalImagenQueryService(AnimalImagenRepository animalImagenRepository, AnimalImagenSearchRepository animalImagenSearchRepository) {
        this.animalImagenRepository = animalImagenRepository;
        this.animalImagenSearchRepository = animalImagenSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AnimalImagen} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnimalImagen> findByCriteria(AnimalImagenCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AnimalImagen> specification = createSpecification(criteria);
        return animalImagenRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AnimalImagen} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnimalImagen> findByCriteria(AnimalImagenCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnimalImagen> specification = createSpecification(criteria);
        return animalImagenRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnimalImagenCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AnimalImagen> specification = createSpecification(criteria);
        return animalImagenRepository.count(specification);
    }

    /**
     * Function to convert {@link AnimalImagenCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AnimalImagen> createSpecification(AnimalImagenCriteria criteria) {
        Specification<AnimalImagen> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AnimalImagen_.id));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), AnimalImagen_.fecha));
            }
            if (criteria.getNota() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNota(), AnimalImagen_.nota));
            }
            if (criteria.getAnimalId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnimalId(),
                    root -> root.join(AnimalImagen_.animal, JoinType.LEFT).get(Animal_.id)));
            }
        }
        return specification;
    }
}
