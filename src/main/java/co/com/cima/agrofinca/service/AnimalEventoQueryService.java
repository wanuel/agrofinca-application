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

import co.com.cima.agrofinca.domain.AnimalEvento;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.AnimalEventoRepository;
import co.com.cima.agrofinca.repository.search.AnimalEventoSearchRepository;
import co.com.cima.agrofinca.service.dto.AnimalEventoCriteria;

/**
 * Service for executing complex queries for {@link AnimalEvento} entities in the database.
 * The main input is a {@link AnimalEventoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnimalEvento} or a {@link Page} of {@link AnimalEvento} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnimalEventoQueryService extends QueryService<AnimalEvento> {

    private final Logger log = LoggerFactory.getLogger(AnimalEventoQueryService.class);

    private final AnimalEventoRepository animalEventoRepository;

    private final AnimalEventoSearchRepository animalEventoSearchRepository;

    public AnimalEventoQueryService(AnimalEventoRepository animalEventoRepository, AnimalEventoSearchRepository animalEventoSearchRepository) {
        this.animalEventoRepository = animalEventoRepository;
        this.animalEventoSearchRepository = animalEventoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AnimalEvento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnimalEvento> findByCriteria(AnimalEventoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AnimalEvento> specification = createSpecification(criteria);
        return animalEventoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AnimalEvento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnimalEvento> findByCriteria(AnimalEventoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnimalEvento> specification = createSpecification(criteria);
        return animalEventoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnimalEventoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AnimalEvento> specification = createSpecification(criteria);
        return animalEventoRepository.count(specification);
    }

    /**
     * Function to convert {@link AnimalEventoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AnimalEvento> createSpecification(AnimalEventoCriteria criteria) {
        Specification<AnimalEvento> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AnimalEvento_.id));
            }
            if (criteria.getTratamientosId() != null) {
                specification = specification.and(buildSpecification(criteria.getTratamientosId(),
                    root -> root.join(AnimalEvento_.tratamientos, JoinType.LEFT).get(AnimalSalud_.id)));
            }
            if (criteria.getPesosId() != null) {
                specification = specification.and(buildSpecification(criteria.getPesosId(),
                    root -> root.join(AnimalEvento_.pesos, JoinType.LEFT).get(AnimalPeso_.id)));
            }
            if (criteria.getCostosId() != null) {
                specification = specification.and(buildSpecification(criteria.getCostosId(),
                    root -> root.join(AnimalEvento_.costos, JoinType.LEFT).get(AnimalCostos_.id)));
            }
            if (criteria.getAnimalId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnimalId(),
                    root -> root.join(AnimalEvento_.animal, JoinType.LEFT).get(Animal_.id)));
            }
            if (criteria.getEventoId() != null) {
                specification = specification.and(buildSpecification(criteria.getEventoId(),
                    root -> root.join(AnimalEvento_.evento, JoinType.LEFT).get(Evento_.id)));
            }
        }
        return specification;
    }
}
