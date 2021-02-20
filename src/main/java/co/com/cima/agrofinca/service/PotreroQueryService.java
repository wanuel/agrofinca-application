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

import co.com.cima.agrofinca.domain.Potrero;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.PotreroRepository;
import co.com.cima.agrofinca.repository.search.PotreroSearchRepository;
import co.com.cima.agrofinca.service.dto.PotreroCriteria;

/**
 * Service for executing complex queries for {@link Potrero} entities in the database.
 * The main input is a {@link PotreroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Potrero} or a {@link Page} of {@link Potrero} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PotreroQueryService extends QueryService<Potrero> {

    private final Logger log = LoggerFactory.getLogger(PotreroQueryService.class);

    private final PotreroRepository potreroRepository;

    private final PotreroSearchRepository potreroSearchRepository;

    public PotreroQueryService(PotreroRepository potreroRepository, PotreroSearchRepository potreroSearchRepository) {
        this.potreroRepository = potreroRepository;
        this.potreroSearchRepository = potreroSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Potrero} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Potrero> findByCriteria(PotreroCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Potrero> specification = createSpecification(criteria);
        return potreroRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Potrero} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Potrero> findByCriteria(PotreroCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Potrero> specification = createSpecification(criteria);
        return potreroRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PotreroCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Potrero> specification = createSpecification(criteria);
        return potreroRepository.count(specification);
    }

    /**
     * Function to convert {@link PotreroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Potrero> createSpecification(PotreroCriteria criteria) {
        Specification<Potrero> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Potrero_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Potrero_.nombre));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Potrero_.descripcion));
            }
            if (criteria.getPasto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPasto(), Potrero_.pasto));
            }
            if (criteria.getArea() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getArea(), Potrero_.area));
            }
            if (criteria.getPastoreosId() != null) {
                specification = specification.and(buildSpecification(criteria.getPastoreosId(),
                    root -> root.join(Potrero_.pastoreos, JoinType.LEFT).get(PotreroPastoreo_.id)));
            }
            if (criteria.getFincaId() != null) {
                specification = specification.and(buildSpecification(criteria.getFincaId(),
                    root -> root.join(Potrero_.finca, JoinType.LEFT).get(Finca_.id)));
            }
        }
        return specification;
    }
}
