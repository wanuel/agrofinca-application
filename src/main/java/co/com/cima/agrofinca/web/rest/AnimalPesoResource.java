package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.domain.AnimalPeso;
import co.com.cima.agrofinca.service.AnimalPesoService;
import co.com.cima.agrofinca.web.rest.errors.BadRequestAlertException;
import co.com.cima.agrofinca.service.dto.AnimalPesoCriteria;
import co.com.cima.agrofinca.service.AnimalPesoQueryService;

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
 * REST controller for managing {@link co.com.cima.agrofinca.domain.AnimalPeso}.
 */
@RestController
@RequestMapping("/api")
public class AnimalPesoResource {

    private final Logger log = LoggerFactory.getLogger(AnimalPesoResource.class);

    private static final String ENTITY_NAME = "animalPeso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnimalPesoService animalPesoService;

    private final AnimalPesoQueryService animalPesoQueryService;

    public AnimalPesoResource(AnimalPesoService animalPesoService, AnimalPesoQueryService animalPesoQueryService) {
        this.animalPesoService = animalPesoService;
        this.animalPesoQueryService = animalPesoQueryService;
    }

    /**
     * {@code POST  /animal-pesos} : Create a new animalPeso.
     *
     * @param animalPeso the animalPeso to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new animalPeso, or with status {@code 400 (Bad Request)} if the animalPeso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/animal-pesos")
    public ResponseEntity<AnimalPeso> createAnimalPeso(@Valid @RequestBody AnimalPeso animalPeso) throws URISyntaxException {
        log.debug("REST request to save AnimalPeso : {}", animalPeso);
        if (animalPeso.getId() != null) {
            throw new BadRequestAlertException("A new animalPeso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnimalPeso result = animalPesoService.save(animalPeso);
        return ResponseEntity.created(new URI("/api/animal-pesos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /animal-pesos} : Updates an existing animalPeso.
     *
     * @param animalPeso the animalPeso to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated animalPeso,
     * or with status {@code 400 (Bad Request)} if the animalPeso is not valid,
     * or with status {@code 500 (Internal Server Error)} if the animalPeso couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/animal-pesos")
    public ResponseEntity<AnimalPeso> updateAnimalPeso(@Valid @RequestBody AnimalPeso animalPeso) throws URISyntaxException {
        log.debug("REST request to update AnimalPeso : {}", animalPeso);
        if (animalPeso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnimalPeso result = animalPesoService.save(animalPeso);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, animalPeso.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /animal-pesos} : get all the animalPesos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of animalPesos in body.
     */
    @GetMapping("/animal-pesos")
    public ResponseEntity<List<AnimalPeso>> getAllAnimalPesos(AnimalPesoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AnimalPesos by criteria: {}", criteria);
        Page<AnimalPeso> page = animalPesoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /animal-pesos/count} : count all the animalPesos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/animal-pesos/count")
    public ResponseEntity<Long> countAnimalPesos(AnimalPesoCriteria criteria) {
        log.debug("REST request to count AnimalPesos by criteria: {}", criteria);
        return ResponseEntity.ok().body(animalPesoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /animal-pesos/:id} : get the "id" animalPeso.
     *
     * @param id the id of the animalPeso to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the animalPeso, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/animal-pesos/{id}")
    public ResponseEntity<AnimalPeso> getAnimalPeso(@PathVariable Long id) {
        log.debug("REST request to get AnimalPeso : {}", id);
        Optional<AnimalPeso> animalPeso = animalPesoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(animalPeso);
    }

    /**
     * {@code DELETE  /animal-pesos/:id} : delete the "id" animalPeso.
     *
     * @param id the id of the animalPeso to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/animal-pesos/{id}")
    public ResponseEntity<Void> deleteAnimalPeso(@PathVariable Long id) {
        log.debug("REST request to delete AnimalPeso : {}", id);
        animalPesoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/animal-pesos?query=:query} : search for the animalPeso corresponding
     * to the query.
     *
     * @param query the query of the animalPeso search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/animal-pesos")
    public ResponseEntity<List<AnimalPeso>> searchAnimalPesos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnimalPesos for query {}", query);
        Page<AnimalPeso> page = animalPesoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
