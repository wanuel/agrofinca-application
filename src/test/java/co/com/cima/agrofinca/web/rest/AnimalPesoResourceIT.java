package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.AnimalPeso;
import co.com.cima.agrofinca.repository.AnimalPesoRepository;
import co.com.cima.agrofinca.repository.search.AnimalPesoSearchRepository;
import co.com.cima.agrofinca.service.AnimalPesoService;

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
import java.math.BigDecimal;
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
 * Integration tests for the {@link AnimalPesoResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AnimalPesoResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_PESO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PESO = new BigDecimal(2);

    @Autowired
    private AnimalPesoRepository animalPesoRepository;

    @Autowired
    private AnimalPesoService animalPesoService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.AnimalPesoSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnimalPesoSearchRepository mockAnimalPesoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnimalPesoMockMvc;

    private AnimalPeso animalPeso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalPeso createEntity(EntityManager em) {
        AnimalPeso animalPeso = new AnimalPeso()
            .fecha(DEFAULT_FECHA)
            .peso(DEFAULT_PESO);
        return animalPeso;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalPeso createUpdatedEntity(EntityManager em) {
        AnimalPeso animalPeso = new AnimalPeso()
            .fecha(UPDATED_FECHA)
            .peso(UPDATED_PESO);
        return animalPeso;
    }

    @BeforeEach
    public void initTest() {
        animalPeso = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnimalPeso() throws Exception {
        int databaseSizeBeforeCreate = animalPesoRepository.findAll().size();
        // Create the AnimalPeso
        restAnimalPesoMockMvc.perform(post("/api/animal-pesos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalPeso)))
            .andExpect(status().isCreated());

        // Validate the AnimalPeso in the database
        List<AnimalPeso> animalPesoList = animalPesoRepository.findAll();
        assertThat(animalPesoList).hasSize(databaseSizeBeforeCreate + 1);
        AnimalPeso testAnimalPeso = animalPesoList.get(animalPesoList.size() - 1);
        assertThat(testAnimalPeso.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testAnimalPeso.getPeso()).isEqualTo(DEFAULT_PESO);

        // Validate the AnimalPeso in Elasticsearch
        verify(mockAnimalPesoSearchRepository, times(1)).save(testAnimalPeso);
    }

    @Test
    @Transactional
    public void createAnimalPesoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = animalPesoRepository.findAll().size();

        // Create the AnimalPeso with an existing ID
        animalPeso.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnimalPesoMockMvc.perform(post("/api/animal-pesos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalPeso)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalPeso in the database
        List<AnimalPeso> animalPesoList = animalPesoRepository.findAll();
        assertThat(animalPesoList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnimalPeso in Elasticsearch
        verify(mockAnimalPesoSearchRepository, times(0)).save(animalPeso);
    }


    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalPesoRepository.findAll().size();
        // set the field null
        animalPeso.setFecha(null);

        // Create the AnimalPeso, which fails.


        restAnimalPesoMockMvc.perform(post("/api/animal-pesos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalPeso)))
            .andExpect(status().isBadRequest());

        List<AnimalPeso> animalPesoList = animalPesoRepository.findAll();
        assertThat(animalPesoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPesoIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalPesoRepository.findAll().size();
        // set the field null
        animalPeso.setPeso(null);

        // Create the AnimalPeso, which fails.


        restAnimalPesoMockMvc.perform(post("/api/animal-pesos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalPeso)))
            .andExpect(status().isBadRequest());

        List<AnimalPeso> animalPesoList = animalPesoRepository.findAll();
        assertThat(animalPesoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnimalPesos() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList
        restAnimalPesoMockMvc.perform(get("/api/animal-pesos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalPeso.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.intValue())));
    }
    
    @Test
    @Transactional
    public void getAnimalPeso() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get the animalPeso
        restAnimalPesoMockMvc.perform(get("/api/animal-pesos/{id}", animalPeso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(animalPeso.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.peso").value(DEFAULT_PESO.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingAnimalPeso() throws Exception {
        // Get the animalPeso
        restAnimalPesoMockMvc.perform(get("/api/animal-pesos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnimalPeso() throws Exception {
        // Initialize the database
        animalPesoService.save(animalPeso);

        int databaseSizeBeforeUpdate = animalPesoRepository.findAll().size();

        // Update the animalPeso
        AnimalPeso updatedAnimalPeso = animalPesoRepository.findById(animalPeso.getId()).get();
        // Disconnect from session so that the updates on updatedAnimalPeso are not directly saved in db
        em.detach(updatedAnimalPeso);
        updatedAnimalPeso
            .fecha(UPDATED_FECHA)
            .peso(UPDATED_PESO);

        restAnimalPesoMockMvc.perform(put("/api/animal-pesos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnimalPeso)))
            .andExpect(status().isOk());

        // Validate the AnimalPeso in the database
        List<AnimalPeso> animalPesoList = animalPesoRepository.findAll();
        assertThat(animalPesoList).hasSize(databaseSizeBeforeUpdate);
        AnimalPeso testAnimalPeso = animalPesoList.get(animalPesoList.size() - 1);
        assertThat(testAnimalPeso.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testAnimalPeso.getPeso()).isEqualTo(UPDATED_PESO);

        // Validate the AnimalPeso in Elasticsearch
        verify(mockAnimalPesoSearchRepository, times(2)).save(testAnimalPeso);
    }

    @Test
    @Transactional
    public void updateNonExistingAnimalPeso() throws Exception {
        int databaseSizeBeforeUpdate = animalPesoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnimalPesoMockMvc.perform(put("/api/animal-pesos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalPeso)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalPeso in the database
        List<AnimalPeso> animalPesoList = animalPesoRepository.findAll();
        assertThat(animalPesoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnimalPeso in Elasticsearch
        verify(mockAnimalPesoSearchRepository, times(0)).save(animalPeso);
    }

    @Test
    @Transactional
    public void deleteAnimalPeso() throws Exception {
        // Initialize the database
        animalPesoService.save(animalPeso);

        int databaseSizeBeforeDelete = animalPesoRepository.findAll().size();

        // Delete the animalPeso
        restAnimalPesoMockMvc.perform(delete("/api/animal-pesos/{id}", animalPeso.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnimalPeso> animalPesoList = animalPesoRepository.findAll();
        assertThat(animalPesoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnimalPeso in Elasticsearch
        verify(mockAnimalPesoSearchRepository, times(1)).deleteById(animalPeso.getId());
    }

    @Test
    @Transactional
    public void searchAnimalPeso() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        animalPesoService.save(animalPeso);
        when(mockAnimalPesoSearchRepository.search(queryStringQuery("id:" + animalPeso.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(animalPeso), PageRequest.of(0, 1), 1));

        // Search the animalPeso
        restAnimalPesoMockMvc.perform(get("/api/_search/animal-pesos?query=id:" + animalPeso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalPeso.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.intValue())));
    }
}
