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

import co.com.cima.agrofinca.domain.Parametros;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.ParametrosRepository;
import co.com.cima.agrofinca.repository.search.ParametrosSearchRepository;
import co.com.cima.agrofinca.service.dto.ParametrosCriteria;

/**
 * Service for executing complex queries for {@link Parametros} entities in the database.
 * The main input is a {@link ParametrosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Parametros} or a {@link Page} of {@link Parametros} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParametrosQueryService extends QueryService<Parametros> {

    private final Logger log = LoggerFactory.getLogger(ParametrosQueryService.class);

    private final ParametrosRepository parametrosRepository;

    private final ParametrosSearchRepository parametrosSearchRepository;

    public ParametrosQueryService(ParametrosRepository parametrosRepository, ParametrosSearchRepository parametrosSearchRepository) {
        this.parametrosRepository = parametrosRepository;
        this.parametrosSearchRepository = parametrosSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Parametros} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Parametros> findByCriteria(ParametrosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Parametros> specification = createSpecification(criteria);
        return parametrosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Parametros} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Parametros> findByCriteria(ParametrosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Parametros> specification = createSpecification(criteria);
        return parametrosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParametrosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Parametros> specification = createSpecification(criteria);
        return parametrosRepository.count(specification);
    }

    /**
     * Function to convert {@link ParametrosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Parametros> createSpecification(ParametrosCriteria criteria) {
        Specification<Parametros> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Parametros_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Parametros_.nombre));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Parametros_.descripcion));
            }
            if (criteria.getEventosId() != null) {
                specification = specification.and(buildSpecification(criteria.getEventosId(),
                    root -> root.join(Parametros_.eventos, JoinType.LEFT).get(Evento_.id)));
            }
            if (criteria.getMedicamentosId() != null) {
                specification = specification.and(buildSpecification(criteria.getMedicamentosId(),
                    root -> root.join(Parametros_.medicamentos, JoinType.LEFT).get(AnimalSalud_.id)));
            }
            if (criteria.getParametrosId() != null) {
                specification = specification.and(buildSpecification(criteria.getParametrosId(),
                    root -> root.join(Parametros_.parametros, JoinType.LEFT).get(Parametros_.id)));
            }
            if (criteria.getTipoLotesId() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoLotesId(),
                    root -> root.join(Parametros_.tipoLotes, JoinType.LEFT).get(Lote_.id)));
            }
            if (criteria.getTiposId() != null) {
                specification = specification.and(buildSpecification(criteria.getTiposId(),
                    root -> root.join(Parametros_.tipos, JoinType.LEFT).get(Animal_.id)));
            }
            if (criteria.getRazasId() != null) {
                specification = specification.and(buildSpecification(criteria.getRazasId(),
                    root -> root.join(Parametros_.razas, JoinType.LEFT).get(Animal_.id)));
            }
            if (criteria.getAgrupadorId() != null) {
                specification = specification.and(buildSpecification(criteria.getAgrupadorId(),
                    root -> root.join(Parametros_.agrupador, JoinType.LEFT).get(Parametros_.id)));
            }
        }
        return specification;
    }
}
