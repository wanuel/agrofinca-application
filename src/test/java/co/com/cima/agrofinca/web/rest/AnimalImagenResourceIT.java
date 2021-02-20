package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.AnimalImagen;
import co.com.cima.agrofinca.repository.AnimalImagenRepository;
import co.com.cima.agrofinca.repository.search.AnimalImagenSearchRepository;
import co.com.cima.agrofinca.service.AnimalImagenService;

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
