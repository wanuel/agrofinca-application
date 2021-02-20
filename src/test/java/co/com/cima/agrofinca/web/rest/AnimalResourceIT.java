package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Animal;
import co.com.cima.agrofinca.repository.AnimalRepository;
import co.com.cima.agrofinca.repository.search.AnimalSearchRepository;
import co.com.cima.agrofinca.service.AnimalService;

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
import co.com.cima.agrofinca.domain.enumeration.SEXO;
import co.com.cima.agrofinca.domain.enumeration.SINO;
import co.com.cima.agrofinca.domain.enumeration.ESTADOANIMAL;
/**
 * Integration tests for the {@link AnimalResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AnimalResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CARACTERIZACION = "AAAAAAAAAA";
    private static final String UPDATED_CARACTERIZACION = "BBBBBBBBBB";

    private static final SINO DEFAULT_HIERRO = SINO.SI;
    private static final SINO UPDATED_HIERRO = SINO.NO;

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_COMPRA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_COMPRA = LocalDate.now(ZoneId.systemDefault());

    private static final SEXO DEFAULT_SEXO = SEXO.MACHO;
    private static final SEXO UPDATED_SEXO = SEXO.HEMBRA;

    private static final SINO DEFAULT_CASTRADO = SINO.SI;
    private static final SINO UPDATED_CASTRADO = SINO.NO;

    private static final LocalDate DEFAULT_FECHA_CASTRACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_CASTRACION = LocalDate.now(ZoneId.systemDefault());

    private static final ESTADOANIMAL DEFAULT_ESTADO = ESTADOANIMAL.VIVO;
    private static final ESTADOANIMAL UPDATED_ESTADO = ESTADOANIMAL.MUERTO;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AnimalService animalService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.AnimalSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnimalSearchRepository mockAnimalSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnimalMockMvc;

    private Animal animal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Animal createEntity(EntityManager em) {
        Animal animal = new Animal()
            .nombre(DEFAULT_NOMBRE)
            .caracterizacion(DEFAULT_CARACTERIZACION)
            .hierro(DEFAULT_HIERRO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .fechaCompra(DEFAULT_FECHA_COMPRA)
            .sexo(DEFAULT_SEXO)
            .castrado(DEFAULT_CASTRADO)
            .fechaCastracion(DEFAULT_FECHA_CASTRACION)
            .estado(DEFAULT_ESTADO);
        return animal;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Animal createUpdatedEntity(EntityManager em) {
        Animal animal = new Animal()
            .nombre(UPDATED_NOMBRE)
            .caracterizacion(UPDATED_CARACTERIZACION)
            .hierro(UPDATED_HIERRO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .fechaCompra(UPDATED_FECHA_COMPRA)
            .sexo(UPDATED_SEXO)
            .castrado(UPDATED_CASTRADO)
            .fechaCastracion(UPDATED_FECHA_CASTRACION)
            .estado(UPDATED_ESTADO);
        return animal;
    }

    @BeforeEach
    public void initTest() {
        animal = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnimal() throws Exception {
        int databaseSizeBeforeCreate = animalRepository.findAll().size();
        // Create the Animal
        restAnimalMockMvc.perform(post("/api/animals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animal)))
            .andExpect(status().isCreated());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeCreate + 1);
        Animal testAnimal = animalList.get(animalList.size() - 1);
        assertThat(testAnimal.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testAnimal.getCaracterizacion()).isEqualTo(DEFAULT_CARACTERIZACION);
        assertThat(testAnimal.getHierro()).isEqualTo(DEFAULT_HIERRO);
        assertThat(testAnimal.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testAnimal.getFechaCompra()).isEqualTo(DEFAULT_FECHA_COMPRA);
        assertThat(testAnimal.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testAnimal.getCastrado()).isEqualTo(DEFAULT_CASTRADO);
        assertThat(testAnimal.getFechaCastracion()).isEqualTo(DEFAULT_FECHA_CASTRACION);
        assertThat(testAnimal.getEstado()).isEqualTo(DEFAULT_ESTADO);

        // Validate the Animal in Elasticsearch
        verify(mockAnimalSearchRepository, times(1)).save(testAnimal);
    }

    @Test
    @Transactional
    public void createAnimalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = animalRepository.findAll().size();

        // Create the Animal with an existing ID
        animal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnimalMockMvc.perform(post("/api/animals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animal)))
            .andExpect(status().isBadRequest());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeCreate);

        // Validate the Animal in Elasticsearch
        verify(mockAnimalSearchRepository, times(0)).save(animal);
    }


    @Test
    @Transactional
    public void checkSexoIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalRepository.findAll().size();
        // set the field null
        animal.setSexo(null);

        // Create the Animal, which fails.


        restAnimalMockMvc.perform(post("/api/animals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animal)))
            .andExpect(status().isBadRequest());

        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCastradoIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalRepository.findAll().size();
        // set the field null
        animal.setCastrado(null);

        // Create the Animal, which fails.


        restAnimalMockMvc.perform(post("/api/animals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animal)))
            .andExpect(status().isBadRequest());

        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalRepository.findAll().size();
        // set the field null
        animal.setEstado(null);

        // Create the Animal, which fails.


        restAnimalMockMvc.perform(post("/api/animals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animal)))
            .andExpect(status().isBadRequest());

        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnimals() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList
        restAnimalMockMvc.perform(get("/api/animals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].caracterizacion").value(hasItem(DEFAULT_CARACTERIZACION)))
            .andExpect(jsonPath("$.[*].hierro").value(hasItem(DEFAULT_HIERRO.toString())))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].fechaCompra").value(hasItem(DEFAULT_FECHA_COMPRA.toString())))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())))
            .andExpect(jsonPath("$.[*].castrado").value(hasItem(DEFAULT_CASTRADO.toString())))
            .andExpect(jsonPath("$.[*].fechaCastracion").value(hasItem(DEFAULT_FECHA_CASTRACION.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }
    
    @Test
    @Transactional
    public void getAnimal() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get the animal
        restAnimalMockMvc.perform(get("/api/animals/{id}", animal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(animal.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.caracterizacion").value(DEFAULT_CARACTERIZACION))
            .andExpect(jsonPath("$.hierro").value(DEFAULT_HIERRO.toString()))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.fechaCompra").value(DEFAULT_FECHA_COMPRA.toString()))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO.toString()))
            .andExpect(jsonPath("$.castrado").value(DEFAULT_CASTRADO.toString()))
            .andExpect(jsonPath("$.fechaCastracion").value(DEFAULT_FECHA_CASTRACION.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingAnimal() throws Exception {
        // Get the animal
        restAnimalMockMvc.perform(get("/api/animals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnimal() throws Exception {
        // Initialize the database
        animalService.save(animal);

        int databaseSizeBeforeUpdate = animalRepository.findAll().size();

        // Update the animal
        Animal updatedAnimal = animalRepository.findById(animal.getId()).get();
        // Disconnect from session so that the updates on updatedAnimal are not directly saved in db
        em.detach(updatedAnimal);
        updatedAnimal
            .nombre(UPDATED_NOMBRE)
            .caracterizacion(UPDATED_CARACTERIZACION)
            .hierro(UPDATED_HIERRO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .fechaCompra(UPDATED_FECHA_COMPRA)
            .sexo(UPDATED_SEXO)
            .castrado(UPDATED_CASTRADO)
            .fechaCastracion(UPDATED_FECHA_CASTRACION)
            .estado(UPDATED_ESTADO);

        restAnimalMockMvc.perform(put("/api/animals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnimal)))
            .andExpect(status().isOk());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeUpdate);
        Animal testAnimal = animalList.get(animalList.size() - 1);
        assertThat(testAnimal.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAnimal.getCaracterizacion()).isEqualTo(UPDATED_CARACTERIZACION);
        assertThat(testAnimal.getHierro()).isEqualTo(UPDATED_HIERRO);
        assertThat(testAnimal.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testAnimal.getFechaCompra()).isEqualTo(UPDATED_FECHA_COMPRA);
        assertThat(testAnimal.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testAnimal.getCastrado()).isEqualTo(UPDATED_CASTRADO);
        assertThat(testAnimal.getFechaCastracion()).isEqualTo(UPDATED_FECHA_CASTRACION);
        assertThat(testAnimal.getEstado()).isEqualTo(UPDATED_ESTADO);

        // Validate the Animal in Elasticsearch
        verify(mockAnimalSearchRepository, times(2)).save(testAnimal);
    }

    @Test
    @Transactional
    public void updateNonExistingAnimal() throws Exception {
        int databaseSizeBeforeUpdate = animalRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnimalMockMvc.perform(put("/api/animals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animal)))
            .andExpect(status().isBadRequest());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Animal in Elasticsearch
        verify(mockAnimalSearchRepository, times(0)).save(animal);
    }

    @Test
    @Transactional
    public void deleteAnimal() throws Exception {
        // Initialize the database
        animalService.save(animal);

        int databaseSizeBeforeDelete = animalRepository.findAll().size();

        // Delete the animal
        restAnimalMockMvc.perform(delete("/api/animals/{id}", animal.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Animal in Elasticsearch
        verify(mockAnimalSearchRepository, times(1)).deleteById(animal.getId());
    }

    @Test
    @Transactional
    public void searchAnimal() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        animalService.save(animal);
        when(mockAnimalSearchRepository.search(queryStringQuery("id:" + animal.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(animal), PageRequest.of(0, 1), 1));

        // Search the animal
        restAnimalMockMvc.perform(get("/api/_search/animals?query=id:" + animal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].caracterizacion").value(hasItem(DEFAULT_CARACTERIZACION)))
            .andExpect(jsonPath("$.[*].hierro").value(hasItem(DEFAULT_HIERRO.toString())))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].fechaCompra").value(hasItem(DEFAULT_FECHA_COMPRA.toString())))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())))
            .andExpect(jsonPath("$.[*].castrado").value(hasItem(DEFAULT_CASTRADO.toString())))
            .andExpect(jsonPath("$.[*].fechaCastracion").value(hasItem(DEFAULT_FECHA_CASTRACION.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }
}
