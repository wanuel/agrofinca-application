package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.PotreroPastoreo;
import co.com.cima.agrofinca.repository.PotreroPastoreoRepository;
import co.com.cima.agrofinca.repository.search.PotreroPastoreoSearchRepository;
import co.com.cima.agrofinca.service.PotreroPastoreoService;

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

    private static final LocalDate DEFAULT_FECHA_SALIDA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_SALIDA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_LIMPIA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_LIMPIA = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_DIAS_DESCANSO = 1;
    private static final Integer UPDATED_DIAS_DESCANSO = 2;

    private static final Integer DEFAULT_DIAS_CARGA = 1;
    private static final Integer UPDATED_DIAS_CARGA = 2;

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
