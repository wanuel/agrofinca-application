package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Potrero;
import co.com.cima.agrofinca.repository.PotreroRepository;
import co.com.cima.agrofinca.repository.search.PotreroSearchRepository;
import co.com.cima.agrofinca.service.PotreroService;

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
 * Integration tests for the {@link PotreroResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PotreroResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_PASTO = "AAAAAAAAAA";
    private static final String UPDATED_PASTO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_AREA = new BigDecimal(2);

    @Autowired
    private PotreroRepository potreroRepository;

    @Autowired
    private PotreroService potreroService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.PotreroSearchRepositoryMockConfiguration
     */
    @Autowired
    private PotreroSearchRepository mockPotreroSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPotreroMockMvc;

    private Potrero potrero;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Potrero createEntity(EntityManager em) {
        Potrero potrero = new Potrero()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .pasto(DEFAULT_PASTO)
            .area(DEFAULT_AREA);
        return potrero;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Potrero createUpdatedEntity(EntityManager em) {
        Potrero potrero = new Potrero()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .pasto(UPDATED_PASTO)
            .area(UPDATED_AREA);
        return potrero;
    }

    @BeforeEach
    public void initTest() {
        potrero = createEntity(em);
    }

    @Test
    @Transactional
    public void createPotrero() throws Exception {
        int databaseSizeBeforeCreate = potreroRepository.findAll().size();
        // Create the Potrero
        restPotreroMockMvc.perform(post("/api/potreros").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(potrero)))
            .andExpect(status().isCreated());

        // Validate the Potrero in the database
        List<Potrero> potreroList = potreroRepository.findAll();
        assertThat(potreroList).hasSize(databaseSizeBeforeCreate + 1);
        Potrero testPotrero = potreroList.get(potreroList.size() - 1);
        assertThat(testPotrero.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPotrero.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPotrero.getPasto()).isEqualTo(DEFAULT_PASTO);
        assertThat(testPotrero.getArea()).isEqualTo(DEFAULT_AREA);

        // Validate the Potrero in Elasticsearch
        verify(mockPotreroSearchRepository, times(1)).save(testPotrero);
    }

    @Test
    @Transactional
    public void createPotreroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = potreroRepository.findAll().size();

        // Create the Potrero with an existing ID
        potrero.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPotreroMockMvc.perform(post("/api/potreros").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(potrero)))
            .andExpect(status().isBadRequest());

        // Validate the Potrero in the database
        List<Potrero> potreroList = potreroRepository.findAll();
        assertThat(potreroList).hasSize(databaseSizeBeforeCreate);

        // Validate the Potrero in Elasticsearch
        verify(mockPotreroSearchRepository, times(0)).save(potrero);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = potreroRepository.findAll().size();
        // set the field null
        potrero.setNombre(null);

        // Create the Potrero, which fails.


        restPotreroMockMvc.perform(post("/api/potreros").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(potrero)))
            .andExpect(status().isBadRequest());

        List<Potrero> potreroList = potreroRepository.findAll();
        assertThat(potreroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPotreros() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList
        restPotreroMockMvc.perform(get("/api/potreros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(potrero.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].pasto").value(hasItem(DEFAULT_PASTO)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.intValue())));
    }
    
    @Test
    @Transactional
    public void getPotrero() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get the potrero
        restPotreroMockMvc.perform(get("/api/potreros/{id}", potrero.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(potrero.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.pasto").value(DEFAULT_PASTO))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPotrero() throws Exception {
        // Get the potrero
        restPotreroMockMvc.perform(get("/api/potreros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePotrero() throws Exception {
        // Initialize the database
        potreroService.save(potrero);

        int databaseSizeBeforeUpdate = potreroRepository.findAll().size();

        // Update the potrero
        Potrero updatedPotrero = potreroRepository.findById(potrero.getId()).get();
        // Disconnect from session so that the updates on updatedPotrero are not directly saved in db
        em.detach(updatedPotrero);
        updatedPotrero
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .pasto(UPDATED_PASTO)
            .area(UPDATED_AREA);

        restPotreroMockMvc.perform(put("/api/potreros").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPotrero)))
            .andExpect(status().isOk());

        // Validate the Potrero in the database
        List<Potrero> potreroList = potreroRepository.findAll();
        assertThat(potreroList).hasSize(databaseSizeBeforeUpdate);
        Potrero testPotrero = potreroList.get(potreroList.size() - 1);
        assertThat(testPotrero.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPotrero.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPotrero.getPasto()).isEqualTo(UPDATED_PASTO);
        assertThat(testPotrero.getArea()).isEqualTo(UPDATED_AREA);

        // Validate the Potrero in Elasticsearch
        verify(mockPotreroSearchRepository, times(2)).save(testPotrero);
    }

    @Test
    @Transactional
    public void updateNonExistingPotrero() throws Exception {
        int databaseSizeBeforeUpdate = potreroRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPotreroMockMvc.perform(put("/api/potreros").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(potrero)))
            .andExpect(status().isBadRequest());

        // Validate the Potrero in the database
        List<Potrero> potreroList = potreroRepository.findAll();
        assertThat(potreroList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Potrero in Elasticsearch
        verify(mockPotreroSearchRepository, times(0)).save(potrero);
    }

    @Test
    @Transactional
    public void deletePotrero() throws Exception {
        // Initialize the database
        potreroService.save(potrero);

        int databaseSizeBeforeDelete = potreroRepository.findAll().size();

        // Delete the potrero
        restPotreroMockMvc.perform(delete("/api/potreros/{id}", potrero.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Potrero> potreroList = potreroRepository.findAll();
        assertThat(potreroList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Potrero in Elasticsearch
        verify(mockPotreroSearchRepository, times(1)).deleteById(potrero.getId());
    }

    @Test
    @Transactional
    public void searchPotrero() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        potreroService.save(potrero);
        when(mockPotreroSearchRepository.search(queryStringQuery("id:" + potrero.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(potrero), PageRequest.of(0, 1), 1));

        // Search the potrero
        restPotreroMockMvc.perform(get("/api/_search/potreros?query=id:" + potrero.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(potrero.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].pasto").value(hasItem(DEFAULT_PASTO)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.intValue())));
    }
}
