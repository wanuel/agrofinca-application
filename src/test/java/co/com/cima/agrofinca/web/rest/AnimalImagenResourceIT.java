package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.AnimalImagen;
import co.com.cima.agrofinca.domain.Animal;
import co.com.cima.agrofinca.repository.AnimalImagenRepository;
import co.com.cima.agrofinca.repository.search.AnimalImagenSearchRepository;
import co.com.cima.agrofinca.service.AnimalImagenService;
import co.com.cima.agrofinca.service.dto.AnimalImagenCriteria;
import co.com.cima.agrofinca.service.AnimalImagenQueryService;

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
import org.springframework.util.Base64Utils;
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

/**
 * Integration tests for the {@link AnimalImagenResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AnimalImagenResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_NOTA = "AAAAAAAAAA";
    private static final String UPDATED_NOTA = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGEN = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEN = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGEN_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEN_CONTENT_TYPE = "image/png";

    @Autowired
    private AnimalImagenRepository animalImagenRepository;

    @Autowired
    private AnimalImagenService animalImagenService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.AnimalImagenSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnimalImagenSearchRepository mockAnimalImagenSearchRepository;

    @Autowired
    private AnimalImagenQueryService animalImagenQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnimalImagenMockMvc;

    private AnimalImagen animalImagen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalImagen createEntity(EntityManager em) {
        AnimalImagen animalImagen = new AnimalImagen()
            .fecha(DEFAULT_FECHA)
            .nota(DEFAULT_NOTA)
            .imagen(DEFAULT_IMAGEN)
            .imagenContentType(DEFAULT_IMAGEN_CONTENT_TYPE);
        return animalImagen;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalImagen createUpdatedEntity(EntityManager em) {
        AnimalImagen animalImagen = new AnimalImagen()
            .fecha(UPDATED_FECHA)
            .nota(UPDATED_NOTA)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE);
        return animalImagen;
    }

    @BeforeEach
    public void initTest() {
        animalImagen = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnimalImagen() throws Exception {
        int databaseSizeBeforeCreate = animalImagenRepository.findAll().size();
        // Create the AnimalImagen
        restAnimalImagenMockMvc.perform(post("/api/animal-imagens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalImagen)))
            .andExpect(status().isCreated());

        // Validate the AnimalImagen in the database
        List<AnimalImagen> animalImagenList = animalImagenRepository.findAll();
        assertThat(animalImagenList).hasSize(databaseSizeBeforeCreate + 1);
        AnimalImagen testAnimalImagen = animalImagenList.get(animalImagenList.size() - 1);
        assertThat(testAnimalImagen.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testAnimalImagen.getNota()).isEqualTo(DEFAULT_NOTA);
        assertThat(testAnimalImagen.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testAnimalImagen.getImagenContentType()).isEqualTo(DEFAULT_IMAGEN_CONTENT_TYPE);

        // Validate the AnimalImagen in Elasticsearch
        verify(mockAnimalImagenSearchRepository, times(1)).save(testAnimalImagen);
    }

    @Test
    @Transactional
    public void createAnimalImagenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = animalImagenRepository.findAll().size();

        // Create the AnimalImagen with an existing ID
        animalImagen.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnimalImagenMockMvc.perform(post("/api/animal-imagens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalImagen)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalImagen in the database
        List<AnimalImagen> animalImagenList = animalImagenRepository.findAll();
        assertThat(animalImagenList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnimalImagen in Elasticsearch
        verify(mockAnimalImagenSearchRepository, times(0)).save(animalImagen);
    }


    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalImagenRepository.findAll().size();
        // set the field null
        animalImagen.setFecha(null);

        // Create the AnimalImagen, which fails.


        restAnimalImagenMockMvc.perform(post("/api/animal-imagens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalImagen)))
            .andExpect(status().isBadRequest());

        List<AnimalImagen> animalImagenList = animalImagenRepository.findAll();
        assertThat(animalImagenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnimalImagens() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList
        restAnimalImagenMockMvc.perform(get("/api/animal-imagens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalImagen.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA)))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))));
    }
    
    @Test
    @Transactional
    public void getAnimalImagen() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get the animalImagen
        restAnimalImagenMockMvc.perform(get("/api/animal-imagens/{id}", animalImagen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(animalImagen.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.nota").value(DEFAULT_NOTA))
            .andExpect(jsonPath("$.imagenContentType").value(DEFAULT_IMAGEN_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagen").value(Base64Utils.encodeToString(DEFAULT_IMAGEN)));
    }


    @Test
    @Transactional
    public void getAnimalImagensByIdFiltering() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        Long id = animalImagen.getId();

        defaultAnimalImagenShouldBeFound("id.equals=" + id);
        defaultAnimalImagenShouldNotBeFound("id.notEquals=" + id);

        defaultAnimalImagenShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnimalImagenShouldNotBeFound("id.greaterThan=" + id);

        defaultAnimalImagenShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnimalImagenShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAnimalImagensByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where fecha equals to DEFAULT_FECHA
        defaultAnimalImagenShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the animalImagenList where fecha equals to UPDATED_FECHA
        defaultAnimalImagenShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalImagensByFechaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where fecha not equals to DEFAULT_FECHA
        defaultAnimalImagenShouldNotBeFound("fecha.notEquals=" + DEFAULT_FECHA);

        // Get all the animalImagenList where fecha not equals to UPDATED_FECHA
        defaultAnimalImagenShouldBeFound("fecha.notEquals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalImagensByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultAnimalImagenShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the animalImagenList where fecha equals to UPDATED_FECHA
        defaultAnimalImagenShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalImagensByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where fecha is not null
        defaultAnimalImagenShouldBeFound("fecha.specified=true");

        // Get all the animalImagenList where fecha is null
        defaultAnimalImagenShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalImagensByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where fecha is greater than or equal to DEFAULT_FECHA
        defaultAnimalImagenShouldBeFound("fecha.greaterThanOrEqual=" + DEFAULT_FECHA);

        // Get all the animalImagenList where fecha is greater than or equal to UPDATED_FECHA
        defaultAnimalImagenShouldNotBeFound("fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalImagensByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where fecha is less than or equal to DEFAULT_FECHA
        defaultAnimalImagenShouldBeFound("fecha.lessThanOrEqual=" + DEFAULT_FECHA);

        // Get all the animalImagenList where fecha is less than or equal to SMALLER_FECHA
        defaultAnimalImagenShouldNotBeFound("fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalImagensByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where fecha is less than DEFAULT_FECHA
        defaultAnimalImagenShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the animalImagenList where fecha is less than UPDATED_FECHA
        defaultAnimalImagenShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAnimalImagensByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where fecha is greater than DEFAULT_FECHA
        defaultAnimalImagenShouldNotBeFound("fecha.greaterThan=" + DEFAULT_FECHA);

        // Get all the animalImagenList where fecha is greater than SMALLER_FECHA
        defaultAnimalImagenShouldBeFound("fecha.greaterThan=" + SMALLER_FECHA);
    }


    @Test
    @Transactional
    public void getAllAnimalImagensByNotaIsEqualToSomething() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where nota equals to DEFAULT_NOTA
        defaultAnimalImagenShouldBeFound("nota.equals=" + DEFAULT_NOTA);

        // Get all the animalImagenList where nota equals to UPDATED_NOTA
        defaultAnimalImagenShouldNotBeFound("nota.equals=" + UPDATED_NOTA);
    }

    @Test
    @Transactional
    public void getAllAnimalImagensByNotaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where nota not equals to DEFAULT_NOTA
        defaultAnimalImagenShouldNotBeFound("nota.notEquals=" + DEFAULT_NOTA);

        // Get all the animalImagenList where nota not equals to UPDATED_NOTA
        defaultAnimalImagenShouldBeFound("nota.notEquals=" + UPDATED_NOTA);
    }

    @Test
    @Transactional
    public void getAllAnimalImagensByNotaIsInShouldWork() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where nota in DEFAULT_NOTA or UPDATED_NOTA
        defaultAnimalImagenShouldBeFound("nota.in=" + DEFAULT_NOTA + "," + UPDATED_NOTA);

        // Get all the animalImagenList where nota equals to UPDATED_NOTA
        defaultAnimalImagenShouldNotBeFound("nota.in=" + UPDATED_NOTA);
    }

    @Test
    @Transactional
    public void getAllAnimalImagensByNotaIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where nota is not null
        defaultAnimalImagenShouldBeFound("nota.specified=true");

        // Get all the animalImagenList where nota is null
        defaultAnimalImagenShouldNotBeFound("nota.specified=false");
    }
                @Test
    @Transactional
    public void getAllAnimalImagensByNotaContainsSomething() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where nota contains DEFAULT_NOTA
        defaultAnimalImagenShouldBeFound("nota.contains=" + DEFAULT_NOTA);

        // Get all the animalImagenList where nota contains UPDATED_NOTA
        defaultAnimalImagenShouldNotBeFound("nota.contains=" + UPDATED_NOTA);
    }

    @Test
    @Transactional
    public void getAllAnimalImagensByNotaNotContainsSomething() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);

        // Get all the animalImagenList where nota does not contain DEFAULT_NOTA
        defaultAnimalImagenShouldNotBeFound("nota.doesNotContain=" + DEFAULT_NOTA);

        // Get all the animalImagenList where nota does not contain UPDATED_NOTA
        defaultAnimalImagenShouldBeFound("nota.doesNotContain=" + UPDATED_NOTA);
    }


    @Test
    @Transactional
    public void getAllAnimalImagensByAnimalIsEqualToSomething() throws Exception {
        // Initialize the database
        animalImagenRepository.saveAndFlush(animalImagen);
        Animal animal = AnimalResourceIT.createEntity(em);
        em.persist(animal);
        em.flush();
        animalImagen.setAnimal(animal);
        animalImagenRepository.saveAndFlush(animalImagen);
        Long animalId = animal.getId();

        // Get all the animalImagenList where animal equals to animalId
        defaultAnimalImagenShouldBeFound("animalId.equals=" + animalId);

        // Get all the animalImagenList where animal equals to animalId + 1
        defaultAnimalImagenShouldNotBeFound("animalId.equals=" + (animalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnimalImagenShouldBeFound(String filter) throws Exception {
        restAnimalImagenMockMvc.perform(get("/api/animal-imagens?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalImagen.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA)))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))));

        // Check, that the count call also returns 1
        restAnimalImagenMockMvc.perform(get("/api/animal-imagens/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnimalImagenShouldNotBeFound(String filter) throws Exception {
        restAnimalImagenMockMvc.perform(get("/api/animal-imagens?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnimalImagenMockMvc.perform(get("/api/animal-imagens/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAnimalImagen() throws Exception {
        // Get the animalImagen
        restAnimalImagenMockMvc.perform(get("/api/animal-imagens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnimalImagen() throws Exception {
        // Initialize the database
        animalImagenService.save(animalImagen);

        int databaseSizeBeforeUpdate = animalImagenRepository.findAll().size();

        // Update the animalImagen
        AnimalImagen updatedAnimalImagen = animalImagenRepository.findById(animalImagen.getId()).get();
        // Disconnect from session so that the updates on updatedAnimalImagen are not directly saved in db
        em.detach(updatedAnimalImagen);
        updatedAnimalImagen
            .fecha(UPDATED_FECHA)
            .nota(UPDATED_NOTA)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE);

        restAnimalImagenMockMvc.perform(put("/api/animal-imagens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnimalImagen)))
            .andExpect(status().isOk());

        // Validate the AnimalImagen in the database
        List<AnimalImagen> animalImagenList = animalImagenRepository.findAll();
        assertThat(animalImagenList).hasSize(databaseSizeBeforeUpdate);
        AnimalImagen testAnimalImagen = animalImagenList.get(animalImagenList.size() - 1);
        assertThat(testAnimalImagen.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testAnimalImagen.getNota()).isEqualTo(UPDATED_NOTA);
        assertThat(testAnimalImagen.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testAnimalImagen.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);

        // Validate the AnimalImagen in Elasticsearch
        verify(mockAnimalImagenSearchRepository, times(2)).save(testAnimalImagen);
    }

    @Test
    @Transactional
    public void updateNonExistingAnimalImagen() throws Exception {
        int databaseSizeBeforeUpdate = animalImagenRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnimalImagenMockMvc.perform(put("/api/animal-imagens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalImagen)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalImagen in the database
        List<AnimalImagen> animalImagenList = animalImagenRepository.findAll();
        assertThat(animalImagenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnimalImagen in Elasticsearch
        verify(mockAnimalImagenSearchRepository, times(0)).save(animalImagen);
    }

    @Test
    @Transactional
    public void deleteAnimalImagen() throws Exception {
        // Initialize the database
        animalImagenService.save(animalImagen);

        int databaseSizeBeforeDelete = animalImagenRepository.findAll().size();

        // Delete the animalImagen
        restAnimalImagenMockMvc.perform(delete("/api/animal-imagens/{id}", animalImagen.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnimalImagen> animalImagenList = animalImagenRepository.findAll();
        assertThat(animalImagenList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnimalImagen in Elasticsearch
        verify(mockAnimalImagenSearchRepository, times(1)).deleteById(animalImagen.getId());
    }

    @Test
    @Transactional
    public void searchAnimalImagen() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        animalImagenService.save(animalImagen);
        when(mockAnimalImagenSearchRepository.search(queryStringQuery("id:" + animalImagen.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(animalImagen), PageRequest.of(0, 1), 1));

        // Search the animalImagen
        restAnimalImagenMockMvc.perform(get("/api/_search/animal-imagens?query=id:" + animalImagen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalImagen.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA)))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))));
    }
}
