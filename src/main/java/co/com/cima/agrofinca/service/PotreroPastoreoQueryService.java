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

import co.com.cima.agrofinca.domain.PotreroPastoreo;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.PotreroPastoreoRepository;
import co.com.cima.agrofinca.repository.search.PotreroPastoreoSearchRepository;
import co.com.cima.agrofinca.service.dto.PotreroPastoreoCriteria;

/**
 * Service for executing complex queries for {@link PotreroPastoreo} entities in the database.
 * The main input is a {@link PotreroPastoreoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PotreroPastoreo} or a {@link Page} of {@link PotreroPastoreo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PotreroPastoreoQueryService extends QueryService<PotreroPastoreo> {

    private final Logger log = LoggerFactory.getLogger(PotreroPastoreoQueryService.class);

    private final PotreroPastoreoRepository potreroPastoreoRepository;

    private final PotreroPastoreoSearchRepository potreroPastoreoSearchRepository;

    public PotreroPastoreoQueryService(PotreroPastoreoRepository potreroPastoreoRepository, PotreroPastoreoSearchRepository potreroPastoreoSearchRepository) {
        this.potreroPastoreoRepository = potreroPastoreoRepository;
        this.potreroPastoreoSearchRepository = potreroPastoreoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PotreroPastoreo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PotreroPastoreo> findByCriteria(PotreroPastoreoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PotreroPastoreo> specification = createSpecification(criteria);
        return potreroPastoreoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PotreroPastoreo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PotreroPastoreo> findByCriteria(PotreroPastoreoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PotreroPastoreo> specification = createSpecification(criteria);
        return potreroPastoreoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PotreroPastoreoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PotreroPastoreo> specification = createSpecification(criteria);
        return potreroPastoreoRepository.count(specification);
    }

    /**
     * Function to convert {@link PotreroPastoreoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PotreroPastoreo> createSpecification(PotreroPastoreoCriteria criteria) {
        Specification<PotreroPastoreo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PotreroPastoreo_.id));
            }
            if (criteria.getFechaIngreso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaIngreso(), PotreroPastoreo_.fechaIngreso));
            }
            if (criteria.getFechaSalida() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaSalida(), PotreroPastoreo_.fechaSalida));
            }
            if (criteria.getFechaLimpia() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaLimpia(), PotreroPastoreo_.fechaLimpia));
            }
            if (criteria.getDiasDescanso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiasDescanso(), PotreroPastoreo_.diasDescanso));
            }
            if (criteria.getDiasCarga() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiasCarga(), PotreroPastoreo_.diasCarga));
            }
            if (criteria.getLimpia() != null) {
                specification = specification.and(buildSpecification(criteria.getLimpia(), PotreroPastoreo_.limpia));
            }
            if (criteria.getLoteId() != null) {
                specification = specification.and(buildSpecification(criteria.getLoteId(),
                    root -> root.join(PotreroPastoreo_.lote, JoinType.LEFT).get(Lote_.id)));
            }
            if (criteria.getPotreroId() != null) {
                specification = specification.and(buildSpecification(criteria.getPotreroId(),
                    root -> root.join(PotreroPastoreo_.potrero, JoinType.LEFT).get(Potrero_.id)));
            }
        }
        return specification;
    }
}
