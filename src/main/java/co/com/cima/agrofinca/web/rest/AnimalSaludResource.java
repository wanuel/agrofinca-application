package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.domain.AnimalSalud;
import co.com.cima.agrofinca.service.AnimalSaludService;
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
 * REST controller for managing {@link co.com.cima.agrofinca.domain.AnimalSalud}.
 */
@RestController
@RequestMapping("/api")
public class AnimalSaludResource {

    private final Logger log = LoggerFactory.getLogger(AnimalSaludResource.class);

    private static final String ENTITY_NAME = "animalSalud";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnimalSaludService animalSaludService;

    public AnimalSaludResource(AnimalSaludService animalSaludService) {
        this.animalSaludService = animalSaludService;
    }

    /**
     * {@code POST  /animal-saluds} : Create a new animalSalud.
     *
     * @param animalSalud the animalSalud to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new animalSalud, or with status {@code 400 (Bad Request)} if the animalSalud has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/animal-saluds")
    public ResponseEntity<AnimalSalud> createAnimalSalud(@Valid @RequestBody AnimalSalud animalSalud) throws URISyntaxException {
        log.debug("REST request to save AnimalSalud : {}", animalSalud);
        if (animalSalud.getId() != null) {
            throw new BadRequestAlertException("A new animalSalud cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnimalSalud result = animalSaludService.save(animalSalud);
        return ResponseEntity.created(new URI("/api/animal-saluds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /animal-saluds} : Updates an existing animalSalud.
     *
     * @param animalSalud the animalSalud to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated animalSalud,
     * or with status {@code 400 (Bad Request)} if the animalSalud is not valid,
     * or with status {@code 500 (Internal Server Error)} if the animalSalud couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/animal-saluds")
    public ResponseEntity<AnimalSalud> updateAnimalSalud(@Valid @RequestBody AnimalSalud animalSalud) throws URISyntaxException {
        log.debug("REST request to update AnimalSalud : {}", animalSalud);
        if (animalSalud.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnimalSalud result = animalSaludService.save(animalSalud);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, animalSalud.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /animal-saluds} : get all the animalSaluds.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of animalSaluds in body.
     */
    @GetMapping("/animal-saluds")
    public ResponseEntity<List<AnimalSalud>> getAllAnimalSaluds(Pageable pageable) {
        log.debug("REST request to get a page of AnimalSaluds");
        Page<AnimalSalud> page = animalSaludService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /animal-saluds/:id} : get the "id" animalSalud.
     *
     * @param id the id of the animalSalud to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the animalSalud, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/animal-saluds/{id}")
    public ResponseEntity<AnimalSalud> getAnimalSalud(@PathVariable Long id) {
        log.debug("REST request to get AnimalSalud : {}", id);
        Optional<AnimalSalud> animalSalud = animalSaludService.findOne(id);
        return ResponseUtil.wrapOrNotFound(animalSalud);
    }

    /**
     * {@code DELETE  /animal-saluds/:id} : delete the "id" animalSalud.
     *
     * @param id the id of the animalSalud to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/animal-saluds/{id}")
    public ResponseEntity<Void> deleteAnimalSalud(@PathVariable Long id) {
        log.debug("REST request to delete AnimalSalud : {}", id);
        animalSaludService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/animal-saluds?query=:query} : search for the animalSalud corresponding
     * to the query.
     *
     * @param query the query of the animalSalud search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/animal-saluds")
    public ResponseEntity<List<AnimalSalud>> searchAnimalSaluds(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnimalSaluds for query {}", query);
        Page<AnimalSalud> page = animalSaludService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
