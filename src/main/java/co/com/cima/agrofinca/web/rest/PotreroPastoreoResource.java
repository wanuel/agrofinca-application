package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.domain.PotreroPastoreo;
import co.com.cima.agrofinca.service.PotreroPastoreoService;
import co.com.cima.agrofinca.web.rest.errors.BadRequestAlertException;
import co.com.cima.agrofinca.service.dto.PotreroPastoreoCriteria;
import co.com.cima.agrofinca.service.PotreroPastoreoQueryService;

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
 * REST controller for managing {@link co.com.cima.agrofinca.domain.PotreroPastoreo}.
 */
@RestController
@RequestMapping("/api")
public class PotreroPastoreoResource {

    private final Logger log = LoggerFactory.getLogger(PotreroPastoreoResource.class);

    private static final String ENTITY_NAME = "potreroPastoreo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PotreroPastoreoService potreroPastoreoService;

    private final PotreroPastoreoQueryService potreroPastoreoQueryService;

    public PotreroPastoreoResource(PotreroPastoreoService potreroPastoreoService, PotreroPastoreoQueryService potreroPastoreoQueryService) {
        this.potreroPastoreoService = potreroPastoreoService;
        this.potreroPastoreoQueryService = potreroPastoreoQueryService;
    }

    /**
     * {@code POST  /potrero-pastoreos} : Create a new potreroPastoreo.
     *
     * @param potreroPastoreo the potreroPastoreo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new potreroPastoreo, or with status {@code 400 (Bad Request)} if the potreroPastoreo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/potrero-pastoreos")
    public ResponseEntity<PotreroPastoreo> createPotreroPastoreo(@Valid @RequestBody PotreroPastoreo potreroPastoreo) throws URISyntaxException {
        log.debug("REST request to save PotreroPastoreo : {}", potreroPastoreo);
        if (potreroPastoreo.getId() != null) {
            throw new BadRequestAlertException("A new potreroPastoreo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PotreroPastoreo result = potreroPastoreoService.save(potreroPastoreo);
        return ResponseEntity.created(new URI("/api/potrero-pastoreos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /potrero-pastoreos} : Updates an existing potreroPastoreo.
     *
     * @param potreroPastoreo the potreroPastoreo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated potreroPastoreo,
     * or with status {@code 400 (Bad Request)} if the potreroPastoreo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the potreroPastoreo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/potrero-pastoreos")
    public ResponseEntity<PotreroPastoreo> updatePotreroPastoreo(@Valid @RequestBody PotreroPastoreo potreroPastoreo) throws URISyntaxException {
        log.debug("REST request to update PotreroPastoreo : {}", potreroPastoreo);
        if (potreroPastoreo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PotreroPastoreo result = potreroPastoreoService.save(potreroPastoreo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, potreroPastoreo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /potrero-pastoreos} : get all the potreroPastoreos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of potreroPastoreos in body.
     */
    @GetMapping("/potrero-pastoreos")
    public ResponseEntity<List<PotreroPastoreo>> getAllPotreroPastoreos(PotreroPastoreoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PotreroPastoreos by criteria: {}", criteria);
        Page<PotreroPastoreo> page = potreroPastoreoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /potrero-pastoreos/count} : count all the potreroPastoreos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/potrero-pastoreos/count")
    public ResponseEntity<Long> countPotreroPastoreos(PotreroPastoreoCriteria criteria) {
        log.debug("REST request to count PotreroPastoreos by criteria: {}", criteria);
        return ResponseEntity.ok().body(potreroPastoreoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /potrero-pastoreos/:id} : get the "id" potreroPastoreo.
     *
     * @param id the id of the potreroPastoreo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the potreroPastoreo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/potrero-pastoreos/{id}")
    public ResponseEntity<PotreroPastoreo> getPotreroPastoreo(@PathVariable Long id) {
        log.debug("REST request to get PotreroPastoreo : {}", id);
        Optional<PotreroPastoreo> potreroPastoreo = potreroPastoreoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(potreroPastoreo);
    }

    /**
     * {@code DELETE  /potrero-pastoreos/:id} : delete the "id" potreroPastoreo.
     *
     * @param id the id of the potreroPastoreo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/potrero-pastoreos/{id}")
    public ResponseEntity<Void> deletePotreroPastoreo(@PathVariable Long id) {
        log.debug("REST request to delete PotreroPastoreo : {}", id);
        potreroPastoreoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/potrero-pastoreos?query=:query} : search for the potreroPastoreo corresponding
     * to the query.
     *
     * @param query the query of the potreroPastoreo search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/potrero-pastoreos")
    public ResponseEntity<List<PotreroPastoreo>> searchPotreroPastoreos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PotreroPastoreos for query {}", query);
        Page<PotreroPastoreo> page = potreroPastoreoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
