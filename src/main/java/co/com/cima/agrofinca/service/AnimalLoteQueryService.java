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

import co.com.cima.agrofinca.domain.AnimalLote;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.AnimalLoteRepository;
import co.com.cima.agrofinca.repository.search.AnimalLoteSearchRepository;
import co.com.cima.agrofinca.service.dto.AnimalLoteCriteria;

/**
 * Service for executing complex queries for {@link AnimalLote} entities in the database.
 * The main input is a {@link AnimalLoteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnimalLote} or a {@link Page} of {@link AnimalLote} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnimalLoteQueryService extends QueryService<AnimalLote> {

    private final Logger log = LoggerFactory.getLogger(AnimalLoteQueryService.class);

    private final AnimalLoteRepository animalLoteRepository;

    private final AnimalLoteSearchRepository animalLoteSearchRepository;

    public AnimalLoteQueryService(AnimalLoteRepository animalLoteRepository, AnimalLoteSearchRepository animalLoteSearchRepository) {
        this.animalLoteRepository = animalLoteRepository;
        this.animalLoteSearchRepository = animalLoteSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AnimalLote} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnimalLote> findByCriteria(AnimalLoteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AnimalLote> specification = createSpecification(criteria);
        return animalLoteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AnimalLote} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnimalLote> findByCriteria(AnimalLoteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnimalLote> specification = createSpecification(criteria);
        return animalLoteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnimalLoteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AnimalLote> specification = createSpecification(criteria);
        return animalLoteRepository.count(specification);
    }

    /**
     * Function to convert {@link AnimalLoteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AnimalLote> createSpecification(AnimalLoteCriteria criteria) {
        Specification<AnimalLote> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AnimalLote_.id));
            }
            if (criteria.getFechaIngreso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaIngreso(), AnimalLote_.fechaIngreso));
            }
            if (criteria.getFechaSalida() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaSalida(), AnimalLote_.fechaSalida));
            }
            if (criteria.getAnimalId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnimalId(),
                    root -> root.join(AnimalLote_.animal, JoinType.LEFT).get(Animal_.id)));
            }
            if (criteria.getLoteId() != null) {
                specification = specification.and(buildSpecification(criteria.getLoteId(),
                    root -> root.join(AnimalLote_.lote, JoinType.LEFT).get(Lote_.id)));
            }
        }
        return specification;
    }
}
