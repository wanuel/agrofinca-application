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

import co.com.cima.agrofinca.domain.Persona;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.PersonaRepository;
import co.com.cima.agrofinca.repository.search.PersonaSearchRepository;
import co.com.cima.agrofinca.service.dto.PersonaCriteria;

/**
 * Service for executing complex queries for {@link Persona} entities in the database.
 * The main input is a {@link PersonaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Persona} or a {@link Page} of {@link Persona} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonaQueryService extends QueryService<Persona> {

    private final Logger log = LoggerFactory.getLogger(PersonaQueryService.class);

    private final PersonaRepository personaRepository;

    private final PersonaSearchRepository personaSearchRepository;

    public PersonaQueryService(PersonaRepository personaRepository, PersonaSearchRepository personaSearchRepository) {
        this.personaRepository = personaRepository;
        this.personaSearchRepository = personaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Persona} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Persona> findByCriteria(PersonaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Persona> specification = createSpecification(criteria);
        return personaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Persona} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Persona> findByCriteria(PersonaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Persona> specification = createSpecification(criteria);
        return personaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Persona> specification = createSpecification(criteria);
        return personaRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Persona> createSpecification(PersonaCriteria criteria) {
        Specification<Persona> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Persona_.id));
            }
            if (criteria.getTipoDocumento() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoDocumento(), Persona_.tipoDocumento));
            }
            if (criteria.getNumDocuemnto() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumDocuemnto(), Persona_.numDocuemnto));
            }
            if (criteria.getPrimerNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrimerNombre(), Persona_.primerNombre));
            }
            if (criteria.getSegundoNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSegundoNombre(), Persona_.segundoNombre));
            }
            if (criteria.getPrimerApellido() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrimerApellido(), Persona_.primerApellido));
            }
            if (criteria.getSegundoApellido() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSegundoApellido(), Persona_.segundoApellido));
            }
            if (criteria.getFechaNacimiento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaNacimiento(), Persona_.fechaNacimiento));
            }
            if (criteria.getGenero() != null) {
                specification = specification.and(buildSpecification(criteria.getGenero(), Persona_.genero));
            }
            if (criteria.getSocioId() != null) {
                specification = specification.and(buildSpecification(criteria.getSocioId(),
                    root -> root.join(Persona_.socio, JoinType.LEFT).get(Socio_.id)));
            }
        }
        return specification;
    }
}
