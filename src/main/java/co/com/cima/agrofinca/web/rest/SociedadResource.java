package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.domain.Sociedad;
import co.com.cima.agrofinca.service.SociedadService;
import co.com.cima.agrofinca.web.rest.errors.BadRequestAlertException;
import co.com.cima.agrofinca.service.dto.SociedadCriteria;
import co.com.cima.agrofinca.service.SociedadQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link co.com.cima.agrofinca.domain.Sociedad}.
 */
@RestController
@RequestMapping("/api")
public class SociedadResource {

    private final Logger log = LoggerFactory.getLogger(SociedadResource.class);

    private static final String ENTITY_NAME = "sociedad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SociedadService sociedadService;

    private final SociedadQueryService sociedadQueryService;

    public SociedadResource(SociedadService sociedadService, SociedadQueryService sociedadQueryService) {
        this.sociedadService = sociedadService;
        this.sociedadQueryService = sociedadQueryService;
    }

    /**
     * {@code POST  /sociedads} : Create a new sociedad.
     *
     * @param sociedad the sociedad to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sociedad, or with status {@code 400 (Bad Request)} if the sociedad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sociedads")
    public ResponseEntity<Sociedad> createSociedad(@Valid @RequestBody Sociedad sociedad) throws URISyntaxException {
        log.debug("REST request to save Sociedad : {}", sociedad);
        if (sociedad.getId() != null) {
            throw new BadRequestAlertException("A new sociedad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sociedad result = sociedadService.save(sociedad);
        return ResponseEntity.created(new URI("/api/sociedads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sociedads} : Updates an existing sociedad.
     *
     * @param sociedad the sociedad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sociedad,
     * or with status {@code 400 (Bad Request)} if the sociedad is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sociedad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sociedads")
    public ResponseEntity<Sociedad> updateSociedad(@Valid @RequestBody Sociedad sociedad) throws URISyntaxException {
        log.debug("REST request to update Sociedad : {}", sociedad);
        if (sociedad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Sociedad result = sociedadService.save(sociedad);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sociedad.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sociedads} : get all the sociedads.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sociedads in body.
     */
    @GetMapping("/sociedads")
    public ResponseEntity<List<Sociedad>> getAllSociedads(SociedadCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Sociedads by criteria: {}", criteria);
        Page<Sociedad> page = sociedadQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sociedads/count} : count all the sociedads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sociedads/count")
    public ResponseEntity<Long> countSociedads(SociedadCriteria criteria) {
        log.debug("REST request to count Sociedads by criteria: {}", criteria);
        return ResponseEntity.ok().body(sociedadQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sociedads/:id} : get the "id" sociedad.
     *
     * @param id the id of the sociedad to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sociedad, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sociedads/{id}")
    public ResponseEntity<Sociedad> getSociedad(@PathVariable Long id) {
        log.debug("REST request to get Sociedad : {}", id);
        Optional<Sociedad> sociedad = sociedadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sociedad);
    }

    /**
     * {@code DELETE  /sociedads/:id} : delete the "id" sociedad.
     *
     * @param id the id of the sociedad to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sociedads/{id}")
    public ResponseEntity<Void> deleteSociedad(@PathVariable Long id) {
        log.debug("REST request to delete Sociedad : {}", id);
        sociedadService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/sociedads?query=:query} : search for the sociedad corresponding
     * to the query.
     *
     * @param query the query of the sociedad search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/sociedads")
    public ResponseEntity<List<Sociedad>> searchSociedads(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Sociedads for query {}", query);
        Page<Sociedad> page = sociedadService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
