package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.AnimalLote;
import co.com.cima.agrofinca.repository.AnimalLoteRepository;
import co.com.cima.agrofinca.repository.search.AnimalLoteSearchRepository;
import co.com.cima.agrofinca.service.AnimalLoteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AnimalLoteResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AnimalLoteResourceIT {

    private static final LocalDate DEFAULT_FECHA_INGRESO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INGRESO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_SALIDA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_SALIDA = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private AnimalLoteRepository animalLoteRepository;

    @Autowired
    private AnimalLoteService animalLoteService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.AnimalLoteSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnimalLoteSearchRepository mockAnimalLoteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnimalLoteMockMvc;

    private AnimalLote animalLote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalLote createEntity(EntityManager em) {
        AnimalLote animalLote = new AnimalLote()
            .fechaIngreso(DEFAULT_FECHA_INGRESO)
            .fechaSalida(DEFAULT_FECHA_SALIDA);
        return animalLote;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalLote createUpdatedEntity(EntityManager em) {
        AnimalLote animalLote = new AnimalLote()
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .fechaSalida(UPDATED_FECHA_SALIDA);
        return animalLote;
    }

    @BeforeEach
    public void initTest() {
        animalLote = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnimalLote() throws Exception {
        int databaseSizeBeforeCreate = animalLoteRepository.findAll().size();
        // Create the AnimalLote
        restAnimalLoteMockMvc.perform(post("/api/animal-lotes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalLote)))
            .andExpect(status().isCreated());

        // Validate the AnimalLote in the database
        List<AnimalLote> animalLoteList = animalLoteRepository.findAll();
        assertThat(animalLoteList).hasSize(databaseSizeBeforeCreate + 1);
        AnimalLote testAnimalLote = animalLoteList.get(animalLoteList.size() - 1);
        assertThat(testAnimalLote.getFechaIngreso()).isEqualTo(DEFAULT_FECHA_INGRESO);
        assertThat(testAnimalLote.getFechaSalida()).isEqualTo(DEFAULT_FECHA_SALIDA);

        // Validate the AnimalLote in Elasticsearch
        verify(mockAnimalLoteSearchRepository, times(1)).save(testAnimalLote);
    }

    @Test
    @Transactional
    public void createAnimalLoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = animalLoteRepository.findAll().size();

        // Create the AnimalLote with an existing ID
        animalLote.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnimalLoteMockMvc.perform(post("/api/animal-lotes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalLote)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalLote in the database
        List<AnimalLote> animalLoteList = animalLoteRepository.findAll();
        assertThat(animalLoteList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnimalLote in Elasticsearch
        verify(mockAnimalLoteSearchRepository, times(0)).save(animalLote);
    }


    @Test
    @Transactional
    public void checkFechaIngresoIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalLoteRepository.findAll().size();
        // set the field null
        animalLote.setFechaIngreso(null);

        // Create the AnimalLote, which fails.


        restAnimalLoteMockMvc.perform(post("/api/animal-lotes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalLote)))
            .andExpect(status().isBadRequest());

        List<AnimalLote> animalLoteList = animalLoteRepository.findAll();
        assertThat(animalLoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnimalLotes() throws Exception {
        // Initialize the database
        animalLoteRepository.saveAndFlush(animalLote);

        // Get all the animalLoteList
        restAnimalLoteMockMvc.perform(get("/api/animal-lotes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalLote.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].fechaSalida").value(hasItem(DEFAULT_FECHA_SALIDA.toString())));
    }
    
    @Test
    @Transactional
    public void getAnimalLote() throws Exception {
        // Initialize the database
        animalLoteRepository.saveAndFlush(animalLote);

        // Get the animalLote
        restAnimalLoteMockMvc.perform(get("/api/animal-lotes/{id}", animalLote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(animalLote.getId().intValue()))
            .andExpect(jsonPath("$.fechaIngreso").value(DEFAULT_FECHA_INGRESO.toString()))
            .andExpect(jsonPath("$.fechaSalida").value(DEFAULT_FECHA_SALIDA.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingAnimalLote() throws Exception {
        // Get the animalLote
        restAnimalLoteMockMvc.perform(get("/api/animal-lotes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnimalLote() throws Exception {
        // Initialize the database
        animalLoteService.save(animalLote);

        int databaseSizeBeforeUpdate = animalLoteRepository.findAll().size();

        // Update the animalLote
        AnimalLote updatedAnimalLote = animalLoteRepository.findById(animalLote.getId()).get();
        // Disconnect from session so that the updates on updatedAnimalLote are not directly saved in db
        em.detach(updatedAnimalLote);
        updatedAnimalLote
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .fechaSalida(UPDATED_FECHA_SALIDA);

        restAnimalLoteMockMvc.perform(put("/api/animal-lotes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnimalLote)))
            .andExpect(status().isOk());

        // Validate the AnimalLote in the database
        List<AnimalLote> animalLoteList = animalLoteRepository.findAll();
        assertThat(animalLoteList).hasSize(databaseSizeBeforeUpdate);
        AnimalLote testAnimalLote = animalLoteList.get(animalLoteList.size() - 1);
        assertThat(testAnimalLote.getFechaIngreso()).isEqualTo(UPDATED_FECHA_INGRESO);
        assertThat(testAnimalLote.getFechaSalida()).isEqualTo(UPDATED_FECHA_SALIDA);

        // Validate the AnimalLote in Elasticsearch
        verify(mockAnimalLoteSearchRepository, times(2)).save(testAnimalLote);
    }

    @Test
    @Transactional
    public void updateNonExistingAnimalLote() throws Exception {
        int databaseSizeBeforeUpdate = animalLoteRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnimalLoteMockMvc.perform(put("/api/animal-lotes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalLote)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalLote in the database
        List<AnimalLote> animalLoteList = animalLoteRepository.findAll();
        assertThat(animalLoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnimalLote in Elasticsearch
        verify(mockAnimalLoteSearchRepository, times(0)).save(animalLote);
    }

    @Test
    @Transactional
    public void deleteAnimalLote() throws Exception {
        // Initialize the database
        animalLoteService.save(animalLote);

        int databaseSizeBeforeDelete = animalLoteRepository.findAll().size();

        // Delete the animalLote
        restAnimalLoteMockMvc.perform(delete("/api/animal-lotes/{id}", animalLote.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnimalLote> animalLoteList = animalLoteRepository.findAll();
        assertThat(animalLoteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnimalLote in Elasticsearch
        verify(mockAnimalLoteSearchRepository, times(1)).deleteById(animalLote.getId());
    }

    @Test
    @Transactional
    public void searchAnimalLote() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        animalLoteService.save(animalLote);
        when(mockAnimalLoteSearchRepository.search(queryStringQuery("id:" + animalLote.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(animalLote), PageRequest.of(0, 1), 1));

        // Search the animalLote
        restAnimalLoteMockMvc.perform(get("/api/_search/animal-lotes?query=id:" + animalLote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalLote.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].fechaSalida").value(hasItem(DEFAULT_FECHA_SALIDA.toString())));
    }
}
