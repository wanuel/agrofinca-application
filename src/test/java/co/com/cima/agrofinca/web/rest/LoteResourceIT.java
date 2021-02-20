package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Lote;
import co.com.cima.agrofinca.repository.LoteRepository;
import co.com.cima.agrofinca.repository.search.LoteSearchRepository;
import co.com.cima.agrofinca.service.LoteService;

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

import co.com.cima.agrofinca.domain.enumeration.ESTADOLOTE;
/**
 * Integration tests for the {@link LoteResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class LoteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final ESTADOLOTE DEFAULT_ESTADO = ESTADOLOTE.ACTIVO;
    private static final ESTADOLOTE UPDATED_ESTADO = ESTADOLOTE.INACTIVO;

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private LoteService loteService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.LoteSearchRepositoryMockConfiguration
     */
    @Autowired
    private LoteSearchRepository mockLoteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoteMockMvc;

    private Lote lote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lote createEntity(EntityManager em) {
        Lote lote = new Lote()
            .nombre(DEFAULT_NOMBRE)
            .fecha(DEFAULT_FECHA)
            .estado(DEFAULT_ESTADO);
        return lote;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lote createUpdatedEntity(EntityManager em) {
        Lote lote = new Lote()
            .nombre(UPDATED_NOMBRE)
            .fecha(UPDATED_FECHA)
            .estado(UPDATED_ESTADO);
        return lote;
    }

    @BeforeEach
    public void initTest() {
        lote = createEntity(em);
    }

    @Test
    @Transactional
    public void createLote() throws Exception {
        int databaseSizeBeforeCreate = loteRepository.findAll().size();
        // Create the Lote
        restLoteMockMvc.perform(post("/api/lotes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lote)))
            .andExpect(status().isCreated());

        // Validate the Lote in the database
        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeCreate + 1);
        Lote testLote = loteList.get(loteList.size() - 1);
        assertThat(testLote.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testLote.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testLote.getEstado()).isEqualTo(DEFAULT_ESTADO);

        // Validate the Lote in Elasticsearch
        verify(mockLoteSearchRepository, times(1)).save(testLote);
    }

    @Test
    @Transactional
    public void createLoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = loteRepository.findAll().size();

        // Create the Lote with an existing ID
        lote.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoteMockMvc.perform(post("/api/lotes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lote)))
            .andExpect(status().isBadRequest());

        // Validate the Lote in the database
        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeCreate);

        // Validate the Lote in Elasticsearch
        verify(mockLoteSearchRepository, times(0)).save(lote);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = loteRepository.findAll().size();
        // set the field null
        lote.setNombre(null);

        // Create the Lote, which fails.


        restLoteMockMvc.perform(post("/api/lotes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lote)))
            .andExpect(status().isBadRequest());

        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = loteRepository.findAll().size();
        // set the field null
        lote.setFecha(null);

        // Create the Lote, which fails.


        restLoteMockMvc.perform(post("/api/lotes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lote)))
            .andExpect(status().isBadRequest());

        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = loteRepository.findAll().size();
        // set the field null
        lote.setEstado(null);

        // Create the Lote, which fails.


        restLoteMockMvc.perform(post("/api/lotes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lote)))
            .andExpect(status().isBadRequest());

        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLotes() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList
        restLoteMockMvc.perform(get("/api/lotes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lote.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }
    
    @Test
    @Transactional
    public void getLote() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get the lote
        restLoteMockMvc.perform(get("/api/lotes/{id}", lote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lote.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingLote() throws Exception {
        // Get the lote
        restLoteMockMvc.perform(get("/api/lotes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLote() throws Exception {
        // Initialize the database
        loteService.save(lote);

        int databaseSizeBeforeUpdate = loteRepository.findAll().size();

        // Update the lote
        Lote updatedLote = loteRepository.findById(lote.getId()).get();
        // Disconnect from session so that the updates on updatedLote are not directly saved in db
        em.detach(updatedLote);
        updatedLote
            .nombre(UPDATED_NOMBRE)
            .fecha(UPDATED_FECHA)
            .estado(UPDATED_ESTADO);

        restLoteMockMvc.perform(put("/api/lotes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLote)))
            .andExpect(status().isOk());

        // Validate the Lote in the database
        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeUpdate);
        Lote testLote = loteList.get(loteList.size() - 1);
        assertThat(testLote.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testLote.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testLote.getEstado()).isEqualTo(UPDATED_ESTADO);

        // Validate the Lote in Elasticsearch
        verify(mockLoteSearchRepository, times(2)).save(testLote);
    }

    @Test
    @Transactional
    public void updateNonExistingLote() throws Exception {
        int databaseSizeBeforeUpdate = loteRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoteMockMvc.perform(put("/api/lotes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lote)))
            .andExpect(status().isBadRequest());

        // Validate the Lote in the database
        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Lote in Elasticsearch
        verify(mockLoteSearchRepository, times(0)).save(lote);
    }

    @Test
    @Transactional
    public void deleteLote() throws Exception {
        // Initialize the database
        loteService.save(lote);

        int databaseSizeBeforeDelete = loteRepository.findAll().size();

        // Delete the lote
        restLoteMockMvc.perform(delete("/api/lotes/{id}", lote.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Lote in Elasticsearch
        verify(mockLoteSearchRepository, times(1)).deleteById(lote.getId());
    }

    @Test
    @Transactional
    public void searchLote() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        loteService.save(lote);
        when(mockLoteSearchRepository.search(queryStringQuery("id:" + lote.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(lote), PageRequest.of(0, 1), 1));

        // Search the lote
        restLoteMockMvc.perform(get("/api/_search/lotes?query=id:" + lote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lote.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }
}
