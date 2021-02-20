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

import co.com.cima.agrofinca.domain.Animal;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.AnimalRepository;
import co.com.cima.agrofinca.repository.search.AnimalSearchRepository;
import co.com.cima.agrofinca.service.dto.AnimalCriteria;

/**
 * Service for executing complex queries for {@link Animal} entities in the database.
 * The main input is a {@link AnimalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Animal} or a {@link Page} of {@link Animal} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnimalQueryService extends QueryService<Animal> {

    private final Logger log = LoggerFactory.getLogger(AnimalQueryService.class);

    private final AnimalRepository animalRepository;

    private final AnimalSearchRepository animalSearchRepository;

    public AnimalQueryService(AnimalRepository animalRepository, AnimalSearchRepository animalSearchRepository) {
        this.animalRepository = animalRepository;
        this.animalSearchRepository = animalSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Animal} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Animal> findByCriteria(AnimalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Animal> specification = createSpecification(criteria);
        return animalRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Animal} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Animal> findByCriteria(AnimalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Animal> specification = createSpecification(criteria);
        return animalRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnimalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Animal> specification = createSpecification(criteria);
        return animalRepository.count(specification);
    }

    /**
     * Function to convert {@link AnimalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Animal> createSpecification(AnimalCriteria criteria) {
        Specification<Animal> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Animal_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Animal_.nombre));
            }
            if (criteria.getCaracterizacion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCaracterizacion(), Animal_.caracterizacion));
            }
            if (criteria.getHierro() != null) {
                specification = specification.and(buildSpecification(criteria.getHierro(), Animal_.hierro));
            }
            if (criteria.getFechaNacimiento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaNacimiento(), Animal_.fechaNacimiento));
            }
            if (criteria.getFechaCompra() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaCompra(), Animal_.fechaCompra));
            }
            if (criteria.getSexo() != null) {
                specification = specification.and(buildSpecification(criteria.getSexo(), Animal_.sexo));
            }
            if (criteria.getCastrado() != null) {
                specification = specification.and(buildSpecification(criteria.getCastrado(), Animal_.castrado));
            }
            if (criteria.getFechaCastracion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaCastracion(), Animal_.fechaCastracion));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Animal_.estado));
            }
            if (criteria.getLotesId() != null) {
                specification = specification.and(buildSpecification(criteria.getLotesId(),
                    root -> root.join(Animal_.lotes, JoinType.LEFT).get(AnimalLote_.id)));
            }
            if (criteria.getImagenesId() != null) {
                specification = specification.and(buildSpecification(criteria.getImagenesId(),
                    root -> root.join(Animal_.imagenes, JoinType.LEFT).get(AnimalImagen_.id)));
            }
            if (criteria.getEventosId() != null) {
                specification = specification.and(buildSpecification(criteria.getEventosId(),
                    root -> root.join(Animal_.eventos, JoinType.LEFT).get(AnimalEvento_.id)));
            }
            if (criteria.getTipoId() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoId(),
                    root -> root.join(Animal_.tipo, JoinType.LEFT).get(Parametros_.id)));
            }
            if (criteria.getRazaId() != null) {
                specification = specification.and(buildSpecification(criteria.getRazaId(),
                    root -> root.join(Animal_.raza, JoinType.LEFT).get(Parametros_.id)));
            }
        }
        return specification;
    }
}
