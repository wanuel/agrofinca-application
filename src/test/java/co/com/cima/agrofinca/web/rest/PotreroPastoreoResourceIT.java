package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.PotreroPastoreo;
import co.com.cima.agrofinca.domain.Lote;
import co.com.cima.agrofinca.domain.Potrero;
import co.com.cima.agrofinca.repository.PotreroPastoreoRepository;
import co.com.cima.agrofinca.repository.search.PotreroPastoreoSearchRepository;
import co.com.cima.agrofinca.service.PotreroPastoreoService;
import co.com.cima.agrofinca.service.dto.PotreroPastoreoCriteria;
import co.com.cima.agrofinca.service.PotreroPastoreoQueryService;

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

import co.com.cima.agrofinca.domain.enumeration.SINO;
/**
 * Integration tests for the {@link PotreroPastoreoResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PotreroPastoreoResourceIT {

    private static final LocalDate DEFAULT_FECHA_INGRESO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INGRESO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_INGRESO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_SALIDA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_SALIDA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_SALIDA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_LIMPIA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_LIMPIA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_LIMPIA = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_DIAS_DESCANSO = 1;
    private static final Integer UPDATED_DIAS_DESCANSO = 2;
    private static final Integer SMALLER_DIAS_DESCANSO = 1 - 1;

    private static final Integer DEFAULT_DIAS_CARGA = 1;
    private static final Integer UPDATED_DIAS_CARGA = 2;
    private static final Integer SMALLER_DIAS_CARGA = 1 - 1;

    private static final SINO DEFAULT_LIMPIA = SINO.SI;
    private static final SINO UPDATED_LIMPIA = SINO.NO;

    @Autowired
    private PotreroPastoreoRepository potreroPastoreoRepository;

    @Autowired
    private PotreroPastoreoService potreroPastoreoService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.PotreroPastoreoSearchRepositoryMockConfiguration
     */
    @Autowired
    private PotreroPastoreoSearchRepository mockPotreroPastoreoSearchRepository;

    @Autowired
    private PotreroPastoreoQueryService potreroPastoreoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPotreroPastoreoMockMvc;

    private PotreroPastoreo potreroPastoreo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PotreroPastoreo createEntity(EntityManager em) {
        PotreroPastoreo potreroPastoreo = new PotreroPastoreo()
            .fechaIngreso(DEFAULT_FECHA_INGRESO)
            .fechaSalida(DEFAULT_FECHA_SALIDA)
            .fechaLimpia(DEFAULT_FECHA_LIMPIA)
            .diasDescanso(DEFAULT_DIAS_DESCANSO)
            .diasCarga(DEFAULT_DIAS_CARGA)
            .limpia(DEFAULT_LIMPIA);
        return potreroPastoreo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PotreroPastoreo createUpdatedEntity(EntityManager em) {
        PotreroPastoreo potreroPastoreo = new PotreroPastoreo()
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .fechaSalida(UPDATED_FECHA_SALIDA)
            .fechaLimpia(UPDATED_FECHA_LIMPIA)
            .diasDescanso(UPDATED_DIAS_DESCANSO)
            .diasCarga(UPDATED_DIAS_CARGA)
            .limpia(UPDATED_LIMPIA);
        return potreroPastoreo;
    }

    @BeforeEach
    public void initTest() {
        potreroPastoreo = createEntity(em);
    }

    @Test
    @Transactional
    public void createPotreroPastoreo() throws Exception {
        int databaseSizeBeforeCreate = potreroPastoreoRepository.findAll().size();
        // Create the PotreroPastoreo
        restPotreroPastoreoMockMvc.perform(post("/api/potrero-pastoreos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(potreroPastoreo)))
            .andExpect(status().isCreated());

        // Validate the PotreroPastoreo in the database
        List<PotreroPastoreo> potreroPastoreoList = potreroPastoreoRepository.findAll();
        assertThat(potreroPastoreoList).hasSize(databaseSizeBeforeCreate + 1);
        PotreroPastoreo testPotreroPastoreo = potreroPastoreoList.get(potreroPastoreoList.size() - 1);
        assertThat(testPotreroPastoreo.getFechaIngreso()).isEqualTo(DEFAULT_FECHA_INGRESO);
        assertThat(testPotreroPastoreo.getFechaSalida()).isEqualTo(DEFAULT_FECHA_SALIDA);
        assertThat(testPotreroPastoreo.getFechaLimpia()).isEqualTo(DEFAULT_FECHA_LIMPIA);
        assertThat(testPotreroPastoreo.getDiasDescanso()).isEqualTo(DEFAULT_DIAS_DESCANSO);
        assertThat(testPotreroPastoreo.getDiasCarga()).isEqualTo(DEFAULT_DIAS_CARGA);
        assertThat(testPotreroPastoreo.getLimpia()).isEqualTo(DEFAULT_LIMPIA);

        // Validate the PotreroPastoreo in Elasticsearch
        verify(mockPotreroPastoreoSearchRepository, times(1)).save(testPotreroPastoreo);
    }

    @Test
    @Transactional
    public void createPotreroPastoreoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = potreroPastoreoRepository.findAll().size();

        // Create the PotreroPastoreo with an existing ID
        potreroPastoreo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPotreroPastoreoMockMvc.perform(post("/api/potrero-pastoreos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(potreroPastoreo)))
            .andExpect(status().isBadRequest());

        // Validate the PotreroPastoreo in the database
        List<PotreroPastoreo> potreroPastoreoList = potreroPastoreoRepository.findAll();
        assertThat(potreroPastoreoList).hasSize(databaseSizeBeforeCreate);

        // Validate the PotreroPastoreo in Elasticsearch
        verify(mockPotreroPastoreoSearchRepository, times(0)).save(potreroPastoreo);
    }


    @Test
    @Transactional
    public void checkFechaIngresoIsRequired() throws Exception {
        int databaseSizeBeforeTest = potreroPastoreoRepository.findAll().size();
        // set the field null
        potreroPastoreo.setFechaIngreso(null);

        // Create the PotreroPastoreo, which fails.


        restPotreroPastoreoMockMvc.perform(post("/api/potrero-pastoreos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(potreroPastoreo)))
            .andExpect(status().isBadRequest());

        List<PotreroPastoreo> potreroPastoreoList = potreroPastoreoRepository.findAll();
        assertThat(potreroPastoreoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLimpiaIsRequired() throws Exception {
        int databaseSizeBeforeTest = potreroPastoreoRepository.findAll().size();
        // set the field null
        potreroPastoreo.setLimpia(null);

        // Create the PotreroPastoreo, which fails.


        restPotreroPastoreoMockMvc.perform(post("/api/potrero-pastoreos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(potreroPastoreo)))
            .andExpect(status().isBadRequest());

        List<PotreroPastoreo> potreroPastoreoList = potreroPastoreoRepository.findAll();
        assertThat(potreroPastoreoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreos() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList
        restPotreroPastoreoMockMvc.perform(get("/api/potrero-pastoreos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(potreroPastoreo.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].fechaSalida").value(hasItem(DEFAULT_FECHA_SALIDA.toString())))
            .andExpect(jsonPath("$.[*].fechaLimpia").value(hasItem(DEFAULT_FECHA_LIMPIA.toString())))
            .andExpect(jsonPath("$.[*].diasDescanso").value(hasItem(DEFAULT_DIAS_DESCANSO)))
            .andExpect(jsonPath("$.[*].diasCarga").value(hasItem(DEFAULT_DIAS_CARGA)))
            .andExpect(jsonPath("$.[*].limpia").value(hasItem(DEFAULT_LIMPIA.toString())));
    }
    
    @Test
    @Transactional
    public void getPotreroPastoreo() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get the potreroPastoreo
        restPotreroPastoreoMockMvc.perform(get("/api/potrero-pastoreos/{id}", potreroPastoreo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(potreroPastoreo.getId().intValue()))
            .andExpect(jsonPath("$.fechaIngreso").value(DEFAULT_FECHA_INGRESO.toString()))
            .andExpect(jsonPath("$.fechaSalida").value(DEFAULT_FECHA_SALIDA.toString()))
            .andExpect(jsonPath("$.fechaLimpia").value(DEFAULT_FECHA_LIMPIA.toString()))
            .andExpect(jsonPath("$.diasDescanso").value(DEFAULT_DIAS_DESCANSO))
            .andExpect(jsonPath("$.diasCarga").value(DEFAULT_DIAS_CARGA))
            .andExpect(jsonPath("$.limpia").value(DEFAULT_LIMPIA.toString()));
    }


    @Test
    @Transactional
    public void getPotreroPastoreosByIdFiltering() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        Long id = potreroPastoreo.getId();

        defaultPotreroPastoreoShouldBeFound("id.equals=" + id);
        defaultPotreroPastoreoShouldNotBeFound("id.notEquals=" + id);

        defaultPotreroPastoreoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPotreroPastoreoShouldNotBeFound("id.greaterThan=" + id);

        defaultPotreroPastoreoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPotreroPastoreoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaIngresoIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaIngreso equals to DEFAULT_FECHA_INGRESO
        defaultPotreroPastoreoShouldBeFound("fechaIngreso.equals=" + DEFAULT_FECHA_INGRESO);

        // Get all the potreroPastoreoList where fechaIngreso equals to UPDATED_FECHA_INGRESO
        defaultPotreroPastoreoShouldNotBeFound("fechaIngreso.equals=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaIngresoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaIngreso not equals to DEFAULT_FECHA_INGRESO
        defaultPotreroPastoreoShouldNotBeFound("fechaIngreso.notEquals=" + DEFAULT_FECHA_INGRESO);

        // Get all the potreroPastoreoList where fechaIngreso not equals to UPDATED_FECHA_INGRESO
        defaultPotreroPastoreoShouldBeFound("fechaIngreso.notEquals=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaIngresoIsInShouldWork() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaIngreso in DEFAULT_FECHA_INGRESO or UPDATED_FECHA_INGRESO
        defaultPotreroPastoreoShouldBeFound("fechaIngreso.in=" + DEFAULT_FECHA_INGRESO + "," + UPDATED_FECHA_INGRESO);

        // Get all the potreroPastoreoList where fechaIngreso equals to UPDATED_FECHA_INGRESO
        defaultPotreroPastoreoShouldNotBeFound("fechaIngreso.in=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaIngresoIsNullOrNotNull() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaIngreso is not null
        defaultPotreroPastoreoShouldBeFound("fechaIngreso.specified=true");

        // Get all the potreroPastoreoList where fechaIngreso is null
        defaultPotreroPastoreoShouldNotBeFound("fechaIngreso.specified=false");
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaIngresoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaIngreso is greater than or equal to DEFAULT_FECHA_INGRESO
        defaultPotreroPastoreoShouldBeFound("fechaIngreso.greaterThanOrEqual=" + DEFAULT_FECHA_INGRESO);

        // Get all the potreroPastoreoList where fechaIngreso is greater than or equal to UPDATED_FECHA_INGRESO
        defaultPotreroPastoreoShouldNotBeFound("fechaIngreso.greaterThanOrEqual=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaIngresoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaIngreso is less than or equal to DEFAULT_FECHA_INGRESO
        defaultPotreroPastoreoShouldBeFound("fechaIngreso.lessThanOrEqual=" + DEFAULT_FECHA_INGRESO);

        // Get all the potreroPastoreoList where fechaIngreso is less than or equal to SMALLER_FECHA_INGRESO
        defaultPotreroPastoreoShouldNotBeFound("fechaIngreso.lessThanOrEqual=" + SMALLER_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaIngresoIsLessThanSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaIngreso is less than DEFAULT_FECHA_INGRESO
        defaultPotreroPastoreoShouldNotBeFound("fechaIngreso.lessThan=" + DEFAULT_FECHA_INGRESO);

        // Get all the potreroPastoreoList where fechaIngreso is less than UPDATED_FECHA_INGRESO
        defaultPotreroPastoreoShouldBeFound("fechaIngreso.lessThan=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaIngresoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaIngreso is greater than DEFAULT_FECHA_INGRESO
        defaultPotreroPastoreoShouldNotBeFound("fechaIngreso.greaterThan=" + DEFAULT_FECHA_INGRESO);

        // Get all the potreroPastoreoList where fechaIngreso is greater than SMALLER_FECHA_INGRESO
        defaultPotreroPastoreoShouldBeFound("fechaIngreso.greaterThan=" + SMALLER_FECHA_INGRESO);
    }


    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaSalidaIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaSalida equals to DEFAULT_FECHA_SALIDA
        defaultPotreroPastoreoShouldBeFound("fechaSalida.equals=" + DEFAULT_FECHA_SALIDA);

        // Get all the potreroPastoreoList where fechaSalida equals to UPDATED_FECHA_SALIDA
        defaultPotreroPastoreoShouldNotBeFound("fechaSalida.equals=" + UPDATED_FECHA_SALIDA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaSalidaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaSalida not equals to DEFAULT_FECHA_SALIDA
        defaultPotreroPastoreoShouldNotBeFound("fechaSalida.notEquals=" + DEFAULT_FECHA_SALIDA);

        // Get all the potreroPastoreoList where fechaSalida not equals to UPDATED_FECHA_SALIDA
        defaultPotreroPastoreoShouldBeFound("fechaSalida.notEquals=" + UPDATED_FECHA_SALIDA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaSalidaIsInShouldWork() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaSalida in DEFAULT_FECHA_SALIDA or UPDATED_FECHA_SALIDA
        defaultPotreroPastoreoShouldBeFound("fechaSalida.in=" + DEFAULT_FECHA_SALIDA + "," + UPDATED_FECHA_SALIDA);

        // Get all the potreroPastoreoList where fechaSalida equals to UPDATED_FECHA_SALIDA
        defaultPotreroPastoreoShouldNotBeFound("fechaSalida.in=" + UPDATED_FECHA_SALIDA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaSalidaIsNullOrNotNull() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaSalida is not null
        defaultPotreroPastoreoShouldBeFound("fechaSalida.specified=true");

        // Get all the potreroPastoreoList where fechaSalida is null
        defaultPotreroPastoreoShouldNotBeFound("fechaSalida.specified=false");
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaSalidaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaSalida is greater than or equal to DEFAULT_FECHA_SALIDA
        defaultPotreroPastoreoShouldBeFound("fechaSalida.greaterThanOrEqual=" + DEFAULT_FECHA_SALIDA);

        // Get all the potreroPastoreoList where fechaSalida is greater than or equal to UPDATED_FECHA_SALIDA
        defaultPotreroPastoreoShouldNotBeFound("fechaSalida.greaterThanOrEqual=" + UPDATED_FECHA_SALIDA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaSalidaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaSalida is less than or equal to DEFAULT_FECHA_SALIDA
        defaultPotreroPastoreoShouldBeFound("fechaSalida.lessThanOrEqual=" + DEFAULT_FECHA_SALIDA);

        // Get all the potreroPastoreoList where fechaSalida is less than or equal to SMALLER_FECHA_SALIDA
        defaultPotreroPastoreoShouldNotBeFound("fechaSalida.lessThanOrEqual=" + SMALLER_FECHA_SALIDA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaSalidaIsLessThanSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaSalida is less than DEFAULT_FECHA_SALIDA
        defaultPotreroPastoreoShouldNotBeFound("fechaSalida.lessThan=" + DEFAULT_FECHA_SALIDA);

        // Get all the potreroPastoreoList where fechaSalida is less than UPDATED_FECHA_SALIDA
        defaultPotreroPastoreoShouldBeFound("fechaSalida.lessThan=" + UPDATED_FECHA_SALIDA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaSalidaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaSalida is greater than DEFAULT_FECHA_SALIDA
        defaultPotreroPastoreoShouldNotBeFound("fechaSalida.greaterThan=" + DEFAULT_FECHA_SALIDA);

        // Get all the potreroPastoreoList where fechaSalida is greater than SMALLER_FECHA_SALIDA
        defaultPotreroPastoreoShouldBeFound("fechaSalida.greaterThan=" + SMALLER_FECHA_SALIDA);
    }


    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaLimpiaIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaLimpia equals to DEFAULT_FECHA_LIMPIA
        defaultPotreroPastoreoShouldBeFound("fechaLimpia.equals=" + DEFAULT_FECHA_LIMPIA);

        // Get all the potreroPastoreoList where fechaLimpia equals to UPDATED_FECHA_LIMPIA
        defaultPotreroPastoreoShouldNotBeFound("fechaLimpia.equals=" + UPDATED_FECHA_LIMPIA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaLimpiaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaLimpia not equals to DEFAULT_FECHA_LIMPIA
        defaultPotreroPastoreoShouldNotBeFound("fechaLimpia.notEquals=" + DEFAULT_FECHA_LIMPIA);

        // Get all the potreroPastoreoList where fechaLimpia not equals to UPDATED_FECHA_LIMPIA
        defaultPotreroPastoreoShouldBeFound("fechaLimpia.notEquals=" + UPDATED_FECHA_LIMPIA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaLimpiaIsInShouldWork() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaLimpia in DEFAULT_FECHA_LIMPIA or UPDATED_FECHA_LIMPIA
        defaultPotreroPastoreoShouldBeFound("fechaLimpia.in=" + DEFAULT_FECHA_LIMPIA + "," + UPDATED_FECHA_LIMPIA);

        // Get all the potreroPastoreoList where fechaLimpia equals to UPDATED_FECHA_LIMPIA
        defaultPotreroPastoreoShouldNotBeFound("fechaLimpia.in=" + UPDATED_FECHA_LIMPIA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaLimpiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaLimpia is not null
        defaultPotreroPastoreoShouldBeFound("fechaLimpia.specified=true");

        // Get all the potreroPastoreoList where fechaLimpia is null
        defaultPotreroPastoreoShouldNotBeFound("fechaLimpia.specified=false");
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaLimpiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaLimpia is greater than or equal to DEFAULT_FECHA_LIMPIA
        defaultPotreroPastoreoShouldBeFound("fechaLimpia.greaterThanOrEqual=" + DEFAULT_FECHA_LIMPIA);

        // Get all the potreroPastoreoList where fechaLimpia is greater than or equal to UPDATED_FECHA_LIMPIA
        defaultPotreroPastoreoShouldNotBeFound("fechaLimpia.greaterThanOrEqual=" + UPDATED_FECHA_LIMPIA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaLimpiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaLimpia is less than or equal to DEFAULT_FECHA_LIMPIA
        defaultPotreroPastoreoShouldBeFound("fechaLimpia.lessThanOrEqual=" + DEFAULT_FECHA_LIMPIA);

        // Get all the potreroPastoreoList where fechaLimpia is less than or equal to SMALLER_FECHA_LIMPIA
        defaultPotreroPastoreoShouldNotBeFound("fechaLimpia.lessThanOrEqual=" + SMALLER_FECHA_LIMPIA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaLimpiaIsLessThanSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaLimpia is less than DEFAULT_FECHA_LIMPIA
        defaultPotreroPastoreoShouldNotBeFound("fechaLimpia.lessThan=" + DEFAULT_FECHA_LIMPIA);

        // Get all the potreroPastoreoList where fechaLimpia is less than UPDATED_FECHA_LIMPIA
        defaultPotreroPastoreoShouldBeFound("fechaLimpia.lessThan=" + UPDATED_FECHA_LIMPIA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByFechaLimpiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where fechaLimpia is greater than DEFAULT_FECHA_LIMPIA
        defaultPotreroPastoreoShouldNotBeFound("fechaLimpia.greaterThan=" + DEFAULT_FECHA_LIMPIA);

        // Get all the potreroPastoreoList where fechaLimpia is greater than SMALLER_FECHA_LIMPIA
        defaultPotreroPastoreoShouldBeFound("fechaLimpia.greaterThan=" + SMALLER_FECHA_LIMPIA);
    }


    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasDescansoIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasDescanso equals to DEFAULT_DIAS_DESCANSO
        defaultPotreroPastoreoShouldBeFound("diasDescanso.equals=" + DEFAULT_DIAS_DESCANSO);

        // Get all the potreroPastoreoList where diasDescanso equals to UPDATED_DIAS_DESCANSO
        defaultPotreroPastoreoShouldNotBeFound("diasDescanso.equals=" + UPDATED_DIAS_DESCANSO);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasDescansoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasDescanso not equals to DEFAULT_DIAS_DESCANSO
        defaultPotreroPastoreoShouldNotBeFound("diasDescanso.notEquals=" + DEFAULT_DIAS_DESCANSO);

        // Get all the potreroPastoreoList where diasDescanso not equals to UPDATED_DIAS_DESCANSO
        defaultPotreroPastoreoShouldBeFound("diasDescanso.notEquals=" + UPDATED_DIAS_DESCANSO);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasDescansoIsInShouldWork() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasDescanso in DEFAULT_DIAS_DESCANSO or UPDATED_DIAS_DESCANSO
        defaultPotreroPastoreoShouldBeFound("diasDescanso.in=" + DEFAULT_DIAS_DESCANSO + "," + UPDATED_DIAS_DESCANSO);

        // Get all the potreroPastoreoList where diasDescanso equals to UPDATED_DIAS_DESCANSO
        defaultPotreroPastoreoShouldNotBeFound("diasDescanso.in=" + UPDATED_DIAS_DESCANSO);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasDescansoIsNullOrNotNull() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasDescanso is not null
        defaultPotreroPastoreoShouldBeFound("diasDescanso.specified=true");

        // Get all the potreroPastoreoList where diasDescanso is null
        defaultPotreroPastoreoShouldNotBeFound("diasDescanso.specified=false");
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasDescansoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasDescanso is greater than or equal to DEFAULT_DIAS_DESCANSO
        defaultPotreroPastoreoShouldBeFound("diasDescanso.greaterThanOrEqual=" + DEFAULT_DIAS_DESCANSO);

        // Get all the potreroPastoreoList where diasDescanso is greater than or equal to UPDATED_DIAS_DESCANSO
        defaultPotreroPastoreoShouldNotBeFound("diasDescanso.greaterThanOrEqual=" + UPDATED_DIAS_DESCANSO);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasDescansoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasDescanso is less than or equal to DEFAULT_DIAS_DESCANSO
        defaultPotreroPastoreoShouldBeFound("diasDescanso.lessThanOrEqual=" + DEFAULT_DIAS_DESCANSO);

        // Get all the potreroPastoreoList where diasDescanso is less than or equal to SMALLER_DIAS_DESCANSO
        defaultPotreroPastoreoShouldNotBeFound("diasDescanso.lessThanOrEqual=" + SMALLER_DIAS_DESCANSO);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasDescansoIsLessThanSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasDescanso is less than DEFAULT_DIAS_DESCANSO
        defaultPotreroPastoreoShouldNotBeFound("diasDescanso.lessThan=" + DEFAULT_DIAS_DESCANSO);

        // Get all the potreroPastoreoList where diasDescanso is less than UPDATED_DIAS_DESCANSO
        defaultPotreroPastoreoShouldBeFound("diasDescanso.lessThan=" + UPDATED_DIAS_DESCANSO);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasDescansoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasDescanso is greater than DEFAULT_DIAS_DESCANSO
        defaultPotreroPastoreoShouldNotBeFound("diasDescanso.greaterThan=" + DEFAULT_DIAS_DESCANSO);

        // Get all the potreroPastoreoList where diasDescanso is greater than SMALLER_DIAS_DESCANSO
        defaultPotreroPastoreoShouldBeFound("diasDescanso.greaterThan=" + SMALLER_DIAS_DESCANSO);
    }


    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasCargaIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasCarga equals to DEFAULT_DIAS_CARGA
        defaultPotreroPastoreoShouldBeFound("diasCarga.equals=" + DEFAULT_DIAS_CARGA);

        // Get all the potreroPastoreoList where diasCarga equals to UPDATED_DIAS_CARGA
        defaultPotreroPastoreoShouldNotBeFound("diasCarga.equals=" + UPDATED_DIAS_CARGA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasCargaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasCarga not equals to DEFAULT_DIAS_CARGA
        defaultPotreroPastoreoShouldNotBeFound("diasCarga.notEquals=" + DEFAULT_DIAS_CARGA);

        // Get all the potreroPastoreoList where diasCarga not equals to UPDATED_DIAS_CARGA
        defaultPotreroPastoreoShouldBeFound("diasCarga.notEquals=" + UPDATED_DIAS_CARGA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasCargaIsInShouldWork() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasCarga in DEFAULT_DIAS_CARGA or UPDATED_DIAS_CARGA
        defaultPotreroPastoreoShouldBeFound("diasCarga.in=" + DEFAULT_DIAS_CARGA + "," + UPDATED_DIAS_CARGA);

        // Get all the potreroPastoreoList where diasCarga equals to UPDATED_DIAS_CARGA
        defaultPotreroPastoreoShouldNotBeFound("diasCarga.in=" + UPDATED_DIAS_CARGA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasCargaIsNullOrNotNull() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasCarga is not null
        defaultPotreroPastoreoShouldBeFound("diasCarga.specified=true");

        // Get all the potreroPastoreoList where diasCarga is null
        defaultPotreroPastoreoShouldNotBeFound("diasCarga.specified=false");
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasCargaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasCarga is greater than or equal to DEFAULT_DIAS_CARGA
        defaultPotreroPastoreoShouldBeFound("diasCarga.greaterThanOrEqual=" + DEFAULT_DIAS_CARGA);

        // Get all the potreroPastoreoList where diasCarga is greater than or equal to UPDATED_DIAS_CARGA
        defaultPotreroPastoreoShouldNotBeFound("diasCarga.greaterThanOrEqual=" + UPDATED_DIAS_CARGA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasCargaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasCarga is less than or equal to DEFAULT_DIAS_CARGA
        defaultPotreroPastoreoShouldBeFound("diasCarga.lessThanOrEqual=" + DEFAULT_DIAS_CARGA);

        // Get all the potreroPastoreoList where diasCarga is less than or equal to SMALLER_DIAS_CARGA
        defaultPotreroPastoreoShouldNotBeFound("diasCarga.lessThanOrEqual=" + SMALLER_DIAS_CARGA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasCargaIsLessThanSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasCarga is less than DEFAULT_DIAS_CARGA
        defaultPotreroPastoreoShouldNotBeFound("diasCarga.lessThan=" + DEFAULT_DIAS_CARGA);

        // Get all the potreroPastoreoList where diasCarga is less than UPDATED_DIAS_CARGA
        defaultPotreroPastoreoShouldBeFound("diasCarga.lessThan=" + UPDATED_DIAS_CARGA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByDiasCargaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where diasCarga is greater than DEFAULT_DIAS_CARGA
        defaultPotreroPastoreoShouldNotBeFound("diasCarga.greaterThan=" + DEFAULT_DIAS_CARGA);

        // Get all the potreroPastoreoList where diasCarga is greater than SMALLER_DIAS_CARGA
        defaultPotreroPastoreoShouldBeFound("diasCarga.greaterThan=" + SMALLER_DIAS_CARGA);
    }


    @Test
    @Transactional
    public void getAllPotreroPastoreosByLimpiaIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where limpia equals to DEFAULT_LIMPIA
        defaultPotreroPastoreoShouldBeFound("limpia.equals=" + DEFAULT_LIMPIA);

        // Get all the potreroPastoreoList where limpia equals to UPDATED_LIMPIA
        defaultPotreroPastoreoShouldNotBeFound("limpia.equals=" + UPDATED_LIMPIA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByLimpiaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where limpia not equals to DEFAULT_LIMPIA
        defaultPotreroPastoreoShouldNotBeFound("limpia.notEquals=" + DEFAULT_LIMPIA);

        // Get all the potreroPastoreoList where limpia not equals to UPDATED_LIMPIA
        defaultPotreroPastoreoShouldBeFound("limpia.notEquals=" + UPDATED_LIMPIA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByLimpiaIsInShouldWork() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where limpia in DEFAULT_LIMPIA or UPDATED_LIMPIA
        defaultPotreroPastoreoShouldBeFound("limpia.in=" + DEFAULT_LIMPIA + "," + UPDATED_LIMPIA);

        // Get all the potreroPastoreoList where limpia equals to UPDATED_LIMPIA
        defaultPotreroPastoreoShouldNotBeFound("limpia.in=" + UPDATED_LIMPIA);
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByLimpiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);

        // Get all the potreroPastoreoList where limpia is not null
        defaultPotreroPastoreoShouldBeFound("limpia.specified=true");

        // Get all the potreroPastoreoList where limpia is null
        defaultPotreroPastoreoShouldNotBeFound("limpia.specified=false");
    }

    @Test
    @Transactional
    public void getAllPotreroPastoreosByLoteIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);
        Lote lote = LoteResourceIT.createEntity(em);
        em.persist(lote);
        em.flush();
        potreroPastoreo.setLote(lote);
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);
        Long loteId = lote.getId();

        // Get all the potreroPastoreoList where lote equals to loteId
        defaultPotreroPastoreoShouldBeFound("loteId.equals=" + loteId);

        // Get all the potreroPastoreoList where lote equals to loteId + 1
        defaultPotreroPastoreoShouldNotBeFound("loteId.equals=" + (loteId + 1));
    }


    @Test
    @Transactional
    public void getAllPotreroPastoreosByPotreroIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);
        Potrero potrero = PotreroResourceIT.createEntity(em);
        em.persist(potrero);
        em.flush();
        potreroPastoreo.setPotrero(potrero);
        potreroPastoreoRepository.saveAndFlush(potreroPastoreo);
        Long potreroId = potrero.getId();

        // Get all the potreroPastoreoList where potrero equals to potreroId
        defaultPotreroPastoreoShouldBeFound("potreroId.equals=" + potreroId);

        // Get all the potreroPastoreoList where potrero equals to potreroId + 1
        defaultPotreroPastoreoShouldNotBeFound("potreroId.equals=" + (potreroId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPotreroPastoreoShouldBeFound(String filter) throws Exception {
        restPotreroPastoreoMockMvc.perform(get("/api/potrero-pastoreos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(potreroPastoreo.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].fechaSalida").value(hasItem(DEFAULT_FECHA_SALIDA.toString())))
            .andExpect(jsonPath("$.[*].fechaLimpia").value(hasItem(DEFAULT_FECHA_LIMPIA.toString())))
            .andExpect(jsonPath("$.[*].diasDescanso").value(hasItem(DEFAULT_DIAS_DESCANSO)))
            .andExpect(jsonPath("$.[*].diasCarga").value(hasItem(DEFAULT_DIAS_CARGA)))
            .andExpect(jsonPath("$.[*].limpia").value(hasItem(DEFAULT_LIMPIA.toString())));

        // Check, that the count call also returns 1
        restPotreroPastoreoMockMvc.perform(get("/api/potrero-pastoreos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPotreroPastoreoShouldNotBeFound(String filter) throws Exception {
        restPotreroPastoreoMockMvc.perform(get("/api/potrero-pastoreos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPotreroPastoreoMockMvc.perform(get("/api/potrero-pastoreos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPotreroPastoreo() throws Exception {
        // Get the potreroPastoreo
        restPotreroPastoreoMockMvc.perform(get("/api/potrero-pastoreos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePotreroPastoreo() throws Exception {
        // Initialize the database
        potreroPastoreoService.save(potreroPastoreo);

        int databaseSizeBeforeUpdate = potreroPastoreoRepository.findAll().size();

        // Update the potreroPastoreo
        PotreroPastoreo updatedPotreroPastoreo = potreroPastoreoRepository.findById(potreroPastoreo.getId()).get();
        // Disconnect from session so that the updates on updatedPotreroPastoreo are not directly saved in db
        em.detach(updatedPotreroPastoreo);
        updatedPotreroPastoreo
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .fechaSalida(UPDATED_FECHA_SALIDA)
            .fechaLimpia(UPDATED_FECHA_LIMPIA)
            .diasDescanso(UPDATED_DIAS_DESCANSO)
            .diasCarga(UPDATED_DIAS_CARGA)
            .limpia(UPDATED_LIMPIA);

        restPotreroPastoreoMockMvc.perform(put("/api/potrero-pastoreos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPotreroPastoreo)))
            .andExpect(status().isOk());

        // Validate the PotreroPastoreo in the database
        List<PotreroPastoreo> potreroPastoreoList = potreroPastoreoRepository.findAll();
        assertThat(potreroPastoreoList).hasSize(databaseSizeBeforeUpdate);
        PotreroPastoreo testPotreroPastoreo = potreroPastoreoList.get(potreroPastoreoList.size() - 1);
        assertThat(testPotreroPastoreo.getFechaIngreso()).isEqualTo(UPDATED_FECHA_INGRESO);
        assertThat(testPotreroPastoreo.getFechaSalida()).isEqualTo(UPDATED_FECHA_SALIDA);
        assertThat(testPotreroPastoreo.getFechaLimpia()).isEqualTo(UPDATED_FECHA_LIMPIA);
        assertThat(testPotreroPastoreo.getDiasDescanso()).isEqualTo(UPDATED_DIAS_DESCANSO);
        assertThat(testPotreroPastoreo.getDiasCarga()).isEqualTo(UPDATED_DIAS_CARGA);
        assertThat(testPotreroPastoreo.getLimpia()).isEqualTo(UPDATED_LIMPIA);

        // Validate the PotreroPastoreo in Elasticsearch
        verify(mockPotreroPastoreoSearchRepository, times(2)).save(testPotreroPastoreo);
    }

    @Test
    @Transactional
    public void updateNonExistingPotreroPastoreo() throws Exception {
        int databaseSizeBeforeUpdate = potreroPastoreoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPotreroPastoreoMockMvc.perform(put("/api/potrero-pastoreos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(potreroPastoreo)))
            .andExpect(status().isBadRequest());

        // Validate the PotreroPastoreo in the database
        List<PotreroPastoreo> potreroPastoreoList = potreroPastoreoRepository.findAll();
        assertThat(potreroPastoreoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PotreroPastoreo in Elasticsearch
        verify(mockPotreroPastoreoSearchRepository, times(0)).save(potreroPastoreo);
    }

    @Test
    @Transactional
    public void deletePotreroPastoreo() throws Exception {
        // Initialize the database
        potreroPastoreoService.save(potreroPastoreo);

        int databaseSizeBeforeDelete = potreroPastoreoRepository.findAll().size();

        // Delete the potreroPastoreo
        restPotreroPastoreoMockMvc.perform(delete("/api/potrero-pastoreos/{id}", potreroPastoreo.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PotreroPastoreo> potreroPastoreoList = potreroPastoreoRepository.findAll();
        assertThat(potreroPastoreoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PotreroPastoreo in Elasticsearch
        verify(mockPotreroPastoreoSearchRepository, times(1)).deleteById(potreroPastoreo.getId());
    }

    @Test
    @Transactional
    public void searchPotreroPastoreo() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        potreroPastoreoService.save(potreroPastoreo);
        when(mockPotreroPastoreoSearchRepository.search(queryStringQuery("id:" + potreroPastoreo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(potreroPastoreo), PageRequest.of(0, 1), 1));

        // Search the potreroPastoreo
        restPotreroPastoreoMockMvc.perform(get("/api/_search/potrero-pastoreos?query=id:" + potreroPastoreo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(potreroPastoreo.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].fechaSalida").value(hasItem(DEFAULT_FECHA_SALIDA.toString())))
            .andExpect(jsonPath("$.[*].fechaLimpia").value(hasItem(DEFAULT_FECHA_LIMPIA.toString())))
            .andExpect(jsonPath("$.[*].diasDescanso").value(hasItem(DEFAULT_DIAS_DESCANSO)))
            .andExpect(jsonPath("$.[*].diasCarga").value(hasItem(DEFAULT_DIAS_CARGA)))
            .andExpect(jsonPath("$.[*].limpia").value(hasItem(DEFAULT_LIMPIA.toString())));
    }
}
