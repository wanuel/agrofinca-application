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

import co.com.cima.agrofinca.domain.Socio;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.SocioRepository;
import co.com.cima.agrofinca.repository.search.SocioSearchRepository;
import co.com.cima.agrofinca.service.dto.SocioCriteria;

/**
 * Service for executing complex queries for {@link Socio} entities in the database.
 * The main input is a {@link SocioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Socio} or a {@link Page} of {@link Socio} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SocioQueryService extends QueryService<Socio> {

    private final Logger log = LoggerFactory.getLogger(SocioQueryService.class);

    private final SocioRepository socioRepository;

    private final SocioSearchRepository socioSearchRepository;

    public SocioQueryService(SocioRepository socioRepository, SocioSearchRepository socioSearchRepository) {
        this.socioRepository = socioRepository;
        this.socioSearchRepository = socioSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Socio} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Socio> findByCriteria(SocioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Socio> specification = createSpecification(criteria);
        return socioRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Socio} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Socio> findByCriteria(SocioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Socio> specification = createSpecification(criteria);
        return socioRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SocioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Socio> specification = createSpecification(criteria);
        return socioRepository.count(specification);
    }

    /**
     * Function to convert {@link SocioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Socio> createSpecification(SocioCriteria criteria) {
        Specification<Socio> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Socio_.id));
            }
            if (criteria.getFechaIngreso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaIngreso(), Socio_.fechaIngreso));
            }
            if (criteria.getParticipacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getParticipacion(), Socio_.participacion));
            }
            if (criteria.getPersonasId() != null) {
                specification = specification.and(buildSpecification(criteria.getPersonasId(),
                    root -> root.join(Socio_.personas, JoinType.LEFT).get(Persona_.id)));
            }
            if (criteria.getSociedadesId() != null) {
                specification = specification.and(buildSpecification(criteria.getSociedadesId(),
                    root -> root.join(Socio_.sociedades, JoinType.LEFT).get(Sociedad_.id)));
            }
        }
        return specification;
    }
}
