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

import co.com.cima.agrofinca.domain.AnimalPeso;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.AnimalPesoRepository;
import co.com.cima.agrofinca.repository.search.AnimalPesoSearchRepository;
import co.com.cima.agrofinca.service.dto.AnimalPesoCriteria;

/**
 * Service for executing complex queries for {@link AnimalPeso} entities in the database.
 * The main input is a {@link AnimalPesoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnimalPeso} or a {@link Page} of {@link AnimalPeso} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnimalPesoQueryService extends QueryService<AnimalPeso> {

    private final Logger log = LoggerFactory.getLogger(AnimalPesoQueryService.class);

    private final AnimalPesoRepository animalPesoRepository;

    private final AnimalPesoSearchRepository animalPesoSearchRepository;

    public AnimalPesoQueryService(AnimalPesoRepository animalPesoRepository, AnimalPesoSearchRepository animalPesoSearchRepository) {
        this.animalPesoRepository = animalPesoRepository;
        this.animalPesoSearchRepository = animalPesoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AnimalPeso} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnimalPeso> findByCriteria(AnimalPesoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AnimalPeso> specification = createSpecification(criteria);
        return animalPesoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AnimalPeso} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnimalPeso> findByCriteria(AnimalPesoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnimalPeso> specification = createSpecification(criteria);
        return animalPesoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnimalPesoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AnimalPeso> specification = createSpecification(criteria);
        return animalPesoRepository.count(specification);
    }

    /**
     * Function to convert {@link AnimalPesoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AnimalPeso> createSpecification(AnimalPesoCriteria criteria) {
        Specification<AnimalPeso> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AnimalPeso_.id));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), AnimalPeso_.fecha));
            }
            if (criteria.getPeso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeso(), AnimalPeso_.peso));
            }
            if (criteria.getAnimalId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnimalId(),
                    root -> root.join(AnimalPeso_.animal, JoinType.LEFT).get(AnimalEvento_.id)));
            }
        }
        return specification;
    }
}
