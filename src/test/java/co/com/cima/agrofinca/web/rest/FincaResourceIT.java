package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Finca;
import co.com.cima.agrofinca.domain.Potrero;
import co.com.cima.agrofinca.repository.FincaRepository;
import co.com.cima.agrofinca.repository.search.FincaSearchRepository;
import co.com.cima.agrofinca.service.FincaService;
import co.com.cima.agrofinca.service.dto.FincaCriteria;
import co.com.cima.agrofinca.service.FincaQueryService;

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
 * Integration tests for the {@link FincaResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class FincaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_AREA = new BigDecimal(2);
    private static final BigDecimal SMALLER_AREA = new BigDecimal(1 - 1);

    private static final String DEFAULT_MATRICULA = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULA = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_CATASTRAL = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_CATASTRAL = "BBBBBBBBBB";

    private static final String DEFAULT_MUNICIPIO = "AAAAAAAAAA";
    private static final String UPDATED_MUNICIPIO = "BBBBBBBBBB";

    private static final String DEFAULT_VEREDA = "AAAAAAAAAA";
    private static final String UPDATED_VEREDA = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERRVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERRVACIONES = "BBBBBBBBBB";

    @Autowired
    private FincaRepository fincaRepository;

    @Autowired
    private FincaService fincaService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.FincaSearchRepositoryMockConfiguration
     */
    @Autowired
    private FincaSearchRepository mockFincaSearchRepository;

    @Autowired
    private FincaQueryService fincaQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFincaMockMvc;

    private Finca finca;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Finca createEntity(EntityManager em) {
        Finca finca = new Finca()
            .nombre(DEFAULT_NOMBRE)
            .area(DEFAULT_AREA)
            .matricula(DEFAULT_MATRICULA)
            .codigoCatastral(DEFAULT_CODIGO_CATASTRAL)
            .municipio(DEFAULT_MUNICIPIO)
            .vereda(DEFAULT_VEREDA)
            .obserrvaciones(DEFAULT_OBSERRVACIONES);
        return finca;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Finca createUpdatedEntity(EntityManager em) {
        Finca finca = new Finca()
            .nombre(UPDATED_NOMBRE)
            .area(UPDATED_AREA)
            .matricula(UPDATED_MATRICULA)
            .codigoCatastral(UPDATED_CODIGO_CATASTRAL)
            .municipio(UPDATED_MUNICIPIO)
            .vereda(UPDATED_VEREDA)
            .obserrvaciones(UPDATED_OBSERRVACIONES);
        return finca;
    }

    @BeforeEach
    public void initTest() {
        finca = createEntity(em);
    }

    @Test
    @Transactional
    public void createFinca() throws Exception {
        int databaseSizeBeforeCreate = fincaRepository.findAll().size();
        // Create the Finca
        restFincaMockMvc.perform(post("/api/fincas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(finca)))
            .andExpect(status().isCreated());

        // Validate the Finca in the database
        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeCreate + 1);
        Finca testFinca = fincaList.get(fincaList.size() - 1);
        assertThat(testFinca.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testFinca.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testFinca.getMatricula()).isEqualTo(DEFAULT_MATRICULA);
        assertThat(testFinca.getCodigoCatastral()).isEqualTo(DEFAULT_CODIGO_CATASTRAL);
        assertThat(testFinca.getMunicipio()).isEqualTo(DEFAULT_MUNICIPIO);
        assertThat(testFinca.getVereda()).isEqualTo(DEFAULT_VEREDA);
        assertThat(testFinca.getObserrvaciones()).isEqualTo(DEFAULT_OBSERRVACIONES);

        // Validate the Finca in Elasticsearch
        verify(mockFincaSearchRepository, times(1)).save(testFinca);
    }

    @Test
    @Transactional
    public void createFincaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fincaRepository.findAll().size();

        // Create the Finca with an existing ID
        finca.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFincaMockMvc.perform(post("/api/fincas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(finca)))
            .andExpect(status().isBadRequest());

        // Validate the Finca in the database
        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Finca in Elasticsearch
        verify(mockFincaSearchRepository, times(0)).save(finca);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = fincaRepository.findAll().size();
        // set the field null
        finca.setNombre(null);

        // Create the Finca, which fails.


        restFincaMockMvc.perform(post("/api/fincas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(finca)))
            .andExpect(status().isBadRequest());

        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFincas() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList
        restFincaMockMvc.perform(get("/api/fincas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.intValue())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].codigoCatastral").value(hasItem(DEFAULT_CODIGO_CATASTRAL)))
            .andExpect(jsonPath("$.[*].municipio").value(hasItem(DEFAULT_MUNICIPIO)))
            .andExpect(jsonPath("$.[*].vereda").value(hasItem(DEFAULT_VEREDA)))
            .andExpect(jsonPath("$.[*].obserrvaciones").value(hasItem(DEFAULT_OBSERRVACIONES)));
    }
    
    @Test
    @Transactional
    public void getFinca() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get the finca
        restFincaMockMvc.perform(get("/api/fincas/{id}", finca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(finca.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA.intValue()))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA))
            .andExpect(jsonPath("$.codigoCatastral").value(DEFAULT_CODIGO_CATASTRAL))
            .andExpect(jsonPath("$.municipio").value(DEFAULT_MUNICIPIO))
            .andExpect(jsonPath("$.vereda").value(DEFAULT_VEREDA))
            .andExpect(jsonPath("$.obserrvaciones").value(DEFAULT_OBSERRVACIONES));
    }


    @Test
    @Transactional
    public void getFincasByIdFiltering() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        Long id = finca.getId();

        defaultFincaShouldBeFound("id.equals=" + id);
        defaultFincaShouldNotBeFound("id.notEquals=" + id);

        defaultFincaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFincaShouldNotBeFound("id.greaterThan=" + id);

        defaultFincaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFincaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFincasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where nombre equals to DEFAULT_NOMBRE
        defaultFincaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the fincaList where nombre equals to UPDATED_NOMBRE
        defaultFincaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllFincasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where nombre not equals to DEFAULT_NOMBRE
        defaultFincaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the fincaList where nombre not equals to UPDATED_NOMBRE
        defaultFincaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllFincasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultFincaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the fincaList where nombre equals to UPDATED_NOMBRE
        defaultFincaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllFincasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where nombre is not null
        defaultFincaShouldBeFound("nombre.specified=true");

        // Get all the fincaList where nombre is null
        defaultFincaShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllFincasByNombreContainsSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where nombre contains DEFAULT_NOMBRE
        defaultFincaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the fincaList where nombre contains UPDATED_NOMBRE
        defaultFincaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllFincasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where nombre does not contain DEFAULT_NOMBRE
        defaultFincaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the fincaList where nombre does not contain UPDATED_NOMBRE
        defaultFincaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllFincasByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where area equals to DEFAULT_AREA
        defaultFincaShouldBeFound("area.equals=" + DEFAULT_AREA);

        // Get all the fincaList where area equals to UPDATED_AREA
        defaultFincaShouldNotBeFound("area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    public void getAllFincasByAreaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where area not equals to DEFAULT_AREA
        defaultFincaShouldNotBeFound("area.notEquals=" + DEFAULT_AREA);

        // Get all the fincaList where area not equals to UPDATED_AREA
        defaultFincaShouldBeFound("area.notEquals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    public void getAllFincasByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where area in DEFAULT_AREA or UPDATED_AREA
        defaultFincaShouldBeFound("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA);

        // Get all the fincaList where area equals to UPDATED_AREA
        defaultFincaShouldNotBeFound("area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    public void getAllFincasByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where area is not null
        defaultFincaShouldBeFound("area.specified=true");

        // Get all the fincaList where area is null
        defaultFincaShouldNotBeFound("area.specified=false");
    }

    @Test
    @Transactional
    public void getAllFincasByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where area is greater than or equal to DEFAULT_AREA
        defaultFincaShouldBeFound("area.greaterThanOrEqual=" + DEFAULT_AREA);

        // Get all the fincaList where area is greater than or equal to UPDATED_AREA
        defaultFincaShouldNotBeFound("area.greaterThanOrEqual=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    public void getAllFincasByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where area is less than or equal to DEFAULT_AREA
        defaultFincaShouldBeFound("area.lessThanOrEqual=" + DEFAULT_AREA);

        // Get all the fincaList where area is less than or equal to SMALLER_AREA
        defaultFincaShouldNotBeFound("area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    public void getAllFincasByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where area is less than DEFAULT_AREA
        defaultFincaShouldNotBeFound("area.lessThan=" + DEFAULT_AREA);

        // Get all the fincaList where area is less than UPDATED_AREA
        defaultFincaShouldBeFound("area.lessThan=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    public void getAllFincasByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where area is greater than DEFAULT_AREA
        defaultFincaShouldNotBeFound("area.greaterThan=" + DEFAULT_AREA);

        // Get all the fincaList where area is greater than SMALLER_AREA
        defaultFincaShouldBeFound("area.greaterThan=" + SMALLER_AREA);
    }


    @Test
    @Transactional
    public void getAllFincasByMatriculaIsEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where matricula equals to DEFAULT_MATRICULA
        defaultFincaShouldBeFound("matricula.equals=" + DEFAULT_MATRICULA);

        // Get all the fincaList where matricula equals to UPDATED_MATRICULA
        defaultFincaShouldNotBeFound("matricula.equals=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    public void getAllFincasByMatriculaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where matricula not equals to DEFAULT_MATRICULA
        defaultFincaShouldNotBeFound("matricula.notEquals=" + DEFAULT_MATRICULA);

        // Get all the fincaList where matricula not equals to UPDATED_MATRICULA
        defaultFincaShouldBeFound("matricula.notEquals=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    public void getAllFincasByMatriculaIsInShouldWork() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where matricula in DEFAULT_MATRICULA or UPDATED_MATRICULA
        defaultFincaShouldBeFound("matricula.in=" + DEFAULT_MATRICULA + "," + UPDATED_MATRICULA);

        // Get all the fincaList where matricula equals to UPDATED_MATRICULA
        defaultFincaShouldNotBeFound("matricula.in=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    public void getAllFincasByMatriculaIsNullOrNotNull() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where matricula is not null
        defaultFincaShouldBeFound("matricula.specified=true");

        // Get all the fincaList where matricula is null
        defaultFincaShouldNotBeFound("matricula.specified=false");
    }
                @Test
    @Transactional
    public void getAllFincasByMatriculaContainsSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where matricula contains DEFAULT_MATRICULA
        defaultFincaShouldBeFound("matricula.contains=" + DEFAULT_MATRICULA);

        // Get all the fincaList where matricula contains UPDATED_MATRICULA
        defaultFincaShouldNotBeFound("matricula.contains=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    public void getAllFincasByMatriculaNotContainsSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where matricula does not contain DEFAULT_MATRICULA
        defaultFincaShouldNotBeFound("matricula.doesNotContain=" + DEFAULT_MATRICULA);

        // Get all the fincaList where matricula does not contain UPDATED_MATRICULA
        defaultFincaShouldBeFound("matricula.doesNotContain=" + UPDATED_MATRICULA);
    }


    @Test
    @Transactional
    public void getAllFincasByCodigoCatastralIsEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where codigoCatastral equals to DEFAULT_CODIGO_CATASTRAL
        defaultFincaShouldBeFound("codigoCatastral.equals=" + DEFAULT_CODIGO_CATASTRAL);

        // Get all the fincaList where codigoCatastral equals to UPDATED_CODIGO_CATASTRAL
        defaultFincaShouldNotBeFound("codigoCatastral.equals=" + UPDATED_CODIGO_CATASTRAL);
    }

    @Test
    @Transactional
    public void getAllFincasByCodigoCatastralIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where codigoCatastral not equals to DEFAULT_CODIGO_CATASTRAL
        defaultFincaShouldNotBeFound("codigoCatastral.notEquals=" + DEFAULT_CODIGO_CATASTRAL);

        // Get all the fincaList where codigoCatastral not equals to UPDATED_CODIGO_CATASTRAL
        defaultFincaShouldBeFound("codigoCatastral.notEquals=" + UPDATED_CODIGO_CATASTRAL);
    }

    @Test
    @Transactional
    public void getAllFincasByCodigoCatastralIsInShouldWork() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where codigoCatastral in DEFAULT_CODIGO_CATASTRAL or UPDATED_CODIGO_CATASTRAL
        defaultFincaShouldBeFound("codigoCatastral.in=" + DEFAULT_CODIGO_CATASTRAL + "," + UPDATED_CODIGO_CATASTRAL);

        // Get all the fincaList where codigoCatastral equals to UPDATED_CODIGO_CATASTRAL
        defaultFincaShouldNotBeFound("codigoCatastral.in=" + UPDATED_CODIGO_CATASTRAL);
    }

    @Test
    @Transactional
    public void getAllFincasByCodigoCatastralIsNullOrNotNull() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where codigoCatastral is not null
        defaultFincaShouldBeFound("codigoCatastral.specified=true");

        // Get all the fincaList where codigoCatastral is null
        defaultFincaShouldNotBeFound("codigoCatastral.specified=false");
    }
                @Test
    @Transactional
    public void getAllFincasByCodigoCatastralContainsSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where codigoCatastral contains DEFAULT_CODIGO_CATASTRAL
        defaultFincaShouldBeFound("codigoCatastral.contains=" + DEFAULT_CODIGO_CATASTRAL);

        // Get all the fincaList where codigoCatastral contains UPDATED_CODIGO_CATASTRAL
        defaultFincaShouldNotBeFound("codigoCatastral.contains=" + UPDATED_CODIGO_CATASTRAL);
    }

    @Test
    @Transactional
    public void getAllFincasByCodigoCatastralNotContainsSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where codigoCatastral does not contain DEFAULT_CODIGO_CATASTRAL
        defaultFincaShouldNotBeFound("codigoCatastral.doesNotContain=" + DEFAULT_CODIGO_CATASTRAL);

        // Get all the fincaList where codigoCatastral does not contain UPDATED_CODIGO_CATASTRAL
        defaultFincaShouldBeFound("codigoCatastral.doesNotContain=" + UPDATED_CODIGO_CATASTRAL);
    }


    @Test
    @Transactional
    public void getAllFincasByMunicipioIsEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where municipio equals to DEFAULT_MUNICIPIO
        defaultFincaShouldBeFound("municipio.equals=" + DEFAULT_MUNICIPIO);

        // Get all the fincaList where municipio equals to UPDATED_MUNICIPIO
        defaultFincaShouldNotBeFound("municipio.equals=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    public void getAllFincasByMunicipioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where municipio not equals to DEFAULT_MUNICIPIO
        defaultFincaShouldNotBeFound("municipio.notEquals=" + DEFAULT_MUNICIPIO);

        // Get all the fincaList where municipio not equals to UPDATED_MUNICIPIO
        defaultFincaShouldBeFound("municipio.notEquals=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    public void getAllFincasByMunicipioIsInShouldWork() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where municipio in DEFAULT_MUNICIPIO or UPDATED_MUNICIPIO
        defaultFincaShouldBeFound("municipio.in=" + DEFAULT_MUNICIPIO + "," + UPDATED_MUNICIPIO);

        // Get all the fincaList where municipio equals to UPDATED_MUNICIPIO
        defaultFincaShouldNotBeFound("municipio.in=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    public void getAllFincasByMunicipioIsNullOrNotNull() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where municipio is not null
        defaultFincaShouldBeFound("municipio.specified=true");

        // Get all the fincaList where municipio is null
        defaultFincaShouldNotBeFound("municipio.specified=false");
    }
                @Test
    @Transactional
    public void getAllFincasByMunicipioContainsSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where municipio contains DEFAULT_MUNICIPIO
        defaultFincaShouldBeFound("municipio.contains=" + DEFAULT_MUNICIPIO);

        // Get all the fincaList where municipio contains UPDATED_MUNICIPIO
        defaultFincaShouldNotBeFound("municipio.contains=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    public void getAllFincasByMunicipioNotContainsSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where municipio does not contain DEFAULT_MUNICIPIO
        defaultFincaShouldNotBeFound("municipio.doesNotContain=" + DEFAULT_MUNICIPIO);

        // Get all the fincaList where municipio does not contain UPDATED_MUNICIPIO
        defaultFincaShouldBeFound("municipio.doesNotContain=" + UPDATED_MUNICIPIO);
    }


    @Test
    @Transactional
    public void getAllFincasByVeredaIsEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where vereda equals to DEFAULT_VEREDA
        defaultFincaShouldBeFound("vereda.equals=" + DEFAULT_VEREDA);

        // Get all the fincaList where vereda equals to UPDATED_VEREDA
        defaultFincaShouldNotBeFound("vereda.equals=" + UPDATED_VEREDA);
    }

    @Test
    @Transactional
    public void getAllFincasByVeredaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where vereda not equals to DEFAULT_VEREDA
        defaultFincaShouldNotBeFound("vereda.notEquals=" + DEFAULT_VEREDA);

        // Get all the fincaList where vereda not equals to UPDATED_VEREDA
        defaultFincaShouldBeFound("vereda.notEquals=" + UPDATED_VEREDA);
    }

    @Test
    @Transactional
    public void getAllFincasByVeredaIsInShouldWork() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where vereda in DEFAULT_VEREDA or UPDATED_VEREDA
        defaultFincaShouldBeFound("vereda.in=" + DEFAULT_VEREDA + "," + UPDATED_VEREDA);

        // Get all the fincaList where vereda equals to UPDATED_VEREDA
        defaultFincaShouldNotBeFound("vereda.in=" + UPDATED_VEREDA);
    }

    @Test
    @Transactional
    public void getAllFincasByVeredaIsNullOrNotNull() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where vereda is not null
        defaultFincaShouldBeFound("vereda.specified=true");

        // Get all the fincaList where vereda is null
        defaultFincaShouldNotBeFound("vereda.specified=false");
    }
                @Test
    @Transactional
    public void getAllFincasByVeredaContainsSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where vereda contains DEFAULT_VEREDA
        defaultFincaShouldBeFound("vereda.contains=" + DEFAULT_VEREDA);

        // Get all the fincaList where vereda contains UPDATED_VEREDA
        defaultFincaShouldNotBeFound("vereda.contains=" + UPDATED_VEREDA);
    }

    @Test
    @Transactional
    public void getAllFincasByVeredaNotContainsSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where vereda does not contain DEFAULT_VEREDA
        defaultFincaShouldNotBeFound("vereda.doesNotContain=" + DEFAULT_VEREDA);

        // Get all the fincaList where vereda does not contain UPDATED_VEREDA
        defaultFincaShouldBeFound("vereda.doesNotContain=" + UPDATED_VEREDA);
    }


    @Test
    @Transactional
    public void getAllFincasByObserrvacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where obserrvaciones equals to DEFAULT_OBSERRVACIONES
        defaultFincaShouldBeFound("obserrvaciones.equals=" + DEFAULT_OBSERRVACIONES);

        // Get all the fincaList where obserrvaciones equals to UPDATED_OBSERRVACIONES
        defaultFincaShouldNotBeFound("obserrvaciones.equals=" + UPDATED_OBSERRVACIONES);
    }

    @Test
    @Transactional
    public void getAllFincasByObserrvacionesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where obserrvaciones not equals to DEFAULT_OBSERRVACIONES
        defaultFincaShouldNotBeFound("obserrvaciones.notEquals=" + DEFAULT_OBSERRVACIONES);

        // Get all the fincaList where obserrvaciones not equals to UPDATED_OBSERRVACIONES
        defaultFincaShouldBeFound("obserrvaciones.notEquals=" + UPDATED_OBSERRVACIONES);
    }

    @Test
    @Transactional
    public void getAllFincasByObserrvacionesIsInShouldWork() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where obserrvaciones in DEFAULT_OBSERRVACIONES or UPDATED_OBSERRVACIONES
        defaultFincaShouldBeFound("obserrvaciones.in=" + DEFAULT_OBSERRVACIONES + "," + UPDATED_OBSERRVACIONES);

        // Get all the fincaList where obserrvaciones equals to UPDATED_OBSERRVACIONES
        defaultFincaShouldNotBeFound("obserrvaciones.in=" + UPDATED_OBSERRVACIONES);
    }

    @Test
    @Transactional
    public void getAllFincasByObserrvacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where obserrvaciones is not null
        defaultFincaShouldBeFound("obserrvaciones.specified=true");

        // Get all the fincaList where obserrvaciones is null
        defaultFincaShouldNotBeFound("obserrvaciones.specified=false");
    }
                @Test
    @Transactional
    public void getAllFincasByObserrvacionesContainsSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where obserrvaciones contains DEFAULT_OBSERRVACIONES
        defaultFincaShouldBeFound("obserrvaciones.contains=" + DEFAULT_OBSERRVACIONES);

        // Get all the fincaList where obserrvaciones contains UPDATED_OBSERRVACIONES
        defaultFincaShouldNotBeFound("obserrvaciones.contains=" + UPDATED_OBSERRVACIONES);
    }

    @Test
    @Transactional
    public void getAllFincasByObserrvacionesNotContainsSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList where obserrvaciones does not contain DEFAULT_OBSERRVACIONES
        defaultFincaShouldNotBeFound("obserrvaciones.doesNotContain=" + DEFAULT_OBSERRVACIONES);

        // Get all the fincaList where obserrvaciones does not contain UPDATED_OBSERRVACIONES
        defaultFincaShouldBeFound("obserrvaciones.doesNotContain=" + UPDATED_OBSERRVACIONES);
    }


    @Test
    @Transactional
    public void getAllFincasByPotrerosIsEqualToSomething() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);
        Potrero potreros = PotreroResourceIT.createEntity(em);
        em.persist(potreros);
        em.flush();
        finca.addPotreros(potreros);
        fincaRepository.saveAndFlush(finca);
        Long potrerosId = potreros.getId();

        // Get all the fincaList where potreros equals to potrerosId
        defaultFincaShouldBeFound("potrerosId.equals=" + potrerosId);

        // Get all the fincaList where potreros equals to potrerosId + 1
        defaultFincaShouldNotBeFound("potrerosId.equals=" + (potrerosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFincaShouldBeFound(String filter) throws Exception {
        restFincaMockMvc.perform(get("/api/fincas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.intValue())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].codigoCatastral").value(hasItem(DEFAULT_CODIGO_CATASTRAL)))
            .andExpect(jsonPath("$.[*].municipio").value(hasItem(DEFAULT_MUNICIPIO)))
            .andExpect(jsonPath("$.[*].vereda").value(hasItem(DEFAULT_VEREDA)))
            .andExpect(jsonPath("$.[*].obserrvaciones").value(hasItem(DEFAULT_OBSERRVACIONES)));

        // Check, that the count call also returns 1
        restFincaMockMvc.perform(get("/api/fincas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFincaShouldNotBeFound(String filter) throws Exception {
        restFincaMockMvc.perform(get("/api/fincas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFincaMockMvc.perform(get("/api/fincas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingFinca() throws Exception {
        // Get the finca
        restFincaMockMvc.perform(get("/api/fincas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFinca() throws Exception {
        // Initialize the database
        fincaService.save(finca);

        int databaseSizeBeforeUpdate = fincaRepository.findAll().size();

        // Update the finca
        Finca updatedFinca = fincaRepository.findById(finca.getId()).get();
        // Disconnect from session so that the updates on updatedFinca are not directly saved in db
        em.detach(updatedFinca);
        updatedFinca
            .nombre(UPDATED_NOMBRE)
            .area(UPDATED_AREA)
            .matricula(UPDATED_MATRICULA)
            .codigoCatastral(UPDATED_CODIGO_CATASTRAL)
            .municipio(UPDATED_MUNICIPIO)
            .vereda(UPDATED_VEREDA)
            .obserrvaciones(UPDATED_OBSERRVACIONES);

        restFincaMockMvc.perform(put("/api/fincas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFinca)))
            .andExpect(status().isOk());

        // Validate the Finca in the database
        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeUpdate);
        Finca testFinca = fincaList.get(fincaList.size() - 1);
        assertThat(testFinca.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFinca.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testFinca.getMatricula()).isEqualTo(UPDATED_MATRICULA);
        assertThat(testFinca.getCodigoCatastral()).isEqualTo(UPDATED_CODIGO_CATASTRAL);
        assertThat(testFinca.getMunicipio()).isEqualTo(UPDATED_MUNICIPIO);
        assertThat(testFinca.getVereda()).isEqualTo(UPDATED_VEREDA);
        assertThat(testFinca.getObserrvaciones()).isEqualTo(UPDATED_OBSERRVACIONES);

        // Validate the Finca in Elasticsearch
        verify(mockFincaSearchRepository, times(2)).save(testFinca);
    }

    @Test
    @Transactional
    public void updateNonExistingFinca() throws Exception {
        int databaseSizeBeforeUpdate = fincaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFincaMockMvc.perform(put("/api/fincas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(finca)))
            .andExpect(status().isBadRequest());

        // Validate the Finca in the database
        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Finca in Elasticsearch
        verify(mockFincaSearchRepository, times(0)).save(finca);
    }

    @Test
    @Transactional
    public void deleteFinca() throws Exception {
        // Initialize the database
        fincaService.save(finca);

        int databaseSizeBeforeDelete = fincaRepository.findAll().size();

        // Delete the finca
        restFincaMockMvc.perform(delete("/api/fincas/{id}", finca.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Finca in Elasticsearch
        verify(mockFincaSearchRepository, times(1)).deleteById(finca.getId());
    }

    @Test
    @Transactional
    public void searchFinca() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fincaService.save(finca);
        when(mockFincaSearchRepository.search(queryStringQuery("id:" + finca.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(finca), PageRequest.of(0, 1), 1));

        // Search the finca
        restFincaMockMvc.perform(get("/api/_search/fincas?query=id:" + finca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.intValue())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].codigoCatastral").value(hasItem(DEFAULT_CODIGO_CATASTRAL)))
            .andExpect(jsonPath("$.[*].municipio").value(hasItem(DEFAULT_MUNICIPIO)))
            .andExpect(jsonPath("$.[*].vereda").value(hasItem(DEFAULT_VEREDA)))
            .andExpect(jsonPath("$.[*].obserrvaciones").value(hasItem(DEFAULT_OBSERRVACIONES)));
    }
}
