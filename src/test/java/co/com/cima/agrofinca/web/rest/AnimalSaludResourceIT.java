package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.AnimalSalud;
import co.com.cima.agrofinca.repository.AnimalSaludRepository;
import co.com.cima.agrofinca.repository.search.AnimalSaludSearchRepository;
import co.com.cima.agrofinca.service.AnimalSaludService;

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

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_LABORATORIO = "AAAAAAAAAA";
    private static final String UPDATED_LABORATORIO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DOSIS = new BigDecimal(1);
    private static final BigDecimal UPDATED_DOSIS = new BigDecimal(2);

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
