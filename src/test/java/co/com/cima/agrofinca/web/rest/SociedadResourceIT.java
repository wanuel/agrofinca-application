package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Sociedad;
import co.com.cima.agrofinca.domain.Socio;
import co.com.cima.agrofinca.repository.SociedadRepository;
import co.com.cima.agrofinca.repository.search.SociedadSearchRepository;
import co.com.cima.agrofinca.service.SociedadService;
import co.com.cima.agrofinca.service.dto.SociedadCriteria;
import co.com.cima.agrofinca.service.SociedadQueryService;

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

import co.com.cima.agrofinca.domain.enumeration.ESTADOSOCIEDAD;
/**
 * Integration tests for the {@link SociedadResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class SociedadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_CREACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_CREACION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_CREACION = LocalDate.ofEpochDay(-1L);

    private static final ESTADOSOCIEDAD DEFAULT_ESTADO = ESTADOSOCIEDAD.ACTIVO;
    private static final ESTADOSOCIEDAD UPDATED_ESTADO = ESTADOSOCIEDAD.INACTIVO;

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    @Autowired
    private SociedadRepository sociedadRepository;

    @Autowired
    private SociedadService sociedadService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.SociedadSearchRepositoryMockConfiguration
     */
    @Autowired
    private SociedadSearchRepository mockSociedadSearchRepository;

    @Autowired
    private SociedadQueryService sociedadQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSociedadMockMvc;

    private Sociedad sociedad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sociedad createEntity(EntityManager em) {
        Sociedad sociedad = new Sociedad()
            .nombre(DEFAULT_NOMBRE)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .estado(DEFAULT_ESTADO)
            .observaciones(DEFAULT_OBSERVACIONES);
        return sociedad;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sociedad createUpdatedEntity(EntityManager em) {
        Sociedad sociedad = new Sociedad()
            .nombre(UPDATED_NOMBRE)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .estado(UPDATED_ESTADO)
            .observaciones(UPDATED_OBSERVACIONES);
        return sociedad;
    }

    @BeforeEach
    public void initTest() {
        sociedad = createEntity(em);
    }

    @Test
    @Transactional
    public void createSociedad() throws Exception {
        int databaseSizeBeforeCreate = sociedadRepository.findAll().size();
        // Create the Sociedad
        restSociedadMockMvc.perform(post("/api/sociedads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sociedad)))
            .andExpect(status().isCreated());

        // Validate the Sociedad in the database
        List<Sociedad> sociedadList = sociedadRepository.findAll();
        assertThat(sociedadList).hasSize(databaseSizeBeforeCreate + 1);
        Sociedad testSociedad = sociedadList.get(sociedadList.size() - 1);
        assertThat(testSociedad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testSociedad.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testSociedad.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testSociedad.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);

        // Validate the Sociedad in Elasticsearch
        verify(mockSociedadSearchRepository, times(1)).save(testSociedad);
    }

    @Test
    @Transactional
    public void createSociedadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sociedadRepository.findAll().size();

        // Create the Sociedad with an existing ID
        sociedad.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSociedadMockMvc.perform(post("/api/sociedads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sociedad)))
            .andExpect(status().isBadRequest());

        // Validate the Sociedad in the database
        List<Sociedad> sociedadList = sociedadRepository.findAll();
        assertThat(sociedadList).hasSize(databaseSizeBeforeCreate);

        // Validate the Sociedad in Elasticsearch
        verify(mockSociedadSearchRepository, times(0)).save(sociedad);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = sociedadRepository.findAll().size();
        // set the field null
        sociedad.setNombre(null);

        // Create the Sociedad, which fails.


        restSociedadMockMvc.perform(post("/api/sociedads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sociedad)))
            .andExpect(status().isBadRequest());

        List<Sociedad> sociedadList = sociedadRepository.findAll();
        assertThat(sociedadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaCreacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = sociedadRepository.findAll().size();
        // set the field null
        sociedad.setFechaCreacion(null);

        // Create the Sociedad, which fails.


        restSociedadMockMvc.perform(post("/api/sociedads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sociedad)))
            .andExpect(status().isBadRequest());

        List<Sociedad> sociedadList = sociedadRepository.findAll();
        assertThat(sociedadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = sociedadRepository.findAll().size();
        // set the field null
        sociedad.setEstado(null);

        // Create the Sociedad, which fails.


        restSociedadMockMvc.perform(post("/api/sociedads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sociedad)))
            .andExpect(status().isBadRequest());

        List<Sociedad> sociedadList = sociedadRepository.findAll();
        assertThat(sociedadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSociedads() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList
        restSociedadMockMvc.perform(get("/api/sociedads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sociedad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }
    
    @Test
    @Transactional
    public void getSociedad() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get the sociedad
        restSociedadMockMvc.perform(get("/api/sociedads/{id}", sociedad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sociedad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }


    @Test
    @Transactional
    public void getSociedadsByIdFiltering() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        Long id = sociedad.getId();

        defaultSociedadShouldBeFound("id.equals=" + id);
        defaultSociedadShouldNotBeFound("id.notEquals=" + id);

        defaultSociedadShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSociedadShouldNotBeFound("id.greaterThan=" + id);

        defaultSociedadShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSociedadShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSociedadsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where nombre equals to DEFAULT_NOMBRE
        defaultSociedadShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the sociedadList where nombre equals to UPDATED_NOMBRE
        defaultSociedadShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllSociedadsByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where nombre not equals to DEFAULT_NOMBRE
        defaultSociedadShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the sociedadList where nombre not equals to UPDATED_NOMBRE
        defaultSociedadShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllSociedadsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultSociedadShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the sociedadList where nombre equals to UPDATED_NOMBRE
        defaultSociedadShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllSociedadsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where nombre is not null
        defaultSociedadShouldBeFound("nombre.specified=true");

        // Get all the sociedadList where nombre is null
        defaultSociedadShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllSociedadsByNombreContainsSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where nombre contains DEFAULT_NOMBRE
        defaultSociedadShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the sociedadList where nombre contains UPDATED_NOMBRE
        defaultSociedadShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllSociedadsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where nombre does not contain DEFAULT_NOMBRE
        defaultSociedadShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the sociedadList where nombre does not contain UPDATED_NOMBRE
        defaultSociedadShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllSociedadsByFechaCreacionIsEqualToSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where fechaCreacion equals to DEFAULT_FECHA_CREACION
        defaultSociedadShouldBeFound("fechaCreacion.equals=" + DEFAULT_FECHA_CREACION);

        // Get all the sociedadList where fechaCreacion equals to UPDATED_FECHA_CREACION
        defaultSociedadShouldNotBeFound("fechaCreacion.equals=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    public void getAllSociedadsByFechaCreacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where fechaCreacion not equals to DEFAULT_FECHA_CREACION
        defaultSociedadShouldNotBeFound("fechaCreacion.notEquals=" + DEFAULT_FECHA_CREACION);

        // Get all the sociedadList where fechaCreacion not equals to UPDATED_FECHA_CREACION
        defaultSociedadShouldBeFound("fechaCreacion.notEquals=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    public void getAllSociedadsByFechaCreacionIsInShouldWork() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where fechaCreacion in DEFAULT_FECHA_CREACION or UPDATED_FECHA_CREACION
        defaultSociedadShouldBeFound("fechaCreacion.in=" + DEFAULT_FECHA_CREACION + "," + UPDATED_FECHA_CREACION);

        // Get all the sociedadList where fechaCreacion equals to UPDATED_FECHA_CREACION
        defaultSociedadShouldNotBeFound("fechaCreacion.in=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    public void getAllSociedadsByFechaCreacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where fechaCreacion is not null
        defaultSociedadShouldBeFound("fechaCreacion.specified=true");

        // Get all the sociedadList where fechaCreacion is null
        defaultSociedadShouldNotBeFound("fechaCreacion.specified=false");
    }

    @Test
    @Transactional
    public void getAllSociedadsByFechaCreacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where fechaCreacion is greater than or equal to DEFAULT_FECHA_CREACION
        defaultSociedadShouldBeFound("fechaCreacion.greaterThanOrEqual=" + DEFAULT_FECHA_CREACION);

        // Get all the sociedadList where fechaCreacion is greater than or equal to UPDATED_FECHA_CREACION
        defaultSociedadShouldNotBeFound("fechaCreacion.greaterThanOrEqual=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    public void getAllSociedadsByFechaCreacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where fechaCreacion is less than or equal to DEFAULT_FECHA_CREACION
        defaultSociedadShouldBeFound("fechaCreacion.lessThanOrEqual=" + DEFAULT_FECHA_CREACION);

        // Get all the sociedadList where fechaCreacion is less than or equal to SMALLER_FECHA_CREACION
        defaultSociedadShouldNotBeFound("fechaCreacion.lessThanOrEqual=" + SMALLER_FECHA_CREACION);
    }

    @Test
    @Transactional
    public void getAllSociedadsByFechaCreacionIsLessThanSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where fechaCreacion is less than DEFAULT_FECHA_CREACION
        defaultSociedadShouldNotBeFound("fechaCreacion.lessThan=" + DEFAULT_FECHA_CREACION);

        // Get all the sociedadList where fechaCreacion is less than UPDATED_FECHA_CREACION
        defaultSociedadShouldBeFound("fechaCreacion.lessThan=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    public void getAllSociedadsByFechaCreacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where fechaCreacion is greater than DEFAULT_FECHA_CREACION
        defaultSociedadShouldNotBeFound("fechaCreacion.greaterThan=" + DEFAULT_FECHA_CREACION);

        // Get all the sociedadList where fechaCreacion is greater than SMALLER_FECHA_CREACION
        defaultSociedadShouldBeFound("fechaCreacion.greaterThan=" + SMALLER_FECHA_CREACION);
    }


    @Test
    @Transactional
    public void getAllSociedadsByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where estado equals to DEFAULT_ESTADO
        defaultSociedadShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the sociedadList where estado equals to UPDATED_ESTADO
        defaultSociedadShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllSociedadsByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where estado not equals to DEFAULT_ESTADO
        defaultSociedadShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the sociedadList where estado not equals to UPDATED_ESTADO
        defaultSociedadShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllSociedadsByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultSociedadShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the sociedadList where estado equals to UPDATED_ESTADO
        defaultSociedadShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllSociedadsByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where estado is not null
        defaultSociedadShouldBeFound("estado.specified=true");

        // Get all the sociedadList where estado is null
        defaultSociedadShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    public void getAllSociedadsByObservacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where observaciones equals to DEFAULT_OBSERVACIONES
        defaultSociedadShouldBeFound("observaciones.equals=" + DEFAULT_OBSERVACIONES);

        // Get all the sociedadList where observaciones equals to UPDATED_OBSERVACIONES
        defaultSociedadShouldNotBeFound("observaciones.equals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    public void getAllSociedadsByObservacionesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where observaciones not equals to DEFAULT_OBSERVACIONES
        defaultSociedadShouldNotBeFound("observaciones.notEquals=" + DEFAULT_OBSERVACIONES);

        // Get all the sociedadList where observaciones not equals to UPDATED_OBSERVACIONES
        defaultSociedadShouldBeFound("observaciones.notEquals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    public void getAllSociedadsByObservacionesIsInShouldWork() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where observaciones in DEFAULT_OBSERVACIONES or UPDATED_OBSERVACIONES
        defaultSociedadShouldBeFound("observaciones.in=" + DEFAULT_OBSERVACIONES + "," + UPDATED_OBSERVACIONES);

        // Get all the sociedadList where observaciones equals to UPDATED_OBSERVACIONES
        defaultSociedadShouldNotBeFound("observaciones.in=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    public void getAllSociedadsByObservacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where observaciones is not null
        defaultSociedadShouldBeFound("observaciones.specified=true");

        // Get all the sociedadList where observaciones is null
        defaultSociedadShouldNotBeFound("observaciones.specified=false");
    }
                @Test
    @Transactional
    public void getAllSociedadsByObservacionesContainsSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where observaciones contains DEFAULT_OBSERVACIONES
        defaultSociedadShouldBeFound("observaciones.contains=" + DEFAULT_OBSERVACIONES);

        // Get all the sociedadList where observaciones contains UPDATED_OBSERVACIONES
        defaultSociedadShouldNotBeFound("observaciones.contains=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    public void getAllSociedadsByObservacionesNotContainsSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);

        // Get all the sociedadList where observaciones does not contain DEFAULT_OBSERVACIONES
        defaultSociedadShouldNotBeFound("observaciones.doesNotContain=" + DEFAULT_OBSERVACIONES);

        // Get all the sociedadList where observaciones does not contain UPDATED_OBSERVACIONES
        defaultSociedadShouldBeFound("observaciones.doesNotContain=" + UPDATED_OBSERVACIONES);
    }


    @Test
    @Transactional
    public void getAllSociedadsBySocioIsEqualToSomething() throws Exception {
        // Initialize the database
        sociedadRepository.saveAndFlush(sociedad);
        Socio socio = SocioResourceIT.createEntity(em);
        em.persist(socio);
        em.flush();
        sociedad.setSocio(socio);
        sociedadRepository.saveAndFlush(sociedad);
        Long socioId = socio.getId();

        // Get all the sociedadList where socio equals to socioId
        defaultSociedadShouldBeFound("socioId.equals=" + socioId);

        // Get all the sociedadList where socio equals to socioId + 1
        defaultSociedadShouldNotBeFound("socioId.equals=" + (socioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSociedadShouldBeFound(String filter) throws Exception {
        restSociedadMockMvc.perform(get("/api/sociedads?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sociedad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));

        // Check, that the count call also returns 1
        restSociedadMockMvc.perform(get("/api/sociedads/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSociedadShouldNotBeFound(String filter) throws Exception {
        restSociedadMockMvc.perform(get("/api/sociedads?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSociedadMockMvc.perform(get("/api/sociedads/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSociedad() throws Exception {
        // Get the sociedad
        restSociedadMockMvc.perform(get("/api/sociedads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSociedad() throws Exception {
        // Initialize the database
        sociedadService.save(sociedad);

        int databaseSizeBeforeUpdate = sociedadRepository.findAll().size();

        // Update the sociedad
        Sociedad updatedSociedad = sociedadRepository.findById(sociedad.getId()).get();
        // Disconnect from session so that the updates on updatedSociedad are not directly saved in db
        em.detach(updatedSociedad);
        updatedSociedad
            .nombre(UPDATED_NOMBRE)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .estado(UPDATED_ESTADO)
            .observaciones(UPDATED_OBSERVACIONES);

        restSociedadMockMvc.perform(put("/api/sociedads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSociedad)))
            .andExpect(status().isOk());

        // Validate the Sociedad in the database
        List<Sociedad> sociedadList = sociedadRepository.findAll();
        assertThat(sociedadList).hasSize(databaseSizeBeforeUpdate);
        Sociedad testSociedad = sociedadList.get(sociedadList.size() - 1);
        assertThat(testSociedad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSociedad.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testSociedad.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testSociedad.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);

        // Validate the Sociedad in Elasticsearch
        verify(mockSociedadSearchRepository, times(2)).save(testSociedad);
    }

    @Test
    @Transactional
    public void updateNonExistingSociedad() throws Exception {
        int databaseSizeBeforeUpdate = sociedadRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSociedadMockMvc.perform(put("/api/sociedads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sociedad)))
            .andExpect(status().isBadRequest());

        // Validate the Sociedad in the database
        List<Sociedad> sociedadList = sociedadRepository.findAll();
        assertThat(sociedadList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Sociedad in Elasticsearch
        verify(mockSociedadSearchRepository, times(0)).save(sociedad);
    }

    @Test
    @Transactional
    public void deleteSociedad() throws Exception {
        // Initialize the database
        sociedadService.save(sociedad);

        int databaseSizeBeforeDelete = sociedadRepository.findAll().size();

        // Delete the sociedad
        restSociedadMockMvc.perform(delete("/api/sociedads/{id}", sociedad.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sociedad> sociedadList = sociedadRepository.findAll();
        assertThat(sociedadList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Sociedad in Elasticsearch
        verify(mockSociedadSearchRepository, times(1)).deleteById(sociedad.getId());
    }

    @Test
    @Transactional
    public void searchSociedad() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        sociedadService.save(sociedad);
        when(mockSociedadSearchRepository.search(queryStringQuery("id:" + sociedad.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(sociedad), PageRequest.of(0, 1), 1));

        // Search the sociedad
        restSociedadMockMvc.perform(get("/api/_search/sociedads?query=id:" + sociedad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sociedad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }
}
