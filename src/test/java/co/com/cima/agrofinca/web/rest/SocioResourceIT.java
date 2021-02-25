package co.com.cima.agrofinca.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Persona;
import co.com.cima.agrofinca.domain.Sociedad;
import co.com.cima.agrofinca.domain.Socio;
import co.com.cima.agrofinca.repository.SocioRepository;
import co.com.cima.agrofinca.repository.search.SocioSearchRepository;
import co.com.cima.agrofinca.service.SocioQueryService;
import co.com.cima.agrofinca.service.SocioService;
import co.com.cima.agrofinca.service.dto.SocioCriteria;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

/**
 * Integration tests for the {@link SocioResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class SocioResourceIT {
    private static final LocalDate DEFAULT_FECHA_INGRESO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INGRESO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_INGRESO = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_PARTICIPACION = new BigDecimal(1);
    private static final BigDecimal UPDATED_PARTICIPACION = new BigDecimal(2);
    private static final BigDecimal SMALLER_PARTICIPACION = new BigDecimal(1 - 1);

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private SocioService socioService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.SocioSearchRepositoryMockConfiguration
     */
    @Autowired
    private SocioSearchRepository mockSocioSearchRepository;

    @Autowired
    private SocioQueryService socioQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSocioMockMvc;

    private Socio socio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Socio createEntity(EntityManager em) {
        Socio socio = new Socio().fechaIngreso(DEFAULT_FECHA_INGRESO).participacion(DEFAULT_PARTICIPACION);
        return socio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Socio createUpdatedEntity(EntityManager em) {
        Socio socio = new Socio().fechaIngreso(UPDATED_FECHA_INGRESO).participacion(UPDATED_PARTICIPACION);
        return socio;
    }

    @BeforeEach
    public void initTest() {
        socio = createEntity(em);
    }

    @Test
    @Transactional
    public void createSocio() throws Exception {
        int databaseSizeBeforeCreate = socioRepository.findAll().size();
        // Create the Socio
        restSocioMockMvc
            .perform(
                post("/api/socios").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socio))
            )
            .andExpect(status().isCreated());

        // Validate the Socio in the database
        List<Socio> socioList = socioRepository.findAll();
        assertThat(socioList).hasSize(databaseSizeBeforeCreate + 1);
        Socio testSocio = socioList.get(socioList.size() - 1);
        assertThat(testSocio.getFechaIngreso()).isEqualTo(DEFAULT_FECHA_INGRESO);
        assertThat(testSocio.getParticipacion()).isEqualTo(DEFAULT_PARTICIPACION);

        // Validate the Socio in Elasticsearch
        verify(mockSocioSearchRepository, times(1)).save(testSocio);
    }

    @Test
    @Transactional
    public void createSocioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = socioRepository.findAll().size();

        // Create the Socio with an existing ID
        socio.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocioMockMvc
            .perform(
                post("/api/socios").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Socio in the database
        List<Socio> socioList = socioRepository.findAll();
        assertThat(socioList).hasSize(databaseSizeBeforeCreate);

        // Validate the Socio in Elasticsearch
        verify(mockSocioSearchRepository, times(0)).save(socio);
    }

    @Test
    @Transactional
    public void checkFechaIngresoIsRequired() throws Exception {
        int databaseSizeBeforeTest = socioRepository.findAll().size();
        // set the field null
        socio.setFechaIngreso(null);

        // Create the Socio, which fails.

        restSocioMockMvc
            .perform(
                post("/api/socios").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socio))
            )
            .andExpect(status().isBadRequest());

        List<Socio> socioList = socioRepository.findAll();
        assertThat(socioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkParticipacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = socioRepository.findAll().size();
        // set the field null
        socio.setParticipacion(null);

        // Create the Socio, which fails.

        restSocioMockMvc
            .perform(
                post("/api/socios").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socio))
            )
            .andExpect(status().isBadRequest());

        List<Socio> socioList = socioRepository.findAll();
        assertThat(socioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSocios() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList
        restSocioMockMvc
            .perform(get("/api/socios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socio.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].participacion").value(hasItem(DEFAULT_PARTICIPACION.intValue())));
    }

    @Test
    @Transactional
    public void getSocio() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get the socio
        restSocioMockMvc
            .perform(get("/api/socios/{id}", socio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(socio.getId().intValue()))
            .andExpect(jsonPath("$.fechaIngreso").value(DEFAULT_FECHA_INGRESO.toString()))
            .andExpect(jsonPath("$.participacion").value(DEFAULT_PARTICIPACION.intValue()));
    }

    @Test
    @Transactional
    public void getSociosByIdFiltering() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        Long id = socio.getId();

        defaultSocioShouldBeFound("id.equals=" + id);
        defaultSocioShouldNotBeFound("id.notEquals=" + id);

        defaultSocioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSocioShouldNotBeFound("id.greaterThan=" + id);

        defaultSocioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSocioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllSociosByFechaIngresoIsEqualToSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where fechaIngreso equals to DEFAULT_FECHA_INGRESO
        defaultSocioShouldBeFound("fechaIngreso.equals=" + DEFAULT_FECHA_INGRESO);

        // Get all the socioList where fechaIngreso equals to UPDATED_FECHA_INGRESO
        defaultSocioShouldNotBeFound("fechaIngreso.equals=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void getAllSociosByFechaIngresoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where fechaIngreso not equals to DEFAULT_FECHA_INGRESO
        defaultSocioShouldNotBeFound("fechaIngreso.notEquals=" + DEFAULT_FECHA_INGRESO);

        // Get all the socioList where fechaIngreso not equals to UPDATED_FECHA_INGRESO
        defaultSocioShouldBeFound("fechaIngreso.notEquals=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void getAllSociosByFechaIngresoIsInShouldWork() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where fechaIngreso in DEFAULT_FECHA_INGRESO or UPDATED_FECHA_INGRESO
        defaultSocioShouldBeFound("fechaIngreso.in=" + DEFAULT_FECHA_INGRESO + "," + UPDATED_FECHA_INGRESO);

        // Get all the socioList where fechaIngreso equals to UPDATED_FECHA_INGRESO
        defaultSocioShouldNotBeFound("fechaIngreso.in=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void getAllSociosByFechaIngresoIsNullOrNotNull() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where fechaIngreso is not null
        defaultSocioShouldBeFound("fechaIngreso.specified=true");

        // Get all the socioList where fechaIngreso is null
        defaultSocioShouldNotBeFound("fechaIngreso.specified=false");
    }

    @Test
    @Transactional
    public void getAllSociosByFechaIngresoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where fechaIngreso is greater than or equal to DEFAULT_FECHA_INGRESO
        defaultSocioShouldBeFound("fechaIngreso.greaterThanOrEqual=" + DEFAULT_FECHA_INGRESO);

        // Get all the socioList where fechaIngreso is greater than or equal to UPDATED_FECHA_INGRESO
        defaultSocioShouldNotBeFound("fechaIngreso.greaterThanOrEqual=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void getAllSociosByFechaIngresoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where fechaIngreso is less than or equal to DEFAULT_FECHA_INGRESO
        defaultSocioShouldBeFound("fechaIngreso.lessThanOrEqual=" + DEFAULT_FECHA_INGRESO);

        // Get all the socioList where fechaIngreso is less than or equal to SMALLER_FECHA_INGRESO
        defaultSocioShouldNotBeFound("fechaIngreso.lessThanOrEqual=" + SMALLER_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void getAllSociosByFechaIngresoIsLessThanSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where fechaIngreso is less than DEFAULT_FECHA_INGRESO
        defaultSocioShouldNotBeFound("fechaIngreso.lessThan=" + DEFAULT_FECHA_INGRESO);

        // Get all the socioList where fechaIngreso is less than UPDATED_FECHA_INGRESO
        defaultSocioShouldBeFound("fechaIngreso.lessThan=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void getAllSociosByFechaIngresoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where fechaIngreso is greater than DEFAULT_FECHA_INGRESO
        defaultSocioShouldNotBeFound("fechaIngreso.greaterThan=" + DEFAULT_FECHA_INGRESO);

        // Get all the socioList where fechaIngreso is greater than SMALLER_FECHA_INGRESO
        defaultSocioShouldBeFound("fechaIngreso.greaterThan=" + SMALLER_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void getAllSociosByParticipacionIsEqualToSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where participacion equals to DEFAULT_PARTICIPACION
        defaultSocioShouldBeFound("participacion.equals=" + DEFAULT_PARTICIPACION);

        // Get all the socioList where participacion equals to UPDATED_PARTICIPACION
        defaultSocioShouldNotBeFound("participacion.equals=" + UPDATED_PARTICIPACION);
    }

    @Test
    @Transactional
    public void getAllSociosByParticipacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where participacion not equals to DEFAULT_PARTICIPACION
        defaultSocioShouldNotBeFound("participacion.notEquals=" + DEFAULT_PARTICIPACION);

        // Get all the socioList where participacion not equals to UPDATED_PARTICIPACION
        defaultSocioShouldBeFound("participacion.notEquals=" + UPDATED_PARTICIPACION);
    }

    @Test
    @Transactional
    public void getAllSociosByParticipacionIsInShouldWork() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where participacion in DEFAULT_PARTICIPACION or UPDATED_PARTICIPACION
        defaultSocioShouldBeFound("participacion.in=" + DEFAULT_PARTICIPACION + "," + UPDATED_PARTICIPACION);

        // Get all the socioList where participacion equals to UPDATED_PARTICIPACION
        defaultSocioShouldNotBeFound("participacion.in=" + UPDATED_PARTICIPACION);
    }

    @Test
    @Transactional
    public void getAllSociosByParticipacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where participacion is not null
        defaultSocioShouldBeFound("participacion.specified=true");

        // Get all the socioList where participacion is null
        defaultSocioShouldNotBeFound("participacion.specified=false");
    }

    @Test
    @Transactional
    public void getAllSociosByParticipacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where participacion is greater than or equal to DEFAULT_PARTICIPACION
        defaultSocioShouldBeFound("participacion.greaterThanOrEqual=" + DEFAULT_PARTICIPACION);

        // Get all the socioList where participacion is greater than or equal to UPDATED_PARTICIPACION
        defaultSocioShouldNotBeFound("participacion.greaterThanOrEqual=" + UPDATED_PARTICIPACION);
    }

    @Test
    @Transactional
    public void getAllSociosByParticipacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where participacion is less than or equal to DEFAULT_PARTICIPACION
        defaultSocioShouldBeFound("participacion.lessThanOrEqual=" + DEFAULT_PARTICIPACION);

        // Get all the socioList where participacion is less than or equal to SMALLER_PARTICIPACION
        defaultSocioShouldNotBeFound("participacion.lessThanOrEqual=" + SMALLER_PARTICIPACION);
    }

    @Test
    @Transactional
    public void getAllSociosByParticipacionIsLessThanSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where participacion is less than DEFAULT_PARTICIPACION
        defaultSocioShouldNotBeFound("participacion.lessThan=" + DEFAULT_PARTICIPACION);

        // Get all the socioList where participacion is less than UPDATED_PARTICIPACION
        defaultSocioShouldBeFound("participacion.lessThan=" + UPDATED_PARTICIPACION);
    }

    @Test
    @Transactional
    public void getAllSociosByParticipacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socioList where participacion is greater than DEFAULT_PARTICIPACION
        defaultSocioShouldNotBeFound("participacion.greaterThan=" + DEFAULT_PARTICIPACION);

        // Get all the socioList where participacion is greater than SMALLER_PARTICIPACION
        defaultSocioShouldBeFound("participacion.greaterThan=" + SMALLER_PARTICIPACION);
    }

    @Test
    @Transactional
    public void getAllSociosByPersonasIsEqualToSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);
        Persona persona = PersonaResourceIT.createEntity(em);
        em.persist(persona);
        em.flush();
        socio.setPersona(persona);
        socioRepository.saveAndFlush(socio);
        Long personasId = persona.getId();

        // Get all the socioList where personas equals to personasId
        defaultSocioShouldBeFound("personasId.equals=" + personasId);

        // Get all the socioList where personas equals to personasId + 1
        defaultSocioShouldNotBeFound("personasId.equals=" + (personasId + 1));
    }

    @Test
    @Transactional
    public void getAllSociosBySociedadesIsEqualToSomething() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);
        Sociedad sociedad = SociedadResourceIT.createEntity(em);
        em.persist(sociedad);
        em.flush();
        socio.setSociedad(sociedad);
        socioRepository.saveAndFlush(socio);
        Long sociedadesId = sociedad.getId();

        // Get all the socioList where sociedades equals to sociedadesId
        defaultSocioShouldBeFound("sociedadesId.equals=" + sociedadesId);

        // Get all the socioList where sociedades equals to sociedadesId + 1
        defaultSocioShouldNotBeFound("sociedadesId.equals=" + (sociedadesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSocioShouldBeFound(String filter) throws Exception {
        restSocioMockMvc
            .perform(get("/api/socios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socio.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].participacion").value(hasItem(DEFAULT_PARTICIPACION.intValue())));

        // Check, that the count call also returns 1
        restSocioMockMvc
            .perform(get("/api/socios/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSocioShouldNotBeFound(String filter) throws Exception {
        restSocioMockMvc
            .perform(get("/api/socios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSocioMockMvc
            .perform(get("/api/socios/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSocio() throws Exception {
        // Get the socio
        restSocioMockMvc.perform(get("/api/socios/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSocio() throws Exception {
        // Initialize the database
        socioService.save(socio);

        int databaseSizeBeforeUpdate = socioRepository.findAll().size();

        // Update the socio
        Socio updatedSocio = socioRepository.findById(socio.getId()).get();
        // Disconnect from session so that the updates on updatedSocio are not directly saved in db
        em.detach(updatedSocio);
        updatedSocio.fechaIngreso(UPDATED_FECHA_INGRESO).participacion(UPDATED_PARTICIPACION);

        restSocioMockMvc
            .perform(
                put("/api/socios")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSocio))
            )
            .andExpect(status().isOk());

        // Validate the Socio in the database
        List<Socio> socioList = socioRepository.findAll();
        assertThat(socioList).hasSize(databaseSizeBeforeUpdate);
        Socio testSocio = socioList.get(socioList.size() - 1);
        assertThat(testSocio.getFechaIngreso()).isEqualTo(UPDATED_FECHA_INGRESO);
        assertThat(testSocio.getParticipacion()).isEqualTo(UPDATED_PARTICIPACION);

        // Validate the Socio in Elasticsearch
        verify(mockSocioSearchRepository, times(2)).save(testSocio);
    }

    @Test
    @Transactional
    public void updateNonExistingSocio() throws Exception {
        int databaseSizeBeforeUpdate = socioRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocioMockMvc
            .perform(
                put("/api/socios").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Socio in the database
        List<Socio> socioList = socioRepository.findAll();
        assertThat(socioList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Socio in Elasticsearch
        verify(mockSocioSearchRepository, times(0)).save(socio);
    }

    @Test
    @Transactional
    public void deleteSocio() throws Exception {
        // Initialize the database
        socioService.save(socio);

        int databaseSizeBeforeDelete = socioRepository.findAll().size();

        // Delete the socio
        restSocioMockMvc
            .perform(delete("/api/socios/{id}", socio.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Socio> socioList = socioRepository.findAll();
        assertThat(socioList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Socio in Elasticsearch
        verify(mockSocioSearchRepository, times(1)).deleteById(socio.getId());
    }

    @Test
    @Transactional
    public void searchSocio() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        socioService.save(socio);
        when(mockSocioSearchRepository.search(queryStringQuery("id:" + socio.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(socio), PageRequest.of(0, 1), 1));

        // Search the socio
        restSocioMockMvc
            .perform(get("/api/_search/socios?query=id:" + socio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socio.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].participacion").value(hasItem(DEFAULT_PARTICIPACION.intValue())));
    }
}
