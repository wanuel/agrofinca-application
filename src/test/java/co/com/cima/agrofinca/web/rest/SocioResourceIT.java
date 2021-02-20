package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Socio;
import co.com.cima.agrofinca.repository.SocioRepository;
import co.com.cima.agrofinca.repository.search.SocioSearchRepository;
import co.com.cima.agrofinca.service.SocioService;

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
 * Integration tests for the {@link SocioResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class SocioResourceIT {

    private static final LocalDate DEFAULT_FECHA_INGRESO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INGRESO = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_PARTICIPACION = new BigDecimal(1);
    private static final BigDecimal UPDATED_PARTICIPACION = new BigDecimal(2);

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
        Socio socio = new Socio()
            .fechaIngreso(DEFAULT_FECHA_INGRESO)
            .participacion(DEFAULT_PARTICIPACION);
        return socio;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Socio createUpdatedEntity(EntityManager em) {
        Socio socio = new Socio()
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .participacion(UPDATED_PARTICIPACION);
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
        restSocioMockMvc.perform(post("/api/socios").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(socio)))
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
        restSocioMockMvc.perform(post("/api/socios").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(socio)))
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


        restSocioMockMvc.perform(post("/api/socios").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(socio)))
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


        restSocioMockMvc.perform(post("/api/socios").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(socio)))
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
        restSocioMockMvc.perform(get("/api/socios?sort=id,desc"))
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
        restSocioMockMvc.perform(get("/api/socios/{id}", socio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(socio.getId().intValue()))
            .andExpect(jsonPath("$.fechaIngreso").value(DEFAULT_FECHA_INGRESO.toString()))
            .andExpect(jsonPath("$.participacion").value(DEFAULT_PARTICIPACION.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingSocio() throws Exception {
        // Get the socio
        restSocioMockMvc.perform(get("/api/socios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
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
        updatedSocio
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .participacion(UPDATED_PARTICIPACION);

        restSocioMockMvc.perform(put("/api/socios").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSocio)))
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
        restSocioMockMvc.perform(put("/api/socios").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(socio)))
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
        restSocioMockMvc.perform(delete("/api/socios/{id}", socio.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
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
        restSocioMockMvc.perform(get("/api/_search/socios?query=id:" + socio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socio.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].participacion").value(hasItem(DEFAULT_PARTICIPACION.intValue())));
    }
}
