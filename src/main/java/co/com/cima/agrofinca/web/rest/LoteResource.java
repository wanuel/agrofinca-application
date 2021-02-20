package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.domain.Lote;
import co.com.cima.agrofinca.service.LoteService;
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
 * REST controller for managing {@link co.com.cima.agrofinca.domain.Lote}.
 */
@RestController
@RequestMapping("/api")
public class LoteResource {

    private final Logger log = LoggerFactory.getLogger(LoteResource.class);

    private static final String ENTITY_NAME = "lote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoteService loteService;

    public LoteResource(LoteService loteService) {
        this.loteService = loteService;
    }

    /**
     * {@code POST  /lotes} : Create a new lote.
     *
     * @param lote the lote to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lote, or with status {@code 400 (Bad Request)} if the lote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lotes")
    public ResponseEntity<Lote> createLote(@Valid @RequestBody Lote lote) throws URISyntaxException {
        log.debug("REST request to save Lote : {}", lote);
        if (lote.getId() != null) {
            throw new BadRequestAlertException("A new lote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Lote result = loteService.save(lote);
        return ResponseEntity.created(new URI("/api/lotes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lotes} : Updates an existing lote.
     *
     * @param lote the lote to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lote,
     * or with status {@code 400 (Bad Request)} if the lote is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lote couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lotes")
    public ResponseEntity<Lote> updateLote(@Valid @RequestBody Lote lote) throws URISyntaxException {
        log.debug("REST request to update Lote : {}", lote);
        if (lote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Lote result = loteService.save(lote);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lote.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lotes} : get all the lotes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lotes in body.
     */
    @GetMapping("/lotes")
    public ResponseEntity<List<Lote>> getAllLotes(Pageable pageable) {
        log.debug("REST request to get a page of Lotes");
        Page<Lote> page = loteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lotes/:id} : get the "id" lote.
     *
     * @param id the id of the lote to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lote, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lotes/{id}")
    public ResponseEntity<Lote> getLote(@PathVariable Long id) {
        log.debug("REST request to get Lote : {}", id);
        Optional<Lote> lote = loteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lote);
    }

    /**
     * {@code DELETE  /lotes/:id} : delete the "id" lote.
     *
     * @param id the id of the lote to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lotes/{id}")
    public ResponseEntity<Void> deleteLote(@PathVariable Long id) {
        log.debug("REST request to delete Lote : {}", id);
        loteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/lotes?query=:query} : search for the lote corresponding
     * to the query.
     *
     * @param query the query of the lote search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lotes")
    public ResponseEntity<List<Lote>> searchLotes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Lotes for query {}", query);
        Page<Lote> page = loteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
