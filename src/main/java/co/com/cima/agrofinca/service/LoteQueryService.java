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

import co.com.cima.agrofinca.domain.Lote;
import co.com.cima.agrofinca.domain.*; // for static metamodels
import co.com.cima.agrofinca.repository.LoteRepository;
import co.com.cima.agrofinca.repository.search.LoteSearchRepository;
import co.com.cima.agrofinca.service.dto.LoteCriteria;

/**
 * Service for executing complex queries for {@link Lote} entities in the database.
 * The main input is a {@link LoteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Lote} or a {@link Page} of {@link Lote} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoteQueryService extends QueryService<Lote> {

    private final Logger log = LoggerFactory.getLogger(LoteQueryService.class);

    private final LoteRepository loteRepository;

    private final LoteSearchRepository loteSearchRepository;

    public LoteQueryService(LoteRepository loteRepository, LoteSearchRepository loteSearchRepository) {
        this.loteRepository = loteRepository;
        this.loteSearchRepository = loteSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Lote} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Lote> findByCriteria(LoteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Lote> specification = createSpecification(criteria);
        return loteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Lote} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Lote> findByCriteria(LoteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Lote> specification = createSpecification(criteria);
        return loteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Lote> specification = createSpecification(criteria);
        return loteRepository.count(specification);
    }

    /**
     * Function to convert {@link LoteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Lote> createSpecification(LoteCriteria criteria) {
        Specification<Lote> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Lote_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Lote_.nombre));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Lote_.fecha));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Lote_.estado));
            }
            if (criteria.getPastoreosId() != null) {
                specification = specification.and(buildSpecification(criteria.getPastoreosId(),
                    root -> root.join(Lote_.pastoreos, JoinType.LEFT).get(PotreroPastoreo_.id)));
            }
            if (criteria.getAnimalesId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnimalesId(),
                    root -> root.join(Lote_.animales, JoinType.LEFT).get(AnimalLote_.id)));
            }
            if (criteria.getTipoId() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoId(),
                    root -> root.join(Lote_.tipo, JoinType.LEFT).get(Parametros_.id)));
            }
        }
        return specification;
    }
}
