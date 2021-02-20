package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.domain.Potrero;
import co.com.cima.agrofinca.service.PotreroService;
import co.com.cima.agrofinca.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link co.com.cima.agrofinca.domain.Potrero}.
 */
@RestController
@RequestMapping("/api")
public class PotreroResource {

    private final Logger log = LoggerFactory.getLogger(PotreroResource.class);

    private static final String ENTITY_NAME = "potrero";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PotreroService potreroService;

    public PotreroResource(PotreroService potreroService) {
        this.potreroService = potreroService;
    }

    /**
     * {@code POST  /potreros} : Create a new potrero.
     *
     * @param potrero the potrero to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new potrero, or with status {@code 400 (Bad Request)} if the potrero has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/potreros")
    public ResponseEntity<Potrero> createPotrero(@Valid @RequestBody Potrero potrero) throws URISyntaxException {
        log.debug("REST request to save Potrero : {}", potrero);
        if (potrero.getId() != null) {
            throw new BadRequestAlertException("A new potrero cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Potrero result = potreroService.save(potrero);
        return ResponseEntity.created(new URI("/api/potreros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /potreros} : Updates an existing potrero.
     *
     * @param potrero the potrero to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated potrero,
     * or with status {@code 400 (Bad Request)} if the potrero is not valid,
     * or with status {@code 500 (Internal Server Error)} if the potrero couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/potreros")
    public ResponseEntity<Potrero> updatePotrero(@Valid @RequestBody Potrero potrero) throws URISyntaxException {
        log.debug("REST request to update Potrero : {}", potrero);
        if (potrero.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Potrero result = potreroService.save(potrero);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, potrero.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /potreros} : get all the potreros.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of potreros in body.
     */
    @GetMapping("/potreros")
    public ResponseEntity<List<Potrero>> getAllPotreros(Pageable pageable) {
        log.debug("REST request to get a page of Potreros");
        Page<Potrero> page = potreroService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /potreros/:id} : get the "id" potrero.
     *
     * @param id the id of the potrero to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the potrero, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/potreros/{id}")
    public ResponseEntity<Potrero> getPotrero(@PathVariable Long id) {
        log.debug("REST request to get Potrero : {}", id);
        Optional<Potrero> potrero = potreroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(potrero);
    }

    /**
     * {@code DELETE  /potreros/:id} : delete the "id" potrero.
     *
     * @param id the id of the potrero to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/potreros/{id}")
    public ResponseEntity<Void> deletePotrero(@PathVariable Long id) {
        log.debug("REST request to delete Potrero : {}", id);
        potreroService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/potreros?query=:query} : search for the potrero corresponding
     * to the query.
     *
     * @param query the query of the potrero search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/potreros")
    public ResponseEntity<List<Potrero>> searchPotreros(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Potreros for query {}", query);
        Page<Potrero> page = potreroService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
