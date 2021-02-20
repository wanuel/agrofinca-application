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

import co.com.cima.agrofinca.domain.Evento;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.EventoRepository;
import co.com.cima.agrofinca.repository.search.EventoSearchRepository;
import co.com.cima.agrofinca.service.dto.EventoCriteria;

/**
 * Service for executing complex queries for {@link Evento} entities in the database.
 * The main input is a {@link EventoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Evento} or a {@link Page} of {@link Evento} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventoQueryService extends QueryService<Evento> {

    private final Logger log = LoggerFactory.getLogger(EventoQueryService.class);

    private final EventoRepository eventoRepository;

    private final EventoSearchRepository eventoSearchRepository;

    public EventoQueryService(EventoRepository eventoRepository, EventoSearchRepository eventoSearchRepository) {
        this.eventoRepository = eventoRepository;
        this.eventoSearchRepository = eventoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Evento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Evento> findByCriteria(EventoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Evento> specification = createSpecification(criteria);
        return eventoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Evento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Evento> findByCriteria(EventoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Evento> specification = createSpecification(criteria);
        return eventoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Evento> specification = createSpecification(criteria);
        return eventoRepository.count(specification);
    }

    /**
     * Function to convert {@link EventoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Evento> createSpecification(EventoCriteria criteria) {
        Specification<Evento> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Evento_.id));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Evento_.fecha));
            }
            if (criteria.getObservacion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacion(), Evento_.observacion));
            }
            if (criteria.getEventosId() != null) {
                specification = specification.and(buildSpecification(criteria.getEventosId(),
                    root -> root.join(Evento_.eventos, JoinType.LEFT).get(AnimalEvento_.id)));
            }
            if (criteria.getEventoId() != null) {
                specification = specification.and(buildSpecification(criteria.getEventoId(),
                    root -> root.join(Evento_.evento, JoinType.LEFT).get(Parametros_.id)));
            }
        }
        return specification;
    }
}
