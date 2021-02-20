package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.AnimalCostos;
import co.com.cima.agrofinca.domain.AnimalEvento;
import co.com.cima.agrofinca.repository.AnimalCostosRepository;
import co.com.cima.agrofinca.repository.search.AnimalCostosSearchRepository;
import co.com.cima.agrofinca.service.AnimalCostosService;
import co.com.cima.agrofinca.service.dto.AnimalCostosCriteria;
import co.com.cima.agrofinca.service.AnimalCostosQueryService;

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
 * Integration tests for the {@link AnimalCostosResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AnimalCostosResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR = new BigDecimal(1 - 1);

    @Autowired
    private AnimalCostosRepository animalCostosRepository;

    @Autowired
    private AnimalCostosService animalCostosService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.AnimalCostosSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnimalCostosSearchRepository mockAnimalCostosSearchRepository;

    @Autowired
    private AnimalCostosQueryService animalCostosQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnimalCostosMockMvc;

    private AnimalCostos animalCostos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalCostos createEntity(EntityManager em) {
        AnimalCostos animalCostos = new AnimalCostos()
            .fecha(DEFAULT_FECHA)
            .valor(DEFAULT_VALOR);
        return animalCostos;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalCostos createUpdatedEntity(EntityManager em) {
        AnimalCostos animalCostos = new AnimalCostos()
            .fecha(UPDATED_FECHA)
            .valor(UPDATED_VALOR);
        return animalCostos;
    }

    @BeforeEach
    public void initTest() {
        animalCostos = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnimalCostos() throws Exception {
        int databaseSizeBeforeCreate = animalCostosRepository.findAll().size();
        // Create the AnimalCostos
        restAnimalCostosMockMvc.perform(post("/api/animal-costos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalCostos)))
            .andExpect(status().isCreated());

        // Validate the AnimalCostos in the database
        List<AnimalCostos> animalCostosList = animalCostosRepository.findAll();
        assertThat(animalCostosList).hasSize(databaseSizeBeforeCreate + 1);
        AnimalCostos testAnimalCostos = animalCostosList.get(animalCostosList.size() - 1);
        assertThat(testAnimalCostos.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testAnimalCostos.getValor()).isEqualTo(DEFAULT_VALOR);

        // Validate the AnimalCostos in Elasticsearch
        verify(mockAnimalCostosSearchRepository, times(1)).save(testAnimalCostos);
    }

    @Test
    @Transactional
    public void createAnimalCostosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = animalCostosRepository.findAll().size();

        // Create the AnimalCostos with an existing ID
        animalCostos.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnimalCostosMockMvc.perform(post("/api/animal-costos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalCostos)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalCostos in the database
        List<AnimalCostos> animalCostosList = animalCostosRepository.findAll();
        assertThat(animalCostosList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnimalCostos in Elasticsearch
        verify(mockAnimalCostosSearchRepository, times(0)).save(animalCostos);
    }


    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalCostosRepository.findAll().size();
        // set the field null
        animalCostos.setFecha(null);

        // Create the AnimalCostos, which fails.


        restAnimalCostosMockMvc.perform(post("/api/animal-costos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalCostos)))
            .andExpect(status().isBadRequest());

        List<AnimalCostos> animalCostosList = animalCostosRepository.findAll();
        assertThat(animalCostosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalCostosRepository.findAll().size();
        // set the field null
        animalCostos.setValor(null);

        // Create the AnimalCostos, which fails.


        restAnimalCostosMockMvc.perform(post("/api/animal-costos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalCostos)))
            .andExpect(status().isBadRequest());

        List<AnimalCostos> animalCostosList = animalCostosRepository.findAll();
        assertThat(animalCostosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnimalCostos() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList
        restAnimalCostosMockMvc.perform(get("/api/animal-costos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalCostos.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())));
    }
    
    @Test
    @Transactional
    public void getAnimalCostos() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get the animalCostos
        restAnimalCostosMockMvc.perform(get("/api/animal-costos/{id}", animalCostos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(animalCostos.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.intValue()));
    }


    @Test
    @Transactional
    public void getAnimalCostosByIdFiltering() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        Long id = animalCostos.getId();

        defaultAnimalCostosShouldBeFound("id.equals=" + id);
        defaultAnimalCostosShouldNotBeFound("id.notEquals=" + id);

        defaultAnimalCostosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnimalCostosShouldNotBeFound("id.greaterThan=" + id);

        defaultAnimalCostosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnimalCostosShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAnimalCostosByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where fecha equals to DEFAULT_FECHA
        defaultAnimalCostosShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the animalCostosList where fecha equals to UPDATED_FECHA
        defaultAnimalCostosShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByFechaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where fecha not equals to DEFAULT_FECHA
        defaultAnimalCostosShouldNotBeFound("fecha.notEquals=" + DEFAULT_FECHA);

        // Get all the animalCostosList where fecha not equals to UPDATED_FECHA
        defaultAnimalCostosShouldBeFound("fecha.notEquals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultAnimalCostosShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the animalCostosList where fecha equals to UPDATED_FECHA
        defaultAnimalCostosShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where fecha is not null
        defaultAnimalCostosShouldBeFound("fecha.specified=true");

        // Get all the animalCostosList where fecha is null
        defaultAnimalCostosShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where fecha is greater than or equal to DEFAULT_FECHA
        defaultAnimalCostosShouldBeFound("fecha.greaterThanOrEqual=" + DEFAULT_FECHA);

        // Get all the animalCostosList where fecha is greater than or equal to UPDATED_FECHA
        defaultAnimalCostosShouldNotBeFound("fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where fecha is less than or equal to DEFAULT_FECHA
        defaultAnimalCostosShouldBeFound("fecha.lessThanOrEqual=" + DEFAULT_FECHA);

        // Get all the animalCostosList where fecha is less than or equal to SMALLER_FECHA
        defaultAnimalCostosShouldNotBeFound("fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where fecha is less than DEFAULT_FECHA
        defaultAnimalCostosShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the animalCostosList where fecha is less than UPDATED_FECHA
        defaultAnimalCostosShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where fecha is greater than DEFAULT_FECHA
        defaultAnimalCostosShouldNotBeFound("fecha.greaterThan=" + DEFAULT_FECHA);

        // Get all the animalCostosList where fecha is greater than SMALLER_FECHA
        defaultAnimalCostosShouldBeFound("fecha.greaterThan=" + SMALLER_FECHA);
    }


    @Test
    @Transactional
    public void getAllAnimalCostosByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where valor equals to DEFAULT_VALOR
        defaultAnimalCostosShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the animalCostosList where valor equals to UPDATED_VALOR
        defaultAnimalCostosShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByValorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where valor not equals to DEFAULT_VALOR
        defaultAnimalCostosShouldNotBeFound("valor.notEquals=" + DEFAULT_VALOR);

        // Get all the animalCostosList where valor not equals to UPDATED_VALOR
        defaultAnimalCostosShouldBeFound("valor.notEquals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByValorIsInShouldWork() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultAnimalCostosShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the animalCostosList where valor equals to UPDATED_VALOR
        defaultAnimalCostosShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where valor is not null
        defaultAnimalCostosShouldBeFound("valor.specified=true");

        // Get all the animalCostosList where valor is null
        defaultAnimalCostosShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where valor is greater than or equal to DEFAULT_VALOR
        defaultAnimalCostosShouldBeFound("valor.greaterThanOrEqual=" + DEFAULT_VALOR);

        // Get all the animalCostosList where valor is greater than or equal to UPDATED_VALOR
        defaultAnimalCostosShouldNotBeFound("valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where valor is less than or equal to DEFAULT_VALOR
        defaultAnimalCostosShouldBeFound("valor.lessThanOrEqual=" + DEFAULT_VALOR);

        // Get all the animalCostosList where valor is less than or equal to SMALLER_VALOR
        defaultAnimalCostosShouldNotBeFound("valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where valor is less than DEFAULT_VALOR
        defaultAnimalCostosShouldNotBeFound("valor.lessThan=" + DEFAULT_VALOR);

        // Get all the animalCostosList where valor is less than UPDATED_VALOR
        defaultAnimalCostosShouldBeFound("valor.lessThan=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllAnimalCostosByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);

        // Get all the animalCostosList where valor is greater than DEFAULT_VALOR
        defaultAnimalCostosShouldNotBeFound("valor.greaterThan=" + DEFAULT_VALOR);

        // Get all the animalCostosList where valor is greater than SMALLER_VALOR
        defaultAnimalCostosShouldBeFound("valor.greaterThan=" + SMALLER_VALOR);
    }


    @Test
    @Transactional
    public void getAllAnimalCostosByAnimalIsEqualToSomething() throws Exception {
        // Initialize the database
        animalCostosRepository.saveAndFlush(animalCostos);
        AnimalEvento animal = AnimalEventoResourceIT.createEntity(em);
        em.persist(animal);
        em.flush();
        animalCostos.setAnimal(animal);
        animalCostosRepository.saveAndFlush(animalCostos);
        Long animalId = animal.getId();

        // Get all the animalCostosList where animal equals to animalId
        defaultAnimalCostosShouldBeFound("animalId.equals=" + animalId);

        // Get all the animalCostosList where animal equals to animalId + 1
        defaultAnimalCostosShouldNotBeFound("animalId.equals=" + (animalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnimalCostosShouldBeFound(String filter) throws Exception {
        restAnimalCostosMockMvc.perform(get("/api/animal-costos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalCostos.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())));

        // Check, that the count call also returns 1
        restAnimalCostosMockMvc.perform(get("/api/animal-costos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnimalCostosShouldNotBeFound(String filter) throws Exception {
        restAnimalCostosMockMvc.perform(get("/api/animal-costos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnimalCostosMockMvc.perform(get("/api/animal-costos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAnimalCostos() throws Exception {
        // Get the animalCostos
        restAnimalCostosMockMvc.perform(get("/api/animal-costos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnimalCostos() throws Exception {
        // Initialize the database
        animalCostosService.save(animalCostos);

        int databaseSizeBeforeUpdate = animalCostosRepository.findAll().size();

        // Update the animalCostos
        AnimalCostos updatedAnimalCostos = animalCostosRepository.findById(animalCostos.getId()).get();
        // Disconnect from session so that the updates on updatedAnimalCostos are not directly saved in db
        em.detach(updatedAnimalCostos);
        updatedAnimalCostos
            .fecha(UPDATED_FECHA)
            .valor(UPDATED_VALOR);

        restAnimalCostosMockMvc.perform(put("/api/animal-costos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnimalCostos)))
            .andExpect(status().isOk());

        // Validate the AnimalCostos in the database
        List<AnimalCostos> animalCostosList = animalCostosRepository.findAll();
        assertThat(animalCostosList).hasSize(databaseSizeBeforeUpdate);
        AnimalCostos testAnimalCostos = animalCostosList.get(animalCostosList.size() - 1);
        assertThat(testAnimalCostos.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testAnimalCostos.getValor()).isEqualTo(UPDATED_VALOR);

        // Validate the AnimalCostos in Elasticsearch
        verify(mockAnimalCostosSearchRepository, times(2)).save(testAnimalCostos);
    }

    @Test
    @Transactional
    public void updateNonExistingAnimalCostos() throws Exception {
        int databaseSizeBeforeUpdate = animalCostosRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnimalCostosMockMvc.perform(put("/api/animal-costos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalCostos)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalCostos in the database
        List<AnimalCostos> animalCostosList = animalCostosRepository.findAll();
        assertThat(animalCostosList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnimalCostos in Elasticsearch
        verify(mockAnimalCostosSearchRepository, times(0)).save(animalCostos);
    }

    @Test
    @Transactional
    public void deleteAnimalCostos() throws Exception {
        // Initialize the database
        animalCostosService.save(animalCostos);

        int databaseSizeBeforeDelete = animalCostosRepository.findAll().size();

        // Delete the animalCostos
        restAnimalCostosMockMvc.perform(delete("/api/animal-costos/{id}", animalCostos.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnimalCostos> animalCostosList = animalCostosRepository.findAll();
        assertThat(animalCostosList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnimalCostos in Elasticsearch
        verify(mockAnimalCostosSearchRepository, times(1)).deleteById(animalCostos.getId());
    }

    @Test
    @Transactional
    public void searchAnimalCostos() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        animalCostosService.save(animalCostos);
        when(mockAnimalCostosSearchRepository.search(queryStringQuery("id:" + animalCostos.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(animalCostos), PageRequest.of(0, 1), 1));

        // Search the animalCostos
        restAnimalCostosMockMvc.perform(get("/api/_search/animal-costos?query=id:" + animalCostos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalCostos.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())));
    }
}
