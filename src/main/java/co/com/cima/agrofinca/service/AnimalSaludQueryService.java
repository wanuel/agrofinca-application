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

import co.com.cima.agrofinca.domain.AnimalSalud;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.AnimalSaludRepository;
import co.com.cima.agrofinca.repository.search.AnimalSaludSearchRepository;
import co.com.cima.agrofinca.service.dto.AnimalSaludCriteria;

/**
 * Service for executing complex queries for {@link AnimalSalud} entities in the database.
 * The main input is a {@link AnimalSaludCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnimalSalud} or a {@link Page} of {@link AnimalSalud} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnimalSaludQueryService extends QueryService<AnimalSalud> {

    private final Logger log = LoggerFactory.getLogger(AnimalSaludQueryService.class);

    private final AnimalSaludRepository animalSaludRepository;

    private final AnimalSaludSearchRepository animalSaludSearchRepository;

    public AnimalSaludQueryService(AnimalSaludRepository animalSaludRepository, AnimalSaludSearchRepository animalSaludSearchRepository) {
        this.animalSaludRepository = animalSaludRepository;
        this.animalSaludSearchRepository = animalSaludSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AnimalSalud} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnimalSalud> findByCriteria(AnimalSaludCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AnimalSalud> specification = createSpecification(criteria);
        return animalSaludRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AnimalSalud} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnimalSalud> findByCriteria(AnimalSaludCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnimalSalud> specification = createSpecification(criteria);
        return animalSaludRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnimalSaludCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AnimalSalud> specification = createSpecification(criteria);
        return animalSaludRepository.count(specification);
    }

    /**
     * Function to convert {@link AnimalSaludCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AnimalSalud> createSpecification(AnimalSaludCriteria criteria) {
        Specification<AnimalSalud> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AnimalSalud_.id));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), AnimalSalud_.fecha));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), AnimalSalud_.nombre));
            }
            if (criteria.getLaboratorio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLaboratorio(), AnimalSalud_.laboratorio));
            }
            if (criteria.getDosis() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDosis(), AnimalSalud_.dosis));
            }
            if (criteria.getInyectado() != null) {
                specification = specification.and(buildSpecification(criteria.getInyectado(), AnimalSalud_.inyectado));
            }
            if (criteria.getIntramuscular() != null) {
                specification = specification.and(buildSpecification(criteria.getIntramuscular(), AnimalSalud_.intramuscular));
            }
            if (criteria.getSubcutaneo() != null) {
                specification = specification.and(buildSpecification(criteria.getSubcutaneo(), AnimalSalud_.subcutaneo));
            }
            if (criteria.getObservacion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacion(), AnimalSalud_.observacion));
            }
            if (criteria.getEventoId() != null) {
                specification = specification.and(buildSpecification(criteria.getEventoId(),
                    root -> root.join(AnimalSalud_.evento, JoinType.LEFT).get(AnimalEvento_.id)));
            }
            if (criteria.getMedicamentoId() != null) {
                specification = specification.and(buildSpecification(criteria.getMedicamentoId(),
                    root -> root.join(AnimalSalud_.medicamento, JoinType.LEFT).get(Parametros_.id)));
            }
        }
        return specification;
    }
}
