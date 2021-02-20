package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Finca;
import co.com.cima.agrofinca.repository.FincaRepository;
import co.com.cima.agrofinca.repository.search.FincaSearchRepository;
import co.com.cima.agrofinca.service.FincaService;

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
