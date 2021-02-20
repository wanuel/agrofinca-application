package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.AnimalEvento;
import co.com.cima.agrofinca.repository.AnimalEventoRepository;
import co.com.cima.agrofinca.repository.search.AnimalEventoSearchRepository;
import co.com.cima.agrofinca.service.AnimalEventoService;

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
 * Integration tests for the {@link AnimalEventoResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AnimalEventoResourceIT {

    @Autowired
    private AnimalEventoRepository animalEventoRepository;

    @Autowired
    private AnimalEventoService animalEventoService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.AnimalEventoSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnimalEventoSearchRepository mockAnimalEventoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnimalEventoMockMvc;

    private AnimalEvento animalEvento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalEvento createEntity(EntityManager em) {
        AnimalEvento animalEvento = new AnimalEvento();
        return animalEvento;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalEvento createUpdatedEntity(EntityManager em) {
        AnimalEvento animalEvento = new AnimalEvento();
        return animalEvento;
    }

    @BeforeEach
    public void initTest() {
        animalEvento = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnimalEvento() throws Exception {
        int databaseSizeBeforeCreate = animalEventoRepository.findAll().size();
        // Create the AnimalEvento
        restAnimalEventoMockMvc.perform(post("/api/animal-eventos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalEvento)))
            .andExpect(status().isCreated());

        // Validate the AnimalEvento in the database
        List<AnimalEvento> animalEventoList = animalEventoRepository.findAll();
        assertThat(animalEventoList).hasSize(databaseSizeBeforeCreate + 1);
        AnimalEvento testAnimalEvento = animalEventoList.get(animalEventoList.size() - 1);

        // Validate the AnimalEvento in Elasticsearch
        verify(mockAnimalEventoSearchRepository, times(1)).save(testAnimalEvento);
    }

    @Test
    @Transactional
    public void createAnimalEventoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = animalEventoRepository.findAll().size();

        // Create the AnimalEvento with an existing ID
        animalEvento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnimalEventoMockMvc.perform(post("/api/animal-eventos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalEvento)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalEvento in the database
        List<AnimalEvento> animalEventoList = animalEventoRepository.findAll();
        assertThat(animalEventoList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnimalEvento in Elasticsearch
        verify(mockAnimalEventoSearchRepository, times(0)).save(animalEvento);
    }


    @Test
    @Transactional
    public void getAllAnimalEventos() throws Exception {
        // Initialize the database
        animalEventoRepository.saveAndFlush(animalEvento);

        // Get all the animalEventoList
        restAnimalEventoMockMvc.perform(get("/api/animal-eventos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalEvento.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getAnimalEvento() throws Exception {
        // Initialize the database
        animalEventoRepository.saveAndFlush(animalEvento);

        // Get the animalEvento
        restAnimalEventoMockMvc.perform(get("/api/animal-eventos/{id}", animalEvento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(animalEvento.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingAnimalEvento() throws Exception {
        // Get the animalEvento
        restAnimalEventoMockMvc.perform(get("/api/animal-eventos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnimalEvento() throws Exception {
        // Initialize the database
        animalEventoService.save(animalEvento);

        int databaseSizeBeforeUpdate = animalEventoRepository.findAll().size();

        // Update the animalEvento
        AnimalEvento updatedAnimalEvento = animalEventoRepository.findById(animalEvento.getId()).get();
        // Disconnect from session so that the updates on updatedAnimalEvento are not directly saved in db
        em.detach(updatedAnimalEvento);

        restAnimalEventoMockMvc.perform(put("/api/animal-eventos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnimalEvento)))
            .andExpect(status().isOk());

        // Validate the AnimalEvento in the database
        List<AnimalEvento> animalEventoList = animalEventoRepository.findAll();
        assertThat(animalEventoList).hasSize(databaseSizeBeforeUpdate);
        AnimalEvento testAnimalEvento = animalEventoList.get(animalEventoList.size() - 1);

        // Validate the AnimalEvento in Elasticsearch
        verify(mockAnimalEventoSearchRepository, times(2)).save(testAnimalEvento);
    }

    @Test
    @Transactional
    public void updateNonExistingAnimalEvento() throws Exception {
        int databaseSizeBeforeUpdate = animalEventoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnimalEventoMockMvc.perform(put("/api/animal-eventos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalEvento)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalEvento in the database
        List<AnimalEvento> animalEventoList = animalEventoRepository.findAll();
        assertThat(animalEventoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnimalEvento in Elasticsearch
        verify(mockAnimalEventoSearchRepository, times(0)).save(animalEvento);
    }

    @Test
    @Transactional
    public void deleteAnimalEvento() throws Exception {
        // Initialize the database
        animalEventoService.save(animalEvento);

        int databaseSizeBeforeDelete = animalEventoRepository.findAll().size();

        // Delete the animalEvento
        restAnimalEventoMockMvc.perform(delete("/api/animal-eventos/{id}", animalEvento.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnimalEvento> animalEventoList = animalEventoRepository.findAll();
        assertThat(animalEventoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnimalEvento in Elasticsearch
        verify(mockAnimalEventoSearchRepository, times(1)).deleteById(animalEvento.getId());
    }

    @Test
    @Transactional
    public void searchAnimalEvento() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        animalEventoService.save(animalEvento);
        when(mockAnimalEventoSearchRepository.search(queryStringQuery("id:" + animalEvento.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(animalEvento), PageRequest.of(0, 1), 1));

        // Search the animalEvento
        restAnimalEventoMockMvc.perform(get("/api/_search/animal-eventos?query=id:" + animalEvento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalEvento.getId().intValue())));
    }
}
