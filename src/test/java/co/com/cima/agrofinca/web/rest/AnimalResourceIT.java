package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Animal;
import co.com.cima.agrofinca.domain.AnimalLote;
import co.com.cima.agrofinca.domain.AnimalImagen;
import co.com.cima.agrofinca.domain.AnimalEvento;
import co.com.cima.agrofinca.domain.Parametros;
import co.com.cima.agrofinca.repository.AnimalRepository;
import co.com.cima.agrofinca.repository.search.AnimalSearchRepository;
import co.com.cima.agrofinca.service.AnimalService;
import co.com.cima.agrofinca.service.dto.AnimalCriteria;
import co.com.cima.agrofinca.service.AnimalQueryService;

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
    private static final LocalDate SMALLER_FECHA_NACIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_COMPRA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_COMPRA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_COMPRA = LocalDate.ofEpochDay(-1L);

    private static final SEXO DEFAULT_SEXO = SEXO.MACHO;
    private static final SEXO UPDATED_SEXO = SEXO.HEMBRA;

    private static final SINO DEFAULT_CASTRADO = SINO.SI;
    private static final SINO UPDATED_CASTRADO = SINO.NO;

    private static final LocalDate DEFAULT_FECHA_CASTRACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_CASTRACION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_CASTRACION = LocalDate.ofEpochDay(-1L);

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
    private AnimalQueryService animalQueryService;

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
    public void getAnimalsByIdFiltering() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        Long id = animal.getId();

        defaultAnimalShouldBeFound("id.equals=" + id);
        defaultAnimalShouldNotBeFound("id.notEquals=" + id);

        defaultAnimalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnimalShouldNotBeFound("id.greaterThan=" + id);

        defaultAnimalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnimalShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAnimalsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where nombre equals to DEFAULT_NOMBRE
        defaultAnimalShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the animalList where nombre equals to UPDATED_NOMBRE
        defaultAnimalShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAnimalsByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where nombre not equals to DEFAULT_NOMBRE
        defaultAnimalShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the animalList where nombre not equals to UPDATED_NOMBRE
        defaultAnimalShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAnimalsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultAnimalShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the animalList where nombre equals to UPDATED_NOMBRE
        defaultAnimalShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAnimalsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where nombre is not null
        defaultAnimalShouldBeFound("nombre.specified=true");

        // Get all the animalList where nombre is null
        defaultAnimalShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllAnimalsByNombreContainsSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where nombre contains DEFAULT_NOMBRE
        defaultAnimalShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the animalList where nombre contains UPDATED_NOMBRE
        defaultAnimalShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAnimalsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where nombre does not contain DEFAULT_NOMBRE
        defaultAnimalShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the animalList where nombre does not contain UPDATED_NOMBRE
        defaultAnimalShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllAnimalsByCaracterizacionIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where caracterizacion equals to DEFAULT_CARACTERIZACION
        defaultAnimalShouldBeFound("caracterizacion.equals=" + DEFAULT_CARACTERIZACION);

        // Get all the animalList where caracterizacion equals to UPDATED_CARACTERIZACION
        defaultAnimalShouldNotBeFound("caracterizacion.equals=" + UPDATED_CARACTERIZACION);
    }

    @Test
    @Transactional
    public void getAllAnimalsByCaracterizacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where caracterizacion not equals to DEFAULT_CARACTERIZACION
        defaultAnimalShouldNotBeFound("caracterizacion.notEquals=" + DEFAULT_CARACTERIZACION);

        // Get all the animalList where caracterizacion not equals to UPDATED_CARACTERIZACION
        defaultAnimalShouldBeFound("caracterizacion.notEquals=" + UPDATED_CARACTERIZACION);
    }

    @Test
    @Transactional
    public void getAllAnimalsByCaracterizacionIsInShouldWork() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where caracterizacion in DEFAULT_CARACTERIZACION or UPDATED_CARACTERIZACION
        defaultAnimalShouldBeFound("caracterizacion.in=" + DEFAULT_CARACTERIZACION + "," + UPDATED_CARACTERIZACION);

        // Get all the animalList where caracterizacion equals to UPDATED_CARACTERIZACION
        defaultAnimalShouldNotBeFound("caracterizacion.in=" + UPDATED_CARACTERIZACION);
    }

    @Test
    @Transactional
    public void getAllAnimalsByCaracterizacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where caracterizacion is not null
        defaultAnimalShouldBeFound("caracterizacion.specified=true");

        // Get all the animalList where caracterizacion is null
        defaultAnimalShouldNotBeFound("caracterizacion.specified=false");
    }
                @Test
    @Transactional
    public void getAllAnimalsByCaracterizacionContainsSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where caracterizacion contains DEFAULT_CARACTERIZACION
        defaultAnimalShouldBeFound("caracterizacion.contains=" + DEFAULT_CARACTERIZACION);

        // Get all the animalList where caracterizacion contains UPDATED_CARACTERIZACION
        defaultAnimalShouldNotBeFound("caracterizacion.contains=" + UPDATED_CARACTERIZACION);
    }

    @Test
    @Transactional
    public void getAllAnimalsByCaracterizacionNotContainsSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where caracterizacion does not contain DEFAULT_CARACTERIZACION
        defaultAnimalShouldNotBeFound("caracterizacion.doesNotContain=" + DEFAULT_CARACTERIZACION);

        // Get all the animalList where caracterizacion does not contain UPDATED_CARACTERIZACION
        defaultAnimalShouldBeFound("caracterizacion.doesNotContain=" + UPDATED_CARACTERIZACION);
    }


    @Test
    @Transactional
    public void getAllAnimalsByHierroIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where hierro equals to DEFAULT_HIERRO
        defaultAnimalShouldBeFound("hierro.equals=" + DEFAULT_HIERRO);

        // Get all the animalList where hierro equals to UPDATED_HIERRO
        defaultAnimalShouldNotBeFound("hierro.equals=" + UPDATED_HIERRO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByHierroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where hierro not equals to DEFAULT_HIERRO
        defaultAnimalShouldNotBeFound("hierro.notEquals=" + DEFAULT_HIERRO);

        // Get all the animalList where hierro not equals to UPDATED_HIERRO
        defaultAnimalShouldBeFound("hierro.notEquals=" + UPDATED_HIERRO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByHierroIsInShouldWork() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where hierro in DEFAULT_HIERRO or UPDATED_HIERRO
        defaultAnimalShouldBeFound("hierro.in=" + DEFAULT_HIERRO + "," + UPDATED_HIERRO);

        // Get all the animalList where hierro equals to UPDATED_HIERRO
        defaultAnimalShouldNotBeFound("hierro.in=" + UPDATED_HIERRO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByHierroIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where hierro is not null
        defaultAnimalShouldBeFound("hierro.specified=true");

        // Get all the animalList where hierro is null
        defaultAnimalShouldNotBeFound("hierro.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaNacimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaNacimiento equals to DEFAULT_FECHA_NACIMIENTO
        defaultAnimalShouldBeFound("fechaNacimiento.equals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the animalList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultAnimalShouldNotBeFound("fechaNacimiento.equals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaNacimientoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaNacimiento not equals to DEFAULT_FECHA_NACIMIENTO
        defaultAnimalShouldNotBeFound("fechaNacimiento.notEquals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the animalList where fechaNacimiento not equals to UPDATED_FECHA_NACIMIENTO
        defaultAnimalShouldBeFound("fechaNacimiento.notEquals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaNacimientoIsInShouldWork() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaNacimiento in DEFAULT_FECHA_NACIMIENTO or UPDATED_FECHA_NACIMIENTO
        defaultAnimalShouldBeFound("fechaNacimiento.in=" + DEFAULT_FECHA_NACIMIENTO + "," + UPDATED_FECHA_NACIMIENTO);

        // Get all the animalList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultAnimalShouldNotBeFound("fechaNacimiento.in=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaNacimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaNacimiento is not null
        defaultAnimalShouldBeFound("fechaNacimiento.specified=true");

        // Get all the animalList where fechaNacimiento is null
        defaultAnimalShouldNotBeFound("fechaNacimiento.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaNacimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaNacimiento is greater than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultAnimalShouldBeFound("fechaNacimiento.greaterThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the animalList where fechaNacimiento is greater than or equal to UPDATED_FECHA_NACIMIENTO
        defaultAnimalShouldNotBeFound("fechaNacimiento.greaterThanOrEqual=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaNacimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaNacimiento is less than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultAnimalShouldBeFound("fechaNacimiento.lessThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the animalList where fechaNacimiento is less than or equal to SMALLER_FECHA_NACIMIENTO
        defaultAnimalShouldNotBeFound("fechaNacimiento.lessThanOrEqual=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaNacimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaNacimiento is less than DEFAULT_FECHA_NACIMIENTO
        defaultAnimalShouldNotBeFound("fechaNacimiento.lessThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the animalList where fechaNacimiento is less than UPDATED_FECHA_NACIMIENTO
        defaultAnimalShouldBeFound("fechaNacimiento.lessThan=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaNacimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaNacimiento is greater than DEFAULT_FECHA_NACIMIENTO
        defaultAnimalShouldNotBeFound("fechaNacimiento.greaterThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the animalList where fechaNacimiento is greater than SMALLER_FECHA_NACIMIENTO
        defaultAnimalShouldBeFound("fechaNacimiento.greaterThan=" + SMALLER_FECHA_NACIMIENTO);
    }


    @Test
    @Transactional
    public void getAllAnimalsByFechaCompraIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCompra equals to DEFAULT_FECHA_COMPRA
        defaultAnimalShouldBeFound("fechaCompra.equals=" + DEFAULT_FECHA_COMPRA);

        // Get all the animalList where fechaCompra equals to UPDATED_FECHA_COMPRA
        defaultAnimalShouldNotBeFound("fechaCompra.equals=" + UPDATED_FECHA_COMPRA);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCompraIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCompra not equals to DEFAULT_FECHA_COMPRA
        defaultAnimalShouldNotBeFound("fechaCompra.notEquals=" + DEFAULT_FECHA_COMPRA);

        // Get all the animalList where fechaCompra not equals to UPDATED_FECHA_COMPRA
        defaultAnimalShouldBeFound("fechaCompra.notEquals=" + UPDATED_FECHA_COMPRA);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCompraIsInShouldWork() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCompra in DEFAULT_FECHA_COMPRA or UPDATED_FECHA_COMPRA
        defaultAnimalShouldBeFound("fechaCompra.in=" + DEFAULT_FECHA_COMPRA + "," + UPDATED_FECHA_COMPRA);

        // Get all the animalList where fechaCompra equals to UPDATED_FECHA_COMPRA
        defaultAnimalShouldNotBeFound("fechaCompra.in=" + UPDATED_FECHA_COMPRA);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCompraIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCompra is not null
        defaultAnimalShouldBeFound("fechaCompra.specified=true");

        // Get all the animalList where fechaCompra is null
        defaultAnimalShouldNotBeFound("fechaCompra.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCompraIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCompra is greater than or equal to DEFAULT_FECHA_COMPRA
        defaultAnimalShouldBeFound("fechaCompra.greaterThanOrEqual=" + DEFAULT_FECHA_COMPRA);

        // Get all the animalList where fechaCompra is greater than or equal to UPDATED_FECHA_COMPRA
        defaultAnimalShouldNotBeFound("fechaCompra.greaterThanOrEqual=" + UPDATED_FECHA_COMPRA);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCompraIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCompra is less than or equal to DEFAULT_FECHA_COMPRA
        defaultAnimalShouldBeFound("fechaCompra.lessThanOrEqual=" + DEFAULT_FECHA_COMPRA);

        // Get all the animalList where fechaCompra is less than or equal to SMALLER_FECHA_COMPRA
        defaultAnimalShouldNotBeFound("fechaCompra.lessThanOrEqual=" + SMALLER_FECHA_COMPRA);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCompraIsLessThanSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCompra is less than DEFAULT_FECHA_COMPRA
        defaultAnimalShouldNotBeFound("fechaCompra.lessThan=" + DEFAULT_FECHA_COMPRA);

        // Get all the animalList where fechaCompra is less than UPDATED_FECHA_COMPRA
        defaultAnimalShouldBeFound("fechaCompra.lessThan=" + UPDATED_FECHA_COMPRA);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCompraIsGreaterThanSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCompra is greater than DEFAULT_FECHA_COMPRA
        defaultAnimalShouldNotBeFound("fechaCompra.greaterThan=" + DEFAULT_FECHA_COMPRA);

        // Get all the animalList where fechaCompra is greater than SMALLER_FECHA_COMPRA
        defaultAnimalShouldBeFound("fechaCompra.greaterThan=" + SMALLER_FECHA_COMPRA);
    }


    @Test
    @Transactional
    public void getAllAnimalsBySexoIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where sexo equals to DEFAULT_SEXO
        defaultAnimalShouldBeFound("sexo.equals=" + DEFAULT_SEXO);

        // Get all the animalList where sexo equals to UPDATED_SEXO
        defaultAnimalShouldNotBeFound("sexo.equals=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    public void getAllAnimalsBySexoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where sexo not equals to DEFAULT_SEXO
        defaultAnimalShouldNotBeFound("sexo.notEquals=" + DEFAULT_SEXO);

        // Get all the animalList where sexo not equals to UPDATED_SEXO
        defaultAnimalShouldBeFound("sexo.notEquals=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    public void getAllAnimalsBySexoIsInShouldWork() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where sexo in DEFAULT_SEXO or UPDATED_SEXO
        defaultAnimalShouldBeFound("sexo.in=" + DEFAULT_SEXO + "," + UPDATED_SEXO);

        // Get all the animalList where sexo equals to UPDATED_SEXO
        defaultAnimalShouldNotBeFound("sexo.in=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    public void getAllAnimalsBySexoIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where sexo is not null
        defaultAnimalShouldBeFound("sexo.specified=true");

        // Get all the animalList where sexo is null
        defaultAnimalShouldNotBeFound("sexo.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalsByCastradoIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where castrado equals to DEFAULT_CASTRADO
        defaultAnimalShouldBeFound("castrado.equals=" + DEFAULT_CASTRADO);

        // Get all the animalList where castrado equals to UPDATED_CASTRADO
        defaultAnimalShouldNotBeFound("castrado.equals=" + UPDATED_CASTRADO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByCastradoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where castrado not equals to DEFAULT_CASTRADO
        defaultAnimalShouldNotBeFound("castrado.notEquals=" + DEFAULT_CASTRADO);

        // Get all the animalList where castrado not equals to UPDATED_CASTRADO
        defaultAnimalShouldBeFound("castrado.notEquals=" + UPDATED_CASTRADO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByCastradoIsInShouldWork() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where castrado in DEFAULT_CASTRADO or UPDATED_CASTRADO
        defaultAnimalShouldBeFound("castrado.in=" + DEFAULT_CASTRADO + "," + UPDATED_CASTRADO);

        // Get all the animalList where castrado equals to UPDATED_CASTRADO
        defaultAnimalShouldNotBeFound("castrado.in=" + UPDATED_CASTRADO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByCastradoIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where castrado is not null
        defaultAnimalShouldBeFound("castrado.specified=true");

        // Get all the animalList where castrado is null
        defaultAnimalShouldNotBeFound("castrado.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCastracionIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCastracion equals to DEFAULT_FECHA_CASTRACION
        defaultAnimalShouldBeFound("fechaCastracion.equals=" + DEFAULT_FECHA_CASTRACION);

        // Get all the animalList where fechaCastracion equals to UPDATED_FECHA_CASTRACION
        defaultAnimalShouldNotBeFound("fechaCastracion.equals=" + UPDATED_FECHA_CASTRACION);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCastracionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCastracion not equals to DEFAULT_FECHA_CASTRACION
        defaultAnimalShouldNotBeFound("fechaCastracion.notEquals=" + DEFAULT_FECHA_CASTRACION);

        // Get all the animalList where fechaCastracion not equals to UPDATED_FECHA_CASTRACION
        defaultAnimalShouldBeFound("fechaCastracion.notEquals=" + UPDATED_FECHA_CASTRACION);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCastracionIsInShouldWork() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCastracion in DEFAULT_FECHA_CASTRACION or UPDATED_FECHA_CASTRACION
        defaultAnimalShouldBeFound("fechaCastracion.in=" + DEFAULT_FECHA_CASTRACION + "," + UPDATED_FECHA_CASTRACION);

        // Get all the animalList where fechaCastracion equals to UPDATED_FECHA_CASTRACION
        defaultAnimalShouldNotBeFound("fechaCastracion.in=" + UPDATED_FECHA_CASTRACION);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCastracionIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCastracion is not null
        defaultAnimalShouldBeFound("fechaCastracion.specified=true");

        // Get all the animalList where fechaCastracion is null
        defaultAnimalShouldNotBeFound("fechaCastracion.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCastracionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCastracion is greater than or equal to DEFAULT_FECHA_CASTRACION
        defaultAnimalShouldBeFound("fechaCastracion.greaterThanOrEqual=" + DEFAULT_FECHA_CASTRACION);

        // Get all the animalList where fechaCastracion is greater than or equal to UPDATED_FECHA_CASTRACION
        defaultAnimalShouldNotBeFound("fechaCastracion.greaterThanOrEqual=" + UPDATED_FECHA_CASTRACION);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCastracionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCastracion is less than or equal to DEFAULT_FECHA_CASTRACION
        defaultAnimalShouldBeFound("fechaCastracion.lessThanOrEqual=" + DEFAULT_FECHA_CASTRACION);

        // Get all the animalList where fechaCastracion is less than or equal to SMALLER_FECHA_CASTRACION
        defaultAnimalShouldNotBeFound("fechaCastracion.lessThanOrEqual=" + SMALLER_FECHA_CASTRACION);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCastracionIsLessThanSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCastracion is less than DEFAULT_FECHA_CASTRACION
        defaultAnimalShouldNotBeFound("fechaCastracion.lessThan=" + DEFAULT_FECHA_CASTRACION);

        // Get all the animalList where fechaCastracion is less than UPDATED_FECHA_CASTRACION
        defaultAnimalShouldBeFound("fechaCastracion.lessThan=" + UPDATED_FECHA_CASTRACION);
    }

    @Test
    @Transactional
    public void getAllAnimalsByFechaCastracionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where fechaCastracion is greater than DEFAULT_FECHA_CASTRACION
        defaultAnimalShouldNotBeFound("fechaCastracion.greaterThan=" + DEFAULT_FECHA_CASTRACION);

        // Get all the animalList where fechaCastracion is greater than SMALLER_FECHA_CASTRACION
        defaultAnimalShouldBeFound("fechaCastracion.greaterThan=" + SMALLER_FECHA_CASTRACION);
    }


    @Test
    @Transactional
    public void getAllAnimalsByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where estado equals to DEFAULT_ESTADO
        defaultAnimalShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the animalList where estado equals to UPDATED_ESTADO
        defaultAnimalShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where estado not equals to DEFAULT_ESTADO
        defaultAnimalShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the animalList where estado not equals to UPDATED_ESTADO
        defaultAnimalShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultAnimalShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the animalList where estado equals to UPDATED_ESTADO
        defaultAnimalShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllAnimalsByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList where estado is not null
        defaultAnimalShouldBeFound("estado.specified=true");

        // Get all the animalList where estado is null
        defaultAnimalShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnimalsByLotesIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);
        AnimalLote lotes = AnimalLoteResourceIT.createEntity(em);
        em.persist(lotes);
        em.flush();
        animal.addLotes(lotes);
        animalRepository.saveAndFlush(animal);
        Long lotesId = lotes.getId();

        // Get all the animalList where lotes equals to lotesId
        defaultAnimalShouldBeFound("lotesId.equals=" + lotesId);

        // Get all the animalList where lotes equals to lotesId + 1
        defaultAnimalShouldNotBeFound("lotesId.equals=" + (lotesId + 1));
    }


    @Test
    @Transactional
    public void getAllAnimalsByImagenesIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);
        AnimalImagen imagenes = AnimalImagenResourceIT.createEntity(em);
        em.persist(imagenes);
        em.flush();
        animal.addImagenes(imagenes);
        animalRepository.saveAndFlush(animal);
        Long imagenesId = imagenes.getId();

        // Get all the animalList where imagenes equals to imagenesId
        defaultAnimalShouldBeFound("imagenesId.equals=" + imagenesId);

        // Get all the animalList where imagenes equals to imagenesId + 1
        defaultAnimalShouldNotBeFound("imagenesId.equals=" + (imagenesId + 1));
    }


    @Test
    @Transactional
    public void getAllAnimalsByEventosIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);
        AnimalEvento eventos = AnimalEventoResourceIT.createEntity(em);
        em.persist(eventos);
        em.flush();
        animal.addEventos(eventos);
        animalRepository.saveAndFlush(animal);
        Long eventosId = eventos.getId();

        // Get all the animalList where eventos equals to eventosId
        defaultAnimalShouldBeFound("eventosId.equals=" + eventosId);

        // Get all the animalList where eventos equals to eventosId + 1
        defaultAnimalShouldNotBeFound("eventosId.equals=" + (eventosId + 1));
    }


    @Test
    @Transactional
    public void getAllAnimalsByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);
        Parametros tipo = ParametrosResourceIT.createEntity(em);
        em.persist(tipo);
        em.flush();
        animal.setTipo(tipo);
        animalRepository.saveAndFlush(animal);
        Long tipoId = tipo.getId();

        // Get all the animalList where tipo equals to tipoId
        defaultAnimalShouldBeFound("tipoId.equals=" + tipoId);

        // Get all the animalList where tipo equals to tipoId + 1
        defaultAnimalShouldNotBeFound("tipoId.equals=" + (tipoId + 1));
    }


    @Test
    @Transactional
    public void getAllAnimalsByRazaIsEqualToSomething() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);
        Parametros raza = ParametrosResourceIT.createEntity(em);
        em.persist(raza);
        em.flush();
        animal.setRaza(raza);
        animalRepository.saveAndFlush(animal);
        Long razaId = raza.getId();

        // Get all the animalList where raza equals to razaId
        defaultAnimalShouldBeFound("razaId.equals=" + razaId);

        // Get all the animalList where raza equals to razaId + 1
        defaultAnimalShouldNotBeFound("razaId.equals=" + (razaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnimalShouldBeFound(String filter) throws Exception {
        restAnimalMockMvc.perform(get("/api/animals?sort=id,desc&" + filter))
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

        // Check, that the count call also returns 1
        restAnimalMockMvc.perform(get("/api/animals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnimalShouldNotBeFound(String filter) throws Exception {
        restAnimalMockMvc.perform(get("/api/animals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnimalMockMvc.perform(get("/api/animals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
