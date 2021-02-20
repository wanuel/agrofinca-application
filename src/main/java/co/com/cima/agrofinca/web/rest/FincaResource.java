package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.domain.Finca;
import co.com.cima.agrofinca.service.FincaService;
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
 * REST controller for managing {@link co.com.cima.agrofinca.domain.Finca}.
 */
@RestController
@RequestMapping("/api")
public class FincaResource {

    private final Logger log = LoggerFactory.getLogger(FincaResource.class);

    private static final String ENTITY_NAME = "finca";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FincaService fincaService;

    public FincaResource(FincaService fincaService) {
        this.fincaService = fincaService;
    }

    /**
     * {@code POST  /fincas} : Create a new finca.
     *
     * @param finca the finca to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new finca, or with status {@code 400 (Bad Request)} if the finca has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fincas")
    public ResponseEntity<Finca> createFinca(@Valid @RequestBody Finca finca) throws URISyntaxException {
        log.debug("REST request to save Finca : {}", finca);
        if (finca.getId() != null) {
            throw new BadRequestAlertException("A new finca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Finca result = fincaService.save(finca);
        return ResponseEntity.created(new URI("/api/fincas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fincas} : Updates an existing finca.
     *
     * @param finca the finca to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated finca,
     * or with status {@code 400 (Bad Request)} if the finca is not valid,
     * or with status {@code 500 (Internal Server Error)} if the finca couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fincas")
    public ResponseEntity<Finca> updateFinca(@Valid @RequestBody Finca finca) throws URISyntaxException {
        log.debug("REST request to update Finca : {}", finca);
        if (finca.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Finca result = fincaService.save(finca);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, finca.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fincas} : get all the fincas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fincas in body.
     */
    @GetMapping("/fincas")
    public ResponseEntity<List<Finca>> getAllFincas(Pageable pageable) {
        log.debug("REST request to get a page of Fincas");
        Page<Finca> page = fincaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fincas/:id} : get the "id" finca.
     *
     * @param id the id of the finca to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the finca, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fincas/{id}")
    public ResponseEntity<Finca> getFinca(@PathVariable Long id) {
        log.debug("REST request to get Finca : {}", id);
        Optional<Finca> finca = fincaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(finca);
    }

    /**
     * {@code DELETE  /fincas/:id} : delete the "id" finca.
     *
     * @param id the id of the finca to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fincas/{id}")
    public ResponseEntity<Void> deleteFinca(@PathVariable Long id) {
        log.debug("REST request to delete Finca : {}", id);
        fincaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/fincas?query=:query} : search for the finca corresponding
     * to the query.
     *
     * @param query the query of the finca search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fincas")
    public ResponseEntity<List<Finca>> searchFincas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Fincas for query {}", query);
        Page<Finca> page = fincaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
