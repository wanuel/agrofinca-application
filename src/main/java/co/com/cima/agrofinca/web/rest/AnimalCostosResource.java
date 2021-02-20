package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.domain.AnimalCostos;
import co.com.cima.agrofinca.service.AnimalCostosService;
import co.com.cima.agrofinca.web.rest.errors.BadRequestAlertException;
import co.com.cima.agrofinca.service.dto.AnimalCostosCriteria;
import co.com.cima.agrofinca.service.AnimalCostosQueryService;

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
 * REST controller for managing {@link co.com.cima.agrofinca.domain.AnimalCostos}.
 */
@RestController
@RequestMapping("/api")
public class AnimalCostosResource {

    private final Logger log = LoggerFactory.getLogger(AnimalCostosResource.class);

    private static final String ENTITY_NAME = "animalCostos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnimalCostosService animalCostosService;

    private final AnimalCostosQueryService animalCostosQueryService;

    public AnimalCostosResource(AnimalCostosService animalCostosService, AnimalCostosQueryService animalCostosQueryService) {
        this.animalCostosService = animalCostosService;
        this.animalCostosQueryService = animalCostosQueryService;
    }

    /**
     * {@code POST  /animal-costos} : Create a new animalCostos.
     *
     * @param animalCostos the animalCostos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new animalCostos, or with status {@code 400 (Bad Request)} if the animalCostos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/animal-costos")
    public ResponseEntity<AnimalCostos> createAnimalCostos(@Valid @RequestBody AnimalCostos animalCostos) throws URISyntaxException {
        log.debug("REST request to save AnimalCostos : {}", animalCostos);
        if (animalCostos.getId() != null) {
            throw new BadRequestAlertException("A new animalCostos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnimalCostos result = animalCostosService.save(animalCostos);
        return ResponseEntity.created(new URI("/api/animal-costos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /animal-costos} : Updates an existing animalCostos.
     *
     * @param animalCostos the animalCostos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated animalCostos,
     * or with status {@code 400 (Bad Request)} if the animalCostos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the animalCostos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/animal-costos")
    public ResponseEntity<AnimalCostos> updateAnimalCostos(@Valid @RequestBody AnimalCostos animalCostos) throws URISyntaxException {
        log.debug("REST request to update AnimalCostos : {}", animalCostos);
        if (animalCostos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnimalCostos result = animalCostosService.save(animalCostos);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, animalCostos.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /animal-costos} : get all the animalCostos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of animalCostos in body.
     */
    @GetMapping("/animal-costos")
    public ResponseEntity<List<AnimalCostos>> getAllAnimalCostos(AnimalCostosCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AnimalCostos by criteria: {}", criteria);
        Page<AnimalCostos> page = animalCostosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /animal-costos/count} : count all the animalCostos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/animal-costos/count")
    public ResponseEntity<Long> countAnimalCostos(AnimalCostosCriteria criteria) {
        log.debug("REST request to count AnimalCostos by criteria: {}", criteria);
        return ResponseEntity.ok().body(animalCostosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /animal-costos/:id} : get the "id" animalCostos.
     *
     * @param id the id of the animalCostos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the animalCostos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/animal-costos/{id}")
    public ResponseEntity<AnimalCostos> getAnimalCostos(@PathVariable Long id) {
        log.debug("REST request to get AnimalCostos : {}", id);
        Optional<AnimalCostos> animalCostos = animalCostosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(animalCostos);
    }

    /**
     * {@code DELETE  /animal-costos/:id} : delete the "id" animalCostos.
     *
     * @param id the id of the animalCostos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/animal-costos/{id}")
    public ResponseEntity<Void> deleteAnimalCostos(@PathVariable Long id) {
        log.debug("REST request to delete AnimalCostos : {}", id);
        animalCostosService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/animal-costos?query=:query} : search for the animalCostos corresponding
     * to the query.
     *
     * @param query the query of the animalCostos search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/animal-costos")
    public ResponseEntity<List<AnimalCostos>> searchAnimalCostos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnimalCostos for query {}", query);
        Page<AnimalCostos> page = animalCostosService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
