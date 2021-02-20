package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.AnimalPeso;
import co.com.cima.agrofinca.domain.AnimalEvento;
import co.com.cima.agrofinca.repository.AnimalPesoRepository;
import co.com.cima.agrofinca.repository.search.AnimalPesoSearchRepository;
import co.com.cima.agrofinca.service.AnimalPesoService;
import co.com.cima.agrofinca.service.dto.AnimalPesoCriteria;
import co.com.cima.agrofinca.service.AnimalPesoQueryService;

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
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_PESO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PESO = new BigDecimal(2);
    private static final BigDecimal SMALLER_PESO = new BigDecimal(1 - 1);

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
    private AnimalPesoQueryService animalPesoQueryService;

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
    public void getAnimalPesosByIdFiltering() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        Long id = animalPeso.getId();

        defaultAnimalPesoShouldBeFound("id.equals=" + id);
        defaultAnimalPesoShouldNotBeFound("id.notEquals=" + id);

        defaultAnimalPesoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnimalPesoShouldNotBeFound("id.greaterThan=" + id);

        defaultAnimalPesoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnimalPesoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAnimalPesosByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where fecha equals to DEFAULT_FECHA
        defaultAnimalPesoShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the animalPesoList where fecha equals to UPDATED_FECHA
        defaultAnimalPesoShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByFechaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where fecha not equals to DEFAULT_FECHA
        defaultAnimalPesoShouldNotBeFound("fecha.notEquals=" + DEFAULT_FECHA);

        // Get all the animalPesoList where fecha not equals to UPDATED_FECHA
        defaultAnimalPesoShouldBeFound("fecha.notEquals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultAnimalPesoShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the animalPesoList where fecha equals to UPDATED_FECHA
        defaultAnimalPesoShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where fecha is not null
        defaultAnimalPesoShouldBeFound("fecha.specified=true");

        // Get all the animalPesoList where fecha is null
        defaultAnimalPesoShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where fecha is greater than or equal to DEFAULT_FECHA
        defaultAnimalPesoShouldBeFound("fecha.greaterThanOrEqual=" + DEFAULT_FECHA);

        // Get all the animalPesoList where fecha is greater than or equal to UPDATED_FECHA
        defaultAnimalPesoShouldNotBeFound("fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where fecha is less than or equal to DEFAULT_FECHA
        defaultAnimalPesoShouldBeFound("fecha.lessThanOrEqual=" + DEFAULT_FECHA);

        // Get all the animalPesoList where fecha is less than or equal to SMALLER_FECHA
        defaultAnimalPesoShouldNotBeFound("fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where fecha is less than DEFAULT_FECHA
        defaultAnimalPesoShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the animalPesoList where fecha is less than UPDATED_FECHA
        defaultAnimalPesoShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where fecha is greater than DEFAULT_FECHA
        defaultAnimalPesoShouldNotBeFound("fecha.greaterThan=" + DEFAULT_FECHA);

        // Get all the animalPesoList where fecha is greater than SMALLER_FECHA
        defaultAnimalPesoShouldBeFound("fecha.greaterThan=" + SMALLER_FECHA);
    }


    @Test
    @Transactional
    public void getAllAnimalPesosByPesoIsEqualToSomething() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where peso equals to DEFAULT_PESO
        defaultAnimalPesoShouldBeFound("peso.equals=" + DEFAULT_PESO);

        // Get all the animalPesoList where peso equals to UPDATED_PESO
        defaultAnimalPesoShouldNotBeFound("peso.equals=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByPesoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where peso not equals to DEFAULT_PESO
        defaultAnimalPesoShouldNotBeFound("peso.notEquals=" + DEFAULT_PESO);

        // Get all the animalPesoList where peso not equals to UPDATED_PESO
        defaultAnimalPesoShouldBeFound("peso.notEquals=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByPesoIsInShouldWork() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where peso in DEFAULT_PESO or UPDATED_PESO
        defaultAnimalPesoShouldBeFound("peso.in=" + DEFAULT_PESO + "," + UPDATED_PESO);

        // Get all the animalPesoList where peso equals to UPDATED_PESO
        defaultAnimalPesoShouldNotBeFound("peso.in=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByPesoIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where peso is not null
        defaultAnimalPesoShouldBeFound("peso.specified=true");

        // Get all the animalPesoList where peso is null
        defaultAnimalPesoShouldNotBeFound("peso.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByPesoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where peso is greater than or equal to DEFAULT_PESO
        defaultAnimalPesoShouldBeFound("peso.greaterThanOrEqual=" + DEFAULT_PESO);

        // Get all the animalPesoList where peso is greater than or equal to UPDATED_PESO
        defaultAnimalPesoShouldNotBeFound("peso.greaterThanOrEqual=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByPesoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where peso is less than or equal to DEFAULT_PESO
        defaultAnimalPesoShouldBeFound("peso.lessThanOrEqual=" + DEFAULT_PESO);

        // Get all the animalPesoList where peso is less than or equal to SMALLER_PESO
        defaultAnimalPesoShouldNotBeFound("peso.lessThanOrEqual=" + SMALLER_PESO);
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByPesoIsLessThanSomething() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where peso is less than DEFAULT_PESO
        defaultAnimalPesoShouldNotBeFound("peso.lessThan=" + DEFAULT_PESO);

        // Get all the animalPesoList where peso is less than UPDATED_PESO
        defaultAnimalPesoShouldBeFound("peso.lessThan=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    public void getAllAnimalPesosByPesoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);

        // Get all the animalPesoList where peso is greater than DEFAULT_PESO
        defaultAnimalPesoShouldNotBeFound("peso.greaterThan=" + DEFAULT_PESO);

        // Get all the animalPesoList where peso is greater than SMALLER_PESO
        defaultAnimalPesoShouldBeFound("peso.greaterThan=" + SMALLER_PESO);
    }


    @Test
    @Transactional
    public void getAllAnimalPesosByAnimalIsEqualToSomething() throws Exception {
        // Initialize the database
        animalPesoRepository.saveAndFlush(animalPeso);
        AnimalEvento animal = AnimalEventoResourceIT.createEntity(em);
        em.persist(animal);
        em.flush();
        animalPeso.setAnimal(animal);
        animalPesoRepository.saveAndFlush(animalPeso);
        Long animalId = animal.getId();

        // Get all the animalPesoList where animal equals to animalId
        defaultAnimalPesoShouldBeFound("animalId.equals=" + animalId);

        // Get all the animalPesoList where animal equals to animalId + 1
        defaultAnimalPesoShouldNotBeFound("animalId.equals=" + (animalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnimalPesoShouldBeFound(String filter) throws Exception {
        restAnimalPesoMockMvc.perform(get("/api/animal-pesos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalPeso.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.intValue())));

        // Check, that the count call also returns 1
        restAnimalPesoMockMvc.perform(get("/api/animal-pesos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnimalPesoShouldNotBeFound(String filter) throws Exception {
        restAnimalPesoMockMvc.perform(get("/api/animal-pesos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnimalPesoMockMvc.perform(get("/api/animal-pesos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
