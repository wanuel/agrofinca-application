package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.domain.AnimalImagen;
import co.com.cima.agrofinca.service.AnimalImagenService;
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
 * REST controller for managing {@link co.com.cima.agrofinca.domain.AnimalImagen}.
 */
@RestController
@RequestMapping("/api")
public class AnimalImagenResource {

    private final Logger log = LoggerFactory.getLogger(AnimalImagenResource.class);

    private static final String ENTITY_NAME = "animalImagen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnimalImagenService animalImagenService;

    public AnimalImagenResource(AnimalImagenService animalImagenService) {
        this.animalImagenService = animalImagenService;
    }

    /**
     * {@code POST  /animal-imagens} : Create a new animalImagen.
     *
     * @param animalImagen the animalImagen to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new animalImagen, or with status {@code 400 (Bad Request)} if the animalImagen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/animal-imagens")
    public ResponseEntity<AnimalImagen> createAnimalImagen(@Valid @RequestBody AnimalImagen animalImagen) throws URISyntaxException {
        log.debug("REST request to save AnimalImagen : {}", animalImagen);
        if (animalImagen.getId() != null) {
            throw new BadRequestAlertException("A new animalImagen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnimalImagen result = animalImagenService.save(animalImagen);
        return ResponseEntity.created(new URI("/api/animal-imagens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /animal-imagens} : Updates an existing animalImagen.
     *
     * @param animalImagen the animalImagen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated animalImagen,
     * or with status {@code 400 (Bad Request)} if the animalImagen is not valid,
     * or with status {@code 500 (Internal Server Error)} if the animalImagen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/animal-imagens")
    public ResponseEntity<AnimalImagen> updateAnimalImagen(@Valid @RequestBody AnimalImagen animalImagen) throws URISyntaxException {
        log.debug("REST request to update AnimalImagen : {}", animalImagen);
        if (animalImagen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnimalImagen result = animalImagenService.save(animalImagen);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, animalImagen.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /animal-imagens} : get all the animalImagens.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of animalImagens in body.
     */
    @GetMapping("/animal-imagens")
    public ResponseEntity<List<AnimalImagen>> getAllAnimalImagens(Pageable pageable) {
        log.debug("REST request to get a page of AnimalImagens");
        Page<AnimalImagen> page = animalImagenService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /animal-imagens/:id} : get the "id" animalImagen.
     *
     * @param id the id of the animalImagen to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the animalImagen, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/animal-imagens/{id}")
    public ResponseEntity<AnimalImagen> getAnimalImagen(@PathVariable Long id) {
        log.debug("REST request to get AnimalImagen : {}", id);
        Optional<AnimalImagen> animalImagen = animalImagenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(animalImagen);
    }

    /**
     * {@code DELETE  /animal-imagens/:id} : delete the "id" animalImagen.
     *
     * @param id the id of the animalImagen to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/animal-imagens/{id}")
    public ResponseEntity<Void> deleteAnimalImagen(@PathVariable Long id) {
        log.debug("REST request to delete AnimalImagen : {}", id);
        animalImagenService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/animal-imagens?query=:query} : search for the animalImagen corresponding
     * to the query.
     *
     * @param query the query of the animalImagen search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/animal-imagens")
    public ResponseEntity<List<AnimalImagen>> searchAnimalImagens(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnimalImagens for query {}", query);
        Page<AnimalImagen> page = animalImagenService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
