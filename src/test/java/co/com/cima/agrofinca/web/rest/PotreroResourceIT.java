package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Potrero;
import co.com.cima.agrofinca.domain.PotreroPastoreo;
import co.com.cima.agrofinca.domain.Finca;
import co.com.cima.agrofinca.repository.PotreroRepository;
import co.com.cima.agrofinca.repository.search.PotreroSearchRepository;
import co.com.cima.agrofinca.service.PotreroService;
import co.com.cima.agrofinca.service.dto.PotreroCriteria;
import co.com.cima.agrofinca.service.PotreroQueryService;

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
    private static final BigDecimal SMALLER_AREA = new BigDecimal(1 - 1);

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
    private PotreroQueryService potreroQueryService;

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
    public void getPotrerosByIdFiltering() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        Long id = potrero.getId();

        defaultPotreroShouldBeFound("id.equals=" + id);
        defaultPotreroShouldNotBeFound("id.notEquals=" + id);

        defaultPotreroShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPotreroShouldNotBeFound("id.greaterThan=" + id);

        defaultPotreroShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPotreroShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPotrerosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where nombre equals to DEFAULT_NOMBRE
        defaultPotreroShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the potreroList where nombre equals to UPDATED_NOMBRE
        defaultPotreroShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPotrerosByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where nombre not equals to DEFAULT_NOMBRE
        defaultPotreroShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the potreroList where nombre not equals to UPDATED_NOMBRE
        defaultPotreroShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPotrerosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultPotreroShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the potreroList where nombre equals to UPDATED_NOMBRE
        defaultPotreroShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPotrerosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where nombre is not null
        defaultPotreroShouldBeFound("nombre.specified=true");

        // Get all the potreroList where nombre is null
        defaultPotreroShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllPotrerosByNombreContainsSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where nombre contains DEFAULT_NOMBRE
        defaultPotreroShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the potreroList where nombre contains UPDATED_NOMBRE
        defaultPotreroShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPotrerosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where nombre does not contain DEFAULT_NOMBRE
        defaultPotreroShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the potreroList where nombre does not contain UPDATED_NOMBRE
        defaultPotreroShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllPotrerosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where descripcion equals to DEFAULT_DESCRIPCION
        defaultPotreroShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the potreroList where descripcion equals to UPDATED_DESCRIPCION
        defaultPotreroShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllPotrerosByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultPotreroShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the potreroList where descripcion not equals to UPDATED_DESCRIPCION
        defaultPotreroShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllPotrerosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultPotreroShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the potreroList where descripcion equals to UPDATED_DESCRIPCION
        defaultPotreroShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllPotrerosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where descripcion is not null
        defaultPotreroShouldBeFound("descripcion.specified=true");

        // Get all the potreroList where descripcion is null
        defaultPotreroShouldNotBeFound("descripcion.specified=false");
    }
                @Test
    @Transactional
    public void getAllPotrerosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where descripcion contains DEFAULT_DESCRIPCION
        defaultPotreroShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the potreroList where descripcion contains UPDATED_DESCRIPCION
        defaultPotreroShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllPotrerosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultPotreroShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the potreroList where descripcion does not contain UPDATED_DESCRIPCION
        defaultPotreroShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }


    @Test
    @Transactional
    public void getAllPotrerosByPastoIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where pasto equals to DEFAULT_PASTO
        defaultPotreroShouldBeFound("pasto.equals=" + DEFAULT_PASTO);

        // Get all the potreroList where pasto equals to UPDATED_PASTO
        defaultPotreroShouldNotBeFound("pasto.equals=" + UPDATED_PASTO);
    }

    @Test
    @Transactional
    public void getAllPotrerosByPastoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where pasto not equals to DEFAULT_PASTO
        defaultPotreroShouldNotBeFound("pasto.notEquals=" + DEFAULT_PASTO);

        // Get all the potreroList where pasto not equals to UPDATED_PASTO
        defaultPotreroShouldBeFound("pasto.notEquals=" + UPDATED_PASTO);
    }

    @Test
    @Transactional
    public void getAllPotrerosByPastoIsInShouldWork() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where pasto in DEFAULT_PASTO or UPDATED_PASTO
        defaultPotreroShouldBeFound("pasto.in=" + DEFAULT_PASTO + "," + UPDATED_PASTO);

        // Get all the potreroList where pasto equals to UPDATED_PASTO
        defaultPotreroShouldNotBeFound("pasto.in=" + UPDATED_PASTO);
    }

    @Test
    @Transactional
    public void getAllPotrerosByPastoIsNullOrNotNull() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where pasto is not null
        defaultPotreroShouldBeFound("pasto.specified=true");

        // Get all the potreroList where pasto is null
        defaultPotreroShouldNotBeFound("pasto.specified=false");
    }
                @Test
    @Transactional
    public void getAllPotrerosByPastoContainsSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where pasto contains DEFAULT_PASTO
        defaultPotreroShouldBeFound("pasto.contains=" + DEFAULT_PASTO);

        // Get all the potreroList where pasto contains UPDATED_PASTO
        defaultPotreroShouldNotBeFound("pasto.contains=" + UPDATED_PASTO);
    }

    @Test
    @Transactional
    public void getAllPotrerosByPastoNotContainsSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where pasto does not contain DEFAULT_PASTO
        defaultPotreroShouldNotBeFound("pasto.doesNotContain=" + DEFAULT_PASTO);

        // Get all the potreroList where pasto does not contain UPDATED_PASTO
        defaultPotreroShouldBeFound("pasto.doesNotContain=" + UPDATED_PASTO);
    }


    @Test
    @Transactional
    public void getAllPotrerosByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where area equals to DEFAULT_AREA
        defaultPotreroShouldBeFound("area.equals=" + DEFAULT_AREA);

        // Get all the potreroList where area equals to UPDATED_AREA
        defaultPotreroShouldNotBeFound("area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    public void getAllPotrerosByAreaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where area not equals to DEFAULT_AREA
        defaultPotreroShouldNotBeFound("area.notEquals=" + DEFAULT_AREA);

        // Get all the potreroList where area not equals to UPDATED_AREA
        defaultPotreroShouldBeFound("area.notEquals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    public void getAllPotrerosByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where area in DEFAULT_AREA or UPDATED_AREA
        defaultPotreroShouldBeFound("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA);

        // Get all the potreroList where area equals to UPDATED_AREA
        defaultPotreroShouldNotBeFound("area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    public void getAllPotrerosByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where area is not null
        defaultPotreroShouldBeFound("area.specified=true");

        // Get all the potreroList where area is null
        defaultPotreroShouldNotBeFound("area.specified=false");
    }

    @Test
    @Transactional
    public void getAllPotrerosByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where area is greater than or equal to DEFAULT_AREA
        defaultPotreroShouldBeFound("area.greaterThanOrEqual=" + DEFAULT_AREA);

        // Get all the potreroList where area is greater than or equal to UPDATED_AREA
        defaultPotreroShouldNotBeFound("area.greaterThanOrEqual=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    public void getAllPotrerosByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where area is less than or equal to DEFAULT_AREA
        defaultPotreroShouldBeFound("area.lessThanOrEqual=" + DEFAULT_AREA);

        // Get all the potreroList where area is less than or equal to SMALLER_AREA
        defaultPotreroShouldNotBeFound("area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    public void getAllPotrerosByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where area is less than DEFAULT_AREA
        defaultPotreroShouldNotBeFound("area.lessThan=" + DEFAULT_AREA);

        // Get all the potreroList where area is less than UPDATED_AREA
        defaultPotreroShouldBeFound("area.lessThan=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    public void getAllPotrerosByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);

        // Get all the potreroList where area is greater than DEFAULT_AREA
        defaultPotreroShouldNotBeFound("area.greaterThan=" + DEFAULT_AREA);

        // Get all the potreroList where area is greater than SMALLER_AREA
        defaultPotreroShouldBeFound("area.greaterThan=" + SMALLER_AREA);
    }


    @Test
    @Transactional
    public void getAllPotrerosByPastoreosIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);
        PotreroPastoreo pastoreos = PotreroPastoreoResourceIT.createEntity(em);
        em.persist(pastoreos);
        em.flush();
        potrero.addPastoreos(pastoreos);
        potreroRepository.saveAndFlush(potrero);
        Long pastoreosId = pastoreos.getId();

        // Get all the potreroList where pastoreos equals to pastoreosId
        defaultPotreroShouldBeFound("pastoreosId.equals=" + pastoreosId);

        // Get all the potreroList where pastoreos equals to pastoreosId + 1
        defaultPotreroShouldNotBeFound("pastoreosId.equals=" + (pastoreosId + 1));
    }


    @Test
    @Transactional
    public void getAllPotrerosByFincaIsEqualToSomething() throws Exception {
        // Initialize the database
        potreroRepository.saveAndFlush(potrero);
        Finca finca = FincaResourceIT.createEntity(em);
        em.persist(finca);
        em.flush();
        potrero.setFinca(finca);
        potreroRepository.saveAndFlush(potrero);
        Long fincaId = finca.getId();

        // Get all the potreroList where finca equals to fincaId
        defaultPotreroShouldBeFound("fincaId.equals=" + fincaId);

        // Get all the potreroList where finca equals to fincaId + 1
        defaultPotreroShouldNotBeFound("fincaId.equals=" + (fincaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPotreroShouldBeFound(String filter) throws Exception {
        restPotreroMockMvc.perform(get("/api/potreros?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(potrero.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].pasto").value(hasItem(DEFAULT_PASTO)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.intValue())));

        // Check, that the count call also returns 1
        restPotreroMockMvc.perform(get("/api/potreros/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPotreroShouldNotBeFound(String filter) throws Exception {
        restPotreroMockMvc.perform(get("/api/potreros?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPotreroMockMvc.perform(get("/api/potreros/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
