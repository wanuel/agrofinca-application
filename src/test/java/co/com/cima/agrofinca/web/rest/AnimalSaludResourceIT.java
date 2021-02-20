package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.AnimalSalud;
import co.com.cima.agrofinca.domain.AnimalEvento;
import co.com.cima.agrofinca.domain.Parametros;
import co.com.cima.agrofinca.repository.AnimalSaludRepository;
import co.com.cima.agrofinca.repository.search.AnimalSaludSearchRepository;
import co.com.cima.agrofinca.service.AnimalSaludService;
import co.com.cima.agrofinca.service.dto.AnimalSaludCriteria;
import co.com.cima.agrofinca.service.AnimalSaludQueryService;

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

import co.com.cima.agrofinca.domain.enumeration.SINO;
import co.com.cima.agrofinca.domain.enumeration.SINO;
import co.com.cima.agrofinca.domain.enumeration.SINO;
/**
 * Integration tests for the {@link AnimalSaludResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AnimalSaludResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_LABORATORIO = "AAAAAAAAAA";
    private static final String UPDATED_LABORATORIO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DOSIS = new BigDecimal(1);
    private static final BigDecimal UPDATED_DOSIS = new BigDecimal(2);
    private static final BigDecimal SMALLER_DOSIS = new BigDecimal(1 - 1);

    private static final SINO DEFAULT_INYECTADO = SINO.SI;
    private static final SINO UPDATED_INYECTADO = SINO.NO;

    private static final SINO DEFAULT_INTRAMUSCULAR = SINO.SI;
    private static final SINO UPDATED_INTRAMUSCULAR = SINO.NO;

    private static final SINO DEFAULT_SUBCUTANEO = SINO.SI;
    private static final SINO UPDATED_SUBCUTANEO = SINO.NO;

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    @Autowired
    private AnimalSaludRepository animalSaludRepository;

    @Autowired
    private AnimalSaludService animalSaludService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.AnimalSaludSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnimalSaludSearchRepository mockAnimalSaludSearchRepository;

    @Autowired
    private AnimalSaludQueryService animalSaludQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnimalSaludMockMvc;

    private AnimalSalud animalSalud;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalSalud createEntity(EntityManager em) {
        AnimalSalud animalSalud = new AnimalSalud()
            .fecha(DEFAULT_FECHA)
            .nombre(DEFAULT_NOMBRE)
            .laboratorio(DEFAULT_LABORATORIO)
            .dosis(DEFAULT_DOSIS)
            .inyectado(DEFAULT_INYECTADO)
            .intramuscular(DEFAULT_INTRAMUSCULAR)
            .subcutaneo(DEFAULT_SUBCUTANEO)
            .observacion(DEFAULT_OBSERVACION);
        return animalSalud;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalSalud createUpdatedEntity(EntityManager em) {
        AnimalSalud animalSalud = new AnimalSalud()
            .fecha(UPDATED_FECHA)
            .nombre(UPDATED_NOMBRE)
            .laboratorio(UPDATED_LABORATORIO)
            .dosis(UPDATED_DOSIS)
            .inyectado(UPDATED_INYECTADO)
            .intramuscular(UPDATED_INTRAMUSCULAR)
            .subcutaneo(UPDATED_SUBCUTANEO)
            .observacion(UPDATED_OBSERVACION);
        return animalSalud;
    }

    @BeforeEach
    public void initTest() {
        animalSalud = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnimalSalud() throws Exception {
        int databaseSizeBeforeCreate = animalSaludRepository.findAll().size();
        // Create the AnimalSalud
        restAnimalSaludMockMvc.perform(post("/api/animal-saluds").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalSalud)))
            .andExpect(status().isCreated());

        // Validate the AnimalSalud in the database
        List<AnimalSalud> animalSaludList = animalSaludRepository.findAll();
        assertThat(animalSaludList).hasSize(databaseSizeBeforeCreate + 1);
        AnimalSalud testAnimalSalud = animalSaludList.get(animalSaludList.size() - 1);
        assertThat(testAnimalSalud.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testAnimalSalud.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testAnimalSalud.getLaboratorio()).isEqualTo(DEFAULT_LABORATORIO);
        assertThat(testAnimalSalud.getDosis()).isEqualTo(DEFAULT_DOSIS);
        assertThat(testAnimalSalud.getInyectado()).isEqualTo(DEFAULT_INYECTADO);
        assertThat(testAnimalSalud.getIntramuscular()).isEqualTo(DEFAULT_INTRAMUSCULAR);
        assertThat(testAnimalSalud.getSubcutaneo()).isEqualTo(DEFAULT_SUBCUTANEO);
        assertThat(testAnimalSalud.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);

        // Validate the AnimalSalud in Elasticsearch
        verify(mockAnimalSaludSearchRepository, times(1)).save(testAnimalSalud);
    }

    @Test
    @Transactional
    public void createAnimalSaludWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = animalSaludRepository.findAll().size();

        // Create the AnimalSalud with an existing ID
        animalSalud.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnimalSaludMockMvc.perform(post("/api/animal-saluds").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalSalud)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalSalud in the database
        List<AnimalSalud> animalSaludList = animalSaludRepository.findAll();
        assertThat(animalSaludList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnimalSalud in Elasticsearch
        verify(mockAnimalSaludSearchRepository, times(0)).save(animalSalud);
    }


    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalSaludRepository.findAll().size();
        // set the field null
        animalSalud.setFecha(null);

        // Create the AnimalSalud, which fails.


        restAnimalSaludMockMvc.perform(post("/api/animal-saluds").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalSalud)))
            .andExpect(status().isBadRequest());

        List<AnimalSalud> animalSaludList = animalSaludRepository.findAll();
        assertThat(animalSaludList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalSaludRepository.findAll().size();
        // set the field null
        animalSalud.setNombre(null);

        // Create the AnimalSalud, which fails.


        restAnimalSaludMockMvc.perform(post("/api/animal-saluds").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalSalud)))
            .andExpect(status().isBadRequest());

        List<AnimalSalud> animalSaludList = animalSaludRepository.findAll();
        assertThat(animalSaludList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDosisIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalSaludRepository.findAll().size();
        // set the field null
        animalSalud.setDosis(null);

        // Create the AnimalSalud, which fails.


        restAnimalSaludMockMvc.perform(post("/api/animal-saluds").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalSalud)))
            .andExpect(status().isBadRequest());

        List<AnimalSalud> animalSaludList = animalSaludRepository.findAll();
        assertThat(animalSaludList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInyectadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalSaludRepository.findAll().size();
        // set the field null
        animalSalud.setInyectado(null);

        // Create the AnimalSalud, which fails.


        restAnimalSaludMockMvc.perform(post("/api/animal-saluds").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalSalud)))
            .andExpect(status().isBadRequest());

        List<AnimalSalud> animalSaludList = animalSaludRepository.findAll();
        assertThat(animalSaludList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIntramuscularIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalSaludRepository.findAll().size();
        // set the field null
        animalSalud.setIntramuscular(null);

        // Create the AnimalSalud, which fails.


        restAnimalSaludMockMvc.perform(post("/api/animal-saluds").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalSalud)))
            .andExpect(status().isBadRequest());

        List<AnimalSalud> animalSaludList = animalSaludRepository.findAll();
        assertThat(animalSaludList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubcutaneoIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalSaludRepository.findAll().size();
        // set the field null
        animalSalud.setSubcutaneo(null);

        // Create the AnimalSalud, which fails.


        restAnimalSaludMockMvc.perform(post("/api/animal-saluds").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalSalud)))
            .andExpect(status().isBadRequest());

        List<AnimalSalud> animalSaludList = animalSaludRepository.findAll();
        assertThat(animalSaludList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnimalSaluds() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList
        restAnimalSaludMockMvc.perform(get("/api/animal-saluds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalSalud.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].laboratorio").value(hasItem(DEFAULT_LABORATORIO)))
            .andExpect(jsonPath("$.[*].dosis").value(hasItem(DEFAULT_DOSIS.intValue())))
            .andExpect(jsonPath("$.[*].inyectado").value(hasItem(DEFAULT_INYECTADO.toString())))
            .andExpect(jsonPath("$.[*].intramuscular").value(hasItem(DEFAULT_INTRAMUSCULAR.toString())))
            .andExpect(jsonPath("$.[*].subcutaneo").value(hasItem(DEFAULT_SUBCUTANEO.toString())))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION)));
    }
    
    @Test
    @Transactional
    public void getAnimalSalud() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get the animalSalud
        restAnimalSaludMockMvc.perform(get("/api/animal-saluds/{id}", animalSalud.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(animalSalud.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.laboratorio").value(DEFAULT_LABORATORIO))
            .andExpect(jsonPath("$.dosis").value(DEFAULT_DOSIS.intValue()))
            .andExpect(jsonPath("$.inyectado").value(DEFAULT_INYECTADO.toString()))
            .andExpect(jsonPath("$.intramuscular").value(DEFAULT_INTRAMUSCULAR.toString()))
            .andExpect(jsonPath("$.subcutaneo").value(DEFAULT_SUBCUTANEO.toString()))
            .andExpect(jsonPath("$.observacion").value(DEFAULT_OBSERVACION));
    }


    @Test
    @Transactional
    public void getAnimalSaludsByIdFiltering() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        Long id = animalSalud.getId();

        defaultAnimalSaludShouldBeFound("id.equals=" + id);
        defaultAnimalSaludShouldNotBeFound("id.notEquals=" + id);

        defaultAnimalSaludShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnimalSaludShouldNotBeFound("id.greaterThan=" + id);

        defaultAnimalSaludShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnimalSaludShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAnimalSaludsByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where fecha equals to DEFAULT_FECHA
        defaultAnimalSaludShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the animalSaludList where fecha equals to UPDATED_FECHA
        defaultAnimalSaludShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByFechaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where fecha not equals to DEFAULT_FECHA
        defaultAnimalSaludShouldNotBeFound("fecha.notEquals=" + DEFAULT_FECHA);

        // Get all the animalSaludList where fecha not equals to UPDATED_FECHA
        defaultAnimalSaludShouldBeFound("fecha.notEquals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultAnimalSaludShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the animalSaludList where fecha equals to UPDATED_FECHA
        defaultAnimalSaludShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where fecha is not null
        defaultAnimalSaludShouldBeFound("fecha.specified=true");

        // Get all the animalSaludList where fecha is null
        defaultAnimalSaludShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where fecha is greater than or equal to DEFAULT_FECHA
        defaultAnimalSaludShouldBeFound("fecha.greaterThanOrEqual=" + DEFAULT_FECHA);

        // Get all the animalSaludList where fecha is greater than or equal to UPDATED_FECHA
        defaultAnimalSaludShouldNotBeFound("fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where fecha is less than or equal to DEFAULT_FECHA
        defaultAnimalSaludShouldBeFound("fecha.lessThanOrEqual=" + DEFAULT_FECHA);

        // Get all the animalSaludList where fecha is less than or equal to SMALLER_FECHA
        defaultAnimalSaludShouldNotBeFound("fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where fecha is less than DEFAULT_FECHA
        defaultAnimalSaludShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the animalSaludList where fecha is less than UPDATED_FECHA
        defaultAnimalSaludShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where fecha is greater than DEFAULT_FECHA
        defaultAnimalSaludShouldNotBeFound("fecha.greaterThan=" + DEFAULT_FECHA);

        // Get all the animalSaludList where fecha is greater than SMALLER_FECHA
        defaultAnimalSaludShouldBeFound("fecha.greaterThan=" + SMALLER_FECHA);
    }


    @Test
    @Transactional
    public void getAllAnimalSaludsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where nombre equals to DEFAULT_NOMBRE
        defaultAnimalSaludShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the animalSaludList where nombre equals to UPDATED_NOMBRE
        defaultAnimalSaludShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where nombre not equals to DEFAULT_NOMBRE
        defaultAnimalSaludShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the animalSaludList where nombre not equals to UPDATED_NOMBRE
        defaultAnimalSaludShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultAnimalSaludShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the animalSaludList where nombre equals to UPDATED_NOMBRE
        defaultAnimalSaludShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where nombre is not null
        defaultAnimalSaludShouldBeFound("nombre.specified=true");

        // Get all the animalSaludList where nombre is null
        defaultAnimalSaludShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllAnimalSaludsByNombreContainsSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where nombre contains DEFAULT_NOMBRE
        defaultAnimalSaludShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the animalSaludList where nombre contains UPDATED_NOMBRE
        defaultAnimalSaludShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where nombre does not contain DEFAULT_NOMBRE
        defaultAnimalSaludShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the animalSaludList where nombre does not contain UPDATED_NOMBRE
        defaultAnimalSaludShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllAnimalSaludsByLaboratorioIsEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where laboratorio equals to DEFAULT_LABORATORIO
        defaultAnimalSaludShouldBeFound("laboratorio.equals=" + DEFAULT_LABORATORIO);

        // Get all the animalSaludList where laboratorio equals to UPDATED_LABORATORIO
        defaultAnimalSaludShouldNotBeFound("laboratorio.equals=" + UPDATED_LABORATORIO);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByLaboratorioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where laboratorio not equals to DEFAULT_LABORATORIO
        defaultAnimalSaludShouldNotBeFound("laboratorio.notEquals=" + DEFAULT_LABORATORIO);

        // Get all the animalSaludList where laboratorio not equals to UPDATED_LABORATORIO
        defaultAnimalSaludShouldBeFound("laboratorio.notEquals=" + UPDATED_LABORATORIO);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByLaboratorioIsInShouldWork() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where laboratorio in DEFAULT_LABORATORIO or UPDATED_LABORATORIO
        defaultAnimalSaludShouldBeFound("laboratorio.in=" + DEFAULT_LABORATORIO + "," + UPDATED_LABORATORIO);

        // Get all the animalSaludList where laboratorio equals to UPDATED_LABORATORIO
        defaultAnimalSaludShouldNotBeFound("laboratorio.in=" + UPDATED_LABORATORIO);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByLaboratorioIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where laboratorio is not null
        defaultAnimalSaludShouldBeFound("laboratorio.specified=true");

        // Get all the animalSaludList where laboratorio is null
        defaultAnimalSaludShouldNotBeFound("laboratorio.specified=false");
    }
                @Test
    @Transactional
    public void getAllAnimalSaludsByLaboratorioContainsSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where laboratorio contains DEFAULT_LABORATORIO
        defaultAnimalSaludShouldBeFound("laboratorio.contains=" + DEFAULT_LABORATORIO);

        // Get all the animalSaludList where laboratorio contains UPDATED_LABORATORIO
        defaultAnimalSaludShouldNotBeFound("laboratorio.contains=" + UPDATED_LABORATORIO);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByLaboratorioNotContainsSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where laboratorio does not contain DEFAULT_LABORATORIO
        defaultAnimalSaludShouldNotBeFound("laboratorio.doesNotContain=" + DEFAULT_LABORATORIO);

        // Get all the animalSaludList where laboratorio does not contain UPDATED_LABORATORIO
        defaultAnimalSaludShouldBeFound("laboratorio.doesNotContain=" + UPDATED_LABORATORIO);
    }


    @Test
    @Transactional
    public void getAllAnimalSaludsByDosisIsEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where dosis equals to DEFAULT_DOSIS
        defaultAnimalSaludShouldBeFound("dosis.equals=" + DEFAULT_DOSIS);

        // Get all the animalSaludList where dosis equals to UPDATED_DOSIS
        defaultAnimalSaludShouldNotBeFound("dosis.equals=" + UPDATED_DOSIS);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByDosisIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where dosis not equals to DEFAULT_DOSIS
        defaultAnimalSaludShouldNotBeFound("dosis.notEquals=" + DEFAULT_DOSIS);

        // Get all the animalSaludList where dosis not equals to UPDATED_DOSIS
        defaultAnimalSaludShouldBeFound("dosis.notEquals=" + UPDATED_DOSIS);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByDosisIsInShouldWork() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where dosis in DEFAULT_DOSIS or UPDATED_DOSIS
        defaultAnimalSaludShouldBeFound("dosis.in=" + DEFAULT_DOSIS + "," + UPDATED_DOSIS);

        // Get all the animalSaludList where dosis equals to UPDATED_DOSIS
        defaultAnimalSaludShouldNotBeFound("dosis.in=" + UPDATED_DOSIS);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByDosisIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where dosis is not null
        defaultAnimalSaludShouldBeFound("dosis.specified=true");

        // Get all the animalSaludList where dosis is null
        defaultAnimalSaludShouldNotBeFound("dosis.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByDosisIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where dosis is greater than or equal to DEFAULT_DOSIS
        defaultAnimalSaludShouldBeFound("dosis.greaterThanOrEqual=" + DEFAULT_DOSIS);

        // Get all the animalSaludList where dosis is greater than or equal to UPDATED_DOSIS
        defaultAnimalSaludShouldNotBeFound("dosis.greaterThanOrEqual=" + UPDATED_DOSIS);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByDosisIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where dosis is less than or equal to DEFAULT_DOSIS
        defaultAnimalSaludShouldBeFound("dosis.lessThanOrEqual=" + DEFAULT_DOSIS);

        // Get all the animalSaludList where dosis is less than or equal to SMALLER_DOSIS
        defaultAnimalSaludShouldNotBeFound("dosis.lessThanOrEqual=" + SMALLER_DOSIS);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByDosisIsLessThanSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where dosis is less than DEFAULT_DOSIS
        defaultAnimalSaludShouldNotBeFound("dosis.lessThan=" + DEFAULT_DOSIS);

        // Get all the animalSaludList where dosis is less than UPDATED_DOSIS
        defaultAnimalSaludShouldBeFound("dosis.lessThan=" + UPDATED_DOSIS);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByDosisIsGreaterThanSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where dosis is greater than DEFAULT_DOSIS
        defaultAnimalSaludShouldNotBeFound("dosis.greaterThan=" + DEFAULT_DOSIS);

        // Get all the animalSaludList where dosis is greater than SMALLER_DOSIS
        defaultAnimalSaludShouldBeFound("dosis.greaterThan=" + SMALLER_DOSIS);
    }


    @Test
    @Transactional
    public void getAllAnimalSaludsByInyectadoIsEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where inyectado equals to DEFAULT_INYECTADO
        defaultAnimalSaludShouldBeFound("inyectado.equals=" + DEFAULT_INYECTADO);

        // Get all the animalSaludList where inyectado equals to UPDATED_INYECTADO
        defaultAnimalSaludShouldNotBeFound("inyectado.equals=" + UPDATED_INYECTADO);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByInyectadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where inyectado not equals to DEFAULT_INYECTADO
        defaultAnimalSaludShouldNotBeFound("inyectado.notEquals=" + DEFAULT_INYECTADO);

        // Get all the animalSaludList where inyectado not equals to UPDATED_INYECTADO
        defaultAnimalSaludShouldBeFound("inyectado.notEquals=" + UPDATED_INYECTADO);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByInyectadoIsInShouldWork() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where inyectado in DEFAULT_INYECTADO or UPDATED_INYECTADO
        defaultAnimalSaludShouldBeFound("inyectado.in=" + DEFAULT_INYECTADO + "," + UPDATED_INYECTADO);

        // Get all the animalSaludList where inyectado equals to UPDATED_INYECTADO
        defaultAnimalSaludShouldNotBeFound("inyectado.in=" + UPDATED_INYECTADO);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByInyectadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where inyectado is not null
        defaultAnimalSaludShouldBeFound("inyectado.specified=true");

        // Get all the animalSaludList where inyectado is null
        defaultAnimalSaludShouldNotBeFound("inyectado.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByIntramuscularIsEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where intramuscular equals to DEFAULT_INTRAMUSCULAR
        defaultAnimalSaludShouldBeFound("intramuscular.equals=" + DEFAULT_INTRAMUSCULAR);

        // Get all the animalSaludList where intramuscular equals to UPDATED_INTRAMUSCULAR
        defaultAnimalSaludShouldNotBeFound("intramuscular.equals=" + UPDATED_INTRAMUSCULAR);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByIntramuscularIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where intramuscular not equals to DEFAULT_INTRAMUSCULAR
        defaultAnimalSaludShouldNotBeFound("intramuscular.notEquals=" + DEFAULT_INTRAMUSCULAR);

        // Get all the animalSaludList where intramuscular not equals to UPDATED_INTRAMUSCULAR
        defaultAnimalSaludShouldBeFound("intramuscular.notEquals=" + UPDATED_INTRAMUSCULAR);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByIntramuscularIsInShouldWork() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where intramuscular in DEFAULT_INTRAMUSCULAR or UPDATED_INTRAMUSCULAR
        defaultAnimalSaludShouldBeFound("intramuscular.in=" + DEFAULT_INTRAMUSCULAR + "," + UPDATED_INTRAMUSCULAR);

        // Get all the animalSaludList where intramuscular equals to UPDATED_INTRAMUSCULAR
        defaultAnimalSaludShouldNotBeFound("intramuscular.in=" + UPDATED_INTRAMUSCULAR);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByIntramuscularIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where intramuscular is not null
        defaultAnimalSaludShouldBeFound("intramuscular.specified=true");

        // Get all the animalSaludList where intramuscular is null
        defaultAnimalSaludShouldNotBeFound("intramuscular.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsBySubcutaneoIsEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where subcutaneo equals to DEFAULT_SUBCUTANEO
        defaultAnimalSaludShouldBeFound("subcutaneo.equals=" + DEFAULT_SUBCUTANEO);

        // Get all the animalSaludList where subcutaneo equals to UPDATED_SUBCUTANEO
        defaultAnimalSaludShouldNotBeFound("subcutaneo.equals=" + UPDATED_SUBCUTANEO);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsBySubcutaneoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where subcutaneo not equals to DEFAULT_SUBCUTANEO
        defaultAnimalSaludShouldNotBeFound("subcutaneo.notEquals=" + DEFAULT_SUBCUTANEO);

        // Get all the animalSaludList where subcutaneo not equals to UPDATED_SUBCUTANEO
        defaultAnimalSaludShouldBeFound("subcutaneo.notEquals=" + UPDATED_SUBCUTANEO);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsBySubcutaneoIsInShouldWork() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where subcutaneo in DEFAULT_SUBCUTANEO or UPDATED_SUBCUTANEO
        defaultAnimalSaludShouldBeFound("subcutaneo.in=" + DEFAULT_SUBCUTANEO + "," + UPDATED_SUBCUTANEO);

        // Get all the animalSaludList where subcutaneo equals to UPDATED_SUBCUTANEO
        defaultAnimalSaludShouldNotBeFound("subcutaneo.in=" + UPDATED_SUBCUTANEO);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsBySubcutaneoIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where subcutaneo is not null
        defaultAnimalSaludShouldBeFound("subcutaneo.specified=true");

        // Get all the animalSaludList where subcutaneo is null
        defaultAnimalSaludShouldNotBeFound("subcutaneo.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByObservacionIsEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where observacion equals to DEFAULT_OBSERVACION
        defaultAnimalSaludShouldBeFound("observacion.equals=" + DEFAULT_OBSERVACION);

        // Get all the animalSaludList where observacion equals to UPDATED_OBSERVACION
        defaultAnimalSaludShouldNotBeFound("observacion.equals=" + UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByObservacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where observacion not equals to DEFAULT_OBSERVACION
        defaultAnimalSaludShouldNotBeFound("observacion.notEquals=" + DEFAULT_OBSERVACION);

        // Get all the animalSaludList where observacion not equals to UPDATED_OBSERVACION
        defaultAnimalSaludShouldBeFound("observacion.notEquals=" + UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByObservacionIsInShouldWork() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where observacion in DEFAULT_OBSERVACION or UPDATED_OBSERVACION
        defaultAnimalSaludShouldBeFound("observacion.in=" + DEFAULT_OBSERVACION + "," + UPDATED_OBSERVACION);

        // Get all the animalSaludList where observacion equals to UPDATED_OBSERVACION
        defaultAnimalSaludShouldNotBeFound("observacion.in=" + UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByObservacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where observacion is not null
        defaultAnimalSaludShouldBeFound("observacion.specified=true");

        // Get all the animalSaludList where observacion is null
        defaultAnimalSaludShouldNotBeFound("observacion.specified=false");
    }
                @Test
    @Transactional
    public void getAllAnimalSaludsByObservacionContainsSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where observacion contains DEFAULT_OBSERVACION
        defaultAnimalSaludShouldBeFound("observacion.contains=" + DEFAULT_OBSERVACION);

        // Get all the animalSaludList where observacion contains UPDATED_OBSERVACION
        defaultAnimalSaludShouldNotBeFound("observacion.contains=" + UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    public void getAllAnimalSaludsByObservacionNotContainsSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);

        // Get all the animalSaludList where observacion does not contain DEFAULT_OBSERVACION
        defaultAnimalSaludShouldNotBeFound("observacion.doesNotContain=" + DEFAULT_OBSERVACION);

        // Get all the animalSaludList where observacion does not contain UPDATED_OBSERVACION
        defaultAnimalSaludShouldBeFound("observacion.doesNotContain=" + UPDATED_OBSERVACION);
    }


    @Test
    @Transactional
    public void getAllAnimalSaludsByEventoIsEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);
        AnimalEvento evento = AnimalEventoResourceIT.createEntity(em);
        em.persist(evento);
        em.flush();
        animalSalud.setEvento(evento);
        animalSaludRepository.saveAndFlush(animalSalud);
        Long eventoId = evento.getId();

        // Get all the animalSaludList where evento equals to eventoId
        defaultAnimalSaludShouldBeFound("eventoId.equals=" + eventoId);

        // Get all the animalSaludList where evento equals to eventoId + 1
        defaultAnimalSaludShouldNotBeFound("eventoId.equals=" + (eventoId + 1));
    }


    @Test
    @Transactional
    public void getAllAnimalSaludsByMedicamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        animalSaludRepository.saveAndFlush(animalSalud);
        Parametros medicamento = ParametrosResourceIT.createEntity(em);
        em.persist(medicamento);
        em.flush();
        animalSalud.setMedicamento(medicamento);
        animalSaludRepository.saveAndFlush(animalSalud);
        Long medicamentoId = medicamento.getId();

        // Get all the animalSaludList where medicamento equals to medicamentoId
        defaultAnimalSaludShouldBeFound("medicamentoId.equals=" + medicamentoId);

        // Get all the animalSaludList where medicamento equals to medicamentoId + 1
        defaultAnimalSaludShouldNotBeFound("medicamentoId.equals=" + (medicamentoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnimalSaludShouldBeFound(String filter) throws Exception {
        restAnimalSaludMockMvc.perform(get("/api/animal-saluds?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalSalud.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].laboratorio").value(hasItem(DEFAULT_LABORATORIO)))
            .andExpect(jsonPath("$.[*].dosis").value(hasItem(DEFAULT_DOSIS.intValue())))
            .andExpect(jsonPath("$.[*].inyectado").value(hasItem(DEFAULT_INYECTADO.toString())))
            .andExpect(jsonPath("$.[*].intramuscular").value(hasItem(DEFAULT_INTRAMUSCULAR.toString())))
            .andExpect(jsonPath("$.[*].subcutaneo").value(hasItem(DEFAULT_SUBCUTANEO.toString())))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION)));

        // Check, that the count call also returns 1
        restAnimalSaludMockMvc.perform(get("/api/animal-saluds/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnimalSaludShouldNotBeFound(String filter) throws Exception {
        restAnimalSaludMockMvc.perform(get("/api/animal-saluds?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnimalSaludMockMvc.perform(get("/api/animal-saluds/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAnimalSalud() throws Exception {
        // Get the animalSalud
        restAnimalSaludMockMvc.perform(get("/api/animal-saluds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnimalSalud() throws Exception {
        // Initialize the database
        animalSaludService.save(animalSalud);

        int databaseSizeBeforeUpdate = animalSaludRepository.findAll().size();

        // Update the animalSalud
        AnimalSalud updatedAnimalSalud = animalSaludRepository.findById(animalSalud.getId()).get();
        // Disconnect from session so that the updates on updatedAnimalSalud are not directly saved in db
        em.detach(updatedAnimalSalud);
        updatedAnimalSalud
            .fecha(UPDATED_FECHA)
            .nombre(UPDATED_NOMBRE)
            .laboratorio(UPDATED_LABORATORIO)
            .dosis(UPDATED_DOSIS)
            .inyectado(UPDATED_INYECTADO)
            .intramuscular(UPDATED_INTRAMUSCULAR)
            .subcutaneo(UPDATED_SUBCUTANEO)
            .observacion(UPDATED_OBSERVACION);

        restAnimalSaludMockMvc.perform(put("/api/animal-saluds").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnimalSalud)))
            .andExpect(status().isOk());

        // Validate the AnimalSalud in the database
        List<AnimalSalud> animalSaludList = animalSaludRepository.findAll();
        assertThat(animalSaludList).hasSize(databaseSizeBeforeUpdate);
        AnimalSalud testAnimalSalud = animalSaludList.get(animalSaludList.size() - 1);
        assertThat(testAnimalSalud.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testAnimalSalud.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAnimalSalud.getLaboratorio()).isEqualTo(UPDATED_LABORATORIO);
        assertThat(testAnimalSalud.getDosis()).isEqualTo(UPDATED_DOSIS);
        assertThat(testAnimalSalud.getInyectado()).isEqualTo(UPDATED_INYECTADO);
        assertThat(testAnimalSalud.getIntramuscular()).isEqualTo(UPDATED_INTRAMUSCULAR);
        assertThat(testAnimalSalud.getSubcutaneo()).isEqualTo(UPDATED_SUBCUTANEO);
        assertThat(testAnimalSalud.getObservacion()).isEqualTo(UPDATED_OBSERVACION);

        // Validate the AnimalSalud in Elasticsearch
        verify(mockAnimalSaludSearchRepository, times(2)).save(testAnimalSalud);
    }

    @Test
    @Transactional
    public void updateNonExistingAnimalSalud() throws Exception {
        int databaseSizeBeforeUpdate = animalSaludRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnimalSaludMockMvc.perform(put("/api/animal-saluds").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalSalud)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalSalud in the database
        List<AnimalSalud> animalSaludList = animalSaludRepository.findAll();
        assertThat(animalSaludList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnimalSalud in Elasticsearch
        verify(mockAnimalSaludSearchRepository, times(0)).save(animalSalud);
    }

    @Test
    @Transactional
    public void deleteAnimalSalud() throws Exception {
        // Initialize the database
        animalSaludService.save(animalSalud);

        int databaseSizeBeforeDelete = animalSaludRepository.findAll().size();

        // Delete the animalSalud
        restAnimalSaludMockMvc.perform(delete("/api/animal-saluds/{id}", animalSalud.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnimalSalud> animalSaludList = animalSaludRepository.findAll();
        assertThat(animalSaludList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnimalSalud in Elasticsearch
        verify(mockAnimalSaludSearchRepository, times(1)).deleteById(animalSalud.getId());
    }

    @Test
    @Transactional
    public void searchAnimalSalud() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        animalSaludService.save(animalSalud);
        when(mockAnimalSaludSearchRepository.search(queryStringQuery("id:" + animalSalud.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(animalSalud), PageRequest.of(0, 1), 1));

        // Search the animalSalud
        restAnimalSaludMockMvc.perform(get("/api/_search/animal-saluds?query=id:" + animalSalud.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalSalud.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].laboratorio").value(hasItem(DEFAULT_LABORATORIO)))
            .andExpect(jsonPath("$.[*].dosis").value(hasItem(DEFAULT_DOSIS.intValue())))
            .andExpect(jsonPath("$.[*].inyectado").value(hasItem(DEFAULT_INYECTADO.toString())))
            .andExpect(jsonPath("$.[*].intramuscular").value(hasItem(DEFAULT_INTRAMUSCULAR.toString())))
            .andExpect(jsonPath("$.[*].subcutaneo").value(hasItem(DEFAULT_SUBCUTANEO.toString())))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION)));
    }
}
