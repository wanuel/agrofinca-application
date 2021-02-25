package co.com.cima.agrofinca.service;

import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.domain.AnimalCostos;
import co.com.cima.agrofinca.repository.AnimalCostosRepository;
import co.com.cima.agrofinca.repository.search.AnimalCostosSearchRepository;
import co.com.cima.agrofinca.service.dto.AnimalCostosCriteria;
import io.github.jhipster.service.QueryService;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link AnimalCostos} entities in the database.
 * The main input is a {@link AnimalCostosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnimalCostos} or a {@link Page} of {@link AnimalCostos} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnimalCostosQueryService extends QueryService<AnimalCostos> {
    private final Logger log = LoggerFactory.getLogger(AnimalCostosQueryService.class);

    private final AnimalCostosRepository animalCostosRepository;

    private final AnimalCostosSearchRepository animalCostosSearchRepository;

    public AnimalCostosQueryService(
        AnimalCostosRepository animalCostosRepository,
        AnimalCostosSearchRepository animalCostosSearchRepository
    ) {
        this.animalCostosRepository = animalCostosRepository;
        this.animalCostosSearchRepository = animalCostosSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AnimalCostos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnimalCostos> findByCriteria(AnimalCostosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AnimalCostos> specification = createSpecification(criteria);
        return animalCostosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AnimalCostos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnimalCostos> findByCriteria(AnimalCostosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnimalCostos> specification = createSpecification(criteria);
        return animalCostosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnimalCostosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AnimalCostos> specification = createSpecification(criteria);
        return animalCostosRepository.count(specification);
    }

    /**
     * Function to convert {@link AnimalCostosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AnimalCostos> createSpecification(AnimalCostosCriteria criteria) {
        Specification<AnimalCostos> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AnimalCostos_.id));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), AnimalCostos_.fecha));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor(), AnimalCostos_.valor));
            }
            if (criteria.getAnimalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAnimalId(),
                            root -> root.join(AnimalCostos_.evento, JoinType.LEFT).get(AnimalEvento_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
