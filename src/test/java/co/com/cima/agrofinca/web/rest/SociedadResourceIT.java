package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Sociedad;
import co.com.cima.agrofinca.repository.SociedadRepository;
import co.com.cima.agrofinca.repository.search.SociedadSearchRepository;
import co.com.cima.agrofinca.service.SociedadService;

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
