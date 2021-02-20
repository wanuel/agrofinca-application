package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Parametros;
import co.com.cima.agrofinca.repository.ParametrosRepository;
import co.com.cima.agrofinca.repository.search.ParametrosSearchRepository;
import co.com.cima.agrofinca.service.ParametrosService;

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
 * Integration tests for the {@link ParametrosResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ParametrosResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private ParametrosRepository parametrosRepository;

    @Autowired
    private ParametrosService parametrosService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.ParametrosSearchRepositoryMockConfiguration
     */
    @Autowired
    private ParametrosSearchRepository mockParametrosSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParametrosMockMvc;

    private Parametros parametros;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parametros createEntity(EntityManager em) {
        Parametros parametros = new Parametros()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION);
        return parametros;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parametros createUpdatedEntity(EntityManager em) {
        Parametros parametros = new Parametros()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);
        return parametros;
    }

    @BeforeEach
    public void initTest() {
        parametros = createEntity(em);
    }

    @Test
    @Transactional
    public void createParametros() throws Exception {
        int databaseSizeBeforeCreate = parametrosRepository.findAll().size();
        // Create the Parametros
        restParametrosMockMvc.perform(post("/api/parametros").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parametros)))
            .andExpect(status().isCreated());

        // Validate the Parametros in the database
        List<Parametros> parametrosList = parametrosRepository.findAll();
        assertThat(parametrosList).hasSize(databaseSizeBeforeCreate + 1);
        Parametros testParametros = parametrosList.get(parametrosList.size() - 1);
        assertThat(testParametros.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testParametros.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);

        // Validate the Parametros in Elasticsearch
        verify(mockParametrosSearchRepository, times(1)).save(testParametros);
    }

    @Test
    @Transactional
    public void createParametrosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parametrosRepository.findAll().size();

        // Create the Parametros with an existing ID
        parametros.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParametrosMockMvc.perform(post("/api/parametros").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parametros)))
            .andExpect(status().isBadRequest());

        // Validate the Parametros in the database
        List<Parametros> parametrosList = parametrosRepository.findAll();
        assertThat(parametrosList).hasSize(databaseSizeBeforeCreate);

        // Validate the Parametros in Elasticsearch
        verify(mockParametrosSearchRepository, times(0)).save(parametros);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametrosRepository.findAll().size();
        // set the field null
        parametros.setNombre(null);

        // Create the Parametros, which fails.


        restParametrosMockMvc.perform(post("/api/parametros").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parametros)))
            .andExpect(status().isBadRequest());

        List<Parametros> parametrosList = parametrosRepository.findAll();
        assertThat(parametrosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParametros() throws Exception {
        // Initialize the database
        parametrosRepository.saveAndFlush(parametros);

        // Get all the parametrosList
        restParametrosMockMvc.perform(get("/api/parametros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parametros.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
    
    @Test
    @Transactional
    public void getParametros() throws Exception {
        // Initialize the database
        parametrosRepository.saveAndFlush(parametros);

        // Get the parametros
        restParametrosMockMvc.perform(get("/api/parametros/{id}", parametros.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parametros.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }
    @Test
    @Transactional
    public void getNonExistingParametros() throws Exception {
        // Get the parametros
        restParametrosMockMvc.perform(get("/api/parametros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParametros() throws Exception {
        // Initialize the database
        parametrosService.save(parametros);

        int databaseSizeBeforeUpdate = parametrosRepository.findAll().size();

        // Update the parametros
        Parametros updatedParametros = parametrosRepository.findById(parametros.getId()).get();
        // Disconnect from session so that the updates on updatedParametros are not directly saved in db
        em.detach(updatedParametros);
        updatedParametros
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);

        restParametrosMockMvc.perform(put("/api/parametros").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedParametros)))
            .andExpect(status().isOk());

        // Validate the Parametros in the database
        List<Parametros> parametrosList = parametrosRepository.findAll();
        assertThat(parametrosList).hasSize(databaseSizeBeforeUpdate);
        Parametros testParametros = parametrosList.get(parametrosList.size() - 1);
        assertThat(testParametros.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testParametros.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);

        // Validate the Parametros in Elasticsearch
        verify(mockParametrosSearchRepository, times(2)).save(testParametros);
    }

    @Test
    @Transactional
    public void updateNonExistingParametros() throws Exception {
        int databaseSizeBeforeUpdate = parametrosRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParametrosMockMvc.perform(put("/api/parametros").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parametros)))
            .andExpect(status().isBadRequest());

        // Validate the Parametros in the database
        List<Parametros> parametrosList = parametrosRepository.findAll();
        assertThat(parametrosList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Parametros in Elasticsearch
        verify(mockParametrosSearchRepository, times(0)).save(parametros);
    }

    @Test
    @Transactional
    public void deleteParametros() throws Exception {
        // Initialize the database
        parametrosService.save(parametros);

        int databaseSizeBeforeDelete = parametrosRepository.findAll().size();

        // Delete the parametros
        restParametrosMockMvc.perform(delete("/api/parametros/{id}", parametros.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parametros> parametrosList = parametrosRepository.findAll();
        assertThat(parametrosList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Parametros in Elasticsearch
        verify(mockParametrosSearchRepository, times(1)).deleteById(parametros.getId());
    }

    @Test
    @Transactional
    public void searchParametros() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        parametrosService.save(parametros);
        when(mockParametrosSearchRepository.search(queryStringQuery("id:" + parametros.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(parametros), PageRequest.of(0, 1), 1));

        // Search the parametros
        restParametrosMockMvc.perform(get("/api/_search/parametros?query=id:" + parametros.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parametros.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
}
