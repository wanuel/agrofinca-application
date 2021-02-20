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

import co.com.cima.agrofinca.domain.Sociedad;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.SociedadRepository;
import co.com.cima.agrofinca.repository.search.SociedadSearchRepository;
import co.com.cima.agrofinca.service.dto.SociedadCriteria;

/**
 * Service for executing complex queries for {@link Sociedad} entities in the database.
 * The main input is a {@link SociedadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sociedad} or a {@link Page} of {@link Sociedad} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SociedadQueryService extends QueryService<Sociedad> {

    private final Logger log = LoggerFactory.getLogger(SociedadQueryService.class);

    private final SociedadRepository sociedadRepository;

    private final SociedadSearchRepository sociedadSearchRepository;

    public SociedadQueryService(SociedadRepository sociedadRepository, SociedadSearchRepository sociedadSearchRepository) {
        this.sociedadRepository = sociedadRepository;
        this.sociedadSearchRepository = sociedadSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Sociedad} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sociedad> findByCriteria(SociedadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sociedad> specification = createSpecification(criteria);
        return sociedadRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sociedad} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sociedad> findByCriteria(SociedadCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sociedad> specification = createSpecification(criteria);
        return sociedadRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SociedadCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sociedad> specification = createSpecification(criteria);
        return sociedadRepository.count(specification);
    }

    /**
     * Function to convert {@link SociedadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sociedad> createSpecification(SociedadCriteria criteria) {
        Specification<Sociedad> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sociedad_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Sociedad_.nombre));
            }
            if (criteria.getFechaCreacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaCreacion(), Sociedad_.fechaCreacion));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Sociedad_.estado));
            }
            if (criteria.getObservaciones() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservaciones(), Sociedad_.observaciones));
            }
            if (criteria.getSocioId() != null) {
                specification = specification.and(buildSpecification(criteria.getSocioId(),
                    root -> root.join(Sociedad_.socio, JoinType.LEFT).get(Socio_.id)));
            }
        }
        return specification;
    }
}
