package co.com.cima.agrofinca.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Persona;
import co.com.cima.agrofinca.domain.Socio;
import co.com.cima.agrofinca.domain.enumeration.GENERO;
import co.com.cima.agrofinca.domain.enumeration.TIPODOCUMENTO;
import co.com.cima.agrofinca.repository.PersonaRepository;
import co.com.cima.agrofinca.repository.search.PersonaSearchRepository;
import co.com.cima.agrofinca.service.PersonaQueryService;
import co.com.cima.agrofinca.service.PersonaService;
import co.com.cima.agrofinca.service.dto.PersonaCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

/**
 * Integration tests for the {@link PersonaResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PersonaResourceIT {
    private static final TIPODOCUMENTO DEFAULT_TIPO_DOCUMENTO = TIPODOCUMENTO.CC;
    private static final TIPODOCUMENTO UPDATED_TIPO_DOCUMENTO = TIPODOCUMENTO.TI;

    private static final Long DEFAULT_NUM_DOCUEMNTO = 1L;
    private static final Long UPDATED_NUM_DOCUEMNTO = 2L;
    private static final Long SMALLER_NUM_DOCUEMNTO = 1L - 1L;

    private static final String DEFAULT_PRIMER_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMER_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_APELLIDO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_NACIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final GENERO DEFAULT_GENERO = GENERO.MASCULINO;
    private static final GENERO UPDATED_GENERO = GENERO.FEMENINO;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PersonaService personaService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.PersonaSearchRepositoryMockConfiguration
     */
    @Autowired
    private PersonaSearchRepository mockPersonaSearchRepository;

    @Autowired
    private PersonaQueryService personaQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonaMockMvc;

    private Persona persona;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persona createEntity(EntityManager em) {
        Persona persona = new Persona()
            .tipoDocumento(DEFAULT_TIPO_DOCUMENTO)
            .numDocumento(DEFAULT_NUM_DOCUEMNTO)
            .primerNombre(DEFAULT_PRIMER_NOMBRE)
            .segundoNombre(DEFAULT_SEGUNDO_NOMBRE)
            .primerApellido(DEFAULT_PRIMER_APELLIDO)
            .segundoApellido(DEFAULT_SEGUNDO_APELLIDO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .genero(DEFAULT_GENERO);
        return persona;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persona createUpdatedEntity(EntityManager em) {
        Persona persona = new Persona()
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .numDocumento(UPDATED_NUM_DOCUEMNTO)
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .genero(UPDATED_GENERO);
        return persona;
    }

    @BeforeEach
    public void initTest() {
        persona = createEntity(em);
    }

    @Test
    @Transactional
    public void createPersona() throws Exception {
        int databaseSizeBeforeCreate = personaRepository.findAll().size();
        // Create the Persona
        restPersonaMockMvc
            .perform(
                post("/api/personas")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(persona))
            )
            .andExpect(status().isCreated());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeCreate + 1);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getTipoDocumento()).isEqualTo(DEFAULT_TIPO_DOCUMENTO);
        assertThat(testPersona.getNumDocumento()).isEqualTo(DEFAULT_NUM_DOCUEMNTO);
        assertThat(testPersona.getPrimerNombre()).isEqualTo(DEFAULT_PRIMER_NOMBRE);
        assertThat(testPersona.getSegundoNombre()).isEqualTo(DEFAULT_SEGUNDO_NOMBRE);
        assertThat(testPersona.getPrimerApellido()).isEqualTo(DEFAULT_PRIMER_APELLIDO);
        assertThat(testPersona.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testPersona.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testPersona.getGenero()).isEqualTo(DEFAULT_GENERO);

        // Validate the Persona in Elasticsearch
        verify(mockPersonaSearchRepository, times(1)).save(testPersona);
    }

    @Test
    @Transactional
    public void createPersonaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personaRepository.findAll().size();

        // Create the Persona with an existing ID
        persona.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonaMockMvc
            .perform(
                post("/api/personas")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(persona))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Persona in Elasticsearch
        verify(mockPersonaSearchRepository, times(0)).save(persona);
    }

    @Test
    @Transactional
    public void checkTipoDocumentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().size();
        // set the field null
        persona.setTipoDocumento(null);

        // Create the Persona, which fails.

        restPersonaMockMvc
            .perform(
                post("/api/personas")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(persona))
            )
            .andExpect(status().isBadRequest());

        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumDocumentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().size();
        // set the field null
        persona.setNumDocumento(null);

        // Create the Persona, which fails.

        restPersonaMockMvc
            .perform(
                post("/api/personas")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(persona))
            )
            .andExpect(status().isBadRequest());

        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrimerNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().size();
        // set the field null
        persona.setPrimerNombre(null);

        // Create the Persona, which fails.

        restPersonaMockMvc
            .perform(
                post("/api/personas")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(persona))
            )
            .andExpect(status().isBadRequest());

        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPersonas() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList
        restPersonaMockMvc
            .perform(get("/api/personas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persona.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].numDocumento").value(hasItem(DEFAULT_NUM_DOCUEMNTO.intValue())))
            .andExpect(jsonPath("$.[*].primerNombre").value(hasItem(DEFAULT_PRIMER_NOMBRE)))
            .andExpect(jsonPath("$.[*].segundoNombre").value(hasItem(DEFAULT_SEGUNDO_NOMBRE)))
            .andExpect(jsonPath("$.[*].primerApellido").value(hasItem(DEFAULT_PRIMER_APELLIDO)))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())));
    }

    @Test
    @Transactional
    public void getPersona() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get the persona
        restPersonaMockMvc
            .perform(get("/api/personas/{id}", persona.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(persona.getId().intValue()))
            .andExpect(jsonPath("$.tipoDocumento").value(DEFAULT_TIPO_DOCUMENTO.toString()))
            .andExpect(jsonPath("$.numDocumento").value(DEFAULT_NUM_DOCUEMNTO.intValue()))
            .andExpect(jsonPath("$.primerNombre").value(DEFAULT_PRIMER_NOMBRE))
            .andExpect(jsonPath("$.segundoNombre").value(DEFAULT_SEGUNDO_NOMBRE))
            .andExpect(jsonPath("$.primerApellido").value(DEFAULT_PRIMER_APELLIDO))
            .andExpect(jsonPath("$.segundoApellido").value(DEFAULT_SEGUNDO_APELLIDO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.genero").value(DEFAULT_GENERO.toString()));
    }

    @Test
    @Transactional
    public void getPersonasByIdFiltering() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        Long id = persona.getId();

        defaultPersonaShouldBeFound("id.equals=" + id);
        defaultPersonaShouldNotBeFound("id.notEquals=" + id);

        defaultPersonaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonaShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllPersonasByTipoDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where tipoDocumento equals to DEFAULT_TIPO_DOCUMENTO
        defaultPersonaShouldBeFound("tipoDocumento.equals=" + DEFAULT_TIPO_DOCUMENTO);

        // Get all the personaList where tipoDocumento equals to UPDATED_TIPO_DOCUMENTO
        defaultPersonaShouldNotBeFound("tipoDocumento.equals=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByTipoDocumentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where tipoDocumento not equals to DEFAULT_TIPO_DOCUMENTO
        defaultPersonaShouldNotBeFound("tipoDocumento.notEquals=" + DEFAULT_TIPO_DOCUMENTO);

        // Get all the personaList where tipoDocumento not equals to UPDATED_TIPO_DOCUMENTO
        defaultPersonaShouldBeFound("tipoDocumento.notEquals=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByTipoDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where tipoDocumento in DEFAULT_TIPO_DOCUMENTO or UPDATED_TIPO_DOCUMENTO
        defaultPersonaShouldBeFound("tipoDocumento.in=" + DEFAULT_TIPO_DOCUMENTO + "," + UPDATED_TIPO_DOCUMENTO);

        // Get all the personaList where tipoDocumento equals to UPDATED_TIPO_DOCUMENTO
        defaultPersonaShouldNotBeFound("tipoDocumento.in=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByTipoDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where tipoDocumento is not null
        defaultPersonaShouldBeFound("tipoDocumento.specified=true");

        // Get all the personaList where tipoDocumento is null
        defaultPersonaShouldNotBeFound("tipoDocumento.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonasByNumDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where numDocumento equals to DEFAULT_NUM_DOCUEMNTO
        defaultPersonaShouldBeFound("numDocumento.equals=" + DEFAULT_NUM_DOCUEMNTO);

        // Get all the personaList where numDocumento equals to UPDATED_NUM_DOCUEMNTO
        defaultPersonaShouldNotBeFound("numDocumento.equals=" + UPDATED_NUM_DOCUEMNTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByNumDocumentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where numDocumento not equals to DEFAULT_NUM_DOCUEMNTO
        defaultPersonaShouldNotBeFound("numDocumento.notEquals=" + DEFAULT_NUM_DOCUEMNTO);

        // Get all the personaList where numDocumento not equals to UPDATED_NUM_DOCUEMNTO
        defaultPersonaShouldBeFound("numDocumento.notEquals=" + UPDATED_NUM_DOCUEMNTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByNumDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where numDocumento in DEFAULT_NUM_DOCUEMNTO or UPDATED_NUM_DOCUEMNTO
        defaultPersonaShouldBeFound("numDocumento.in=" + DEFAULT_NUM_DOCUEMNTO + "," + UPDATED_NUM_DOCUEMNTO);

        // Get all the personaList where numDocumento equals to UPDATED_NUM_DOCUEMNTO
        defaultPersonaShouldNotBeFound("numDocumento.in=" + UPDATED_NUM_DOCUEMNTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByNumDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where numDocumento is not null
        defaultPersonaShouldBeFound("numDocumento.specified=true");

        // Get all the personaList where numDocumento is null
        defaultPersonaShouldNotBeFound("numDocumento.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonasByNumDocumentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where numDocumento is greater than or equal to DEFAULT_NUM_DOCUEMNTO
        defaultPersonaShouldBeFound("numDocumento.greaterThanOrEqual=" + DEFAULT_NUM_DOCUEMNTO);

        // Get all the personaList where numDocumento is greater than or equal to UPDATED_NUM_DOCUEMNTO
        defaultPersonaShouldNotBeFound("numDocumento.greaterThanOrEqual=" + UPDATED_NUM_DOCUEMNTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByNumDocumentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where numDocumento is less than or equal to DEFAULT_NUM_DOCUEMNTO
        defaultPersonaShouldBeFound("numDocumento.lessThanOrEqual=" + DEFAULT_NUM_DOCUEMNTO);

        // Get all the personaList where numDocumento is less than or equal to SMALLER_NUM_DOCUEMNTO
        defaultPersonaShouldNotBeFound("numDocumento.lessThanOrEqual=" + SMALLER_NUM_DOCUEMNTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByNumDocumentoIsLessThanSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where numDocumento is less than DEFAULT_NUM_DOCUEMNTO
        defaultPersonaShouldNotBeFound("numDocumento.lessThan=" + DEFAULT_NUM_DOCUEMNTO);

        // Get all the personaList where numDocumento is less than UPDATED_NUM_DOCUEMNTO
        defaultPersonaShouldBeFound("numDocumento.lessThan=" + UPDATED_NUM_DOCUEMNTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByNumDocumentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where numDocumento is greater than DEFAULT_NUM_DOCUEMNTO
        defaultPersonaShouldNotBeFound("numDocumento.greaterThan=" + DEFAULT_NUM_DOCUEMNTO);

        // Get all the personaList where numDocumento is greater than SMALLER_NUM_DOCUEMNTO
        defaultPersonaShouldBeFound("numDocumento.greaterThan=" + SMALLER_NUM_DOCUEMNTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByPrimerNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where primerNombre equals to DEFAULT_PRIMER_NOMBRE
        defaultPersonaShouldBeFound("primerNombre.equals=" + DEFAULT_PRIMER_NOMBRE);

        // Get all the personaList where primerNombre equals to UPDATED_PRIMER_NOMBRE
        defaultPersonaShouldNotBeFound("primerNombre.equals=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPersonasByPrimerNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where primerNombre not equals to DEFAULT_PRIMER_NOMBRE
        defaultPersonaShouldNotBeFound("primerNombre.notEquals=" + DEFAULT_PRIMER_NOMBRE);

        // Get all the personaList where primerNombre not equals to UPDATED_PRIMER_NOMBRE
        defaultPersonaShouldBeFound("primerNombre.notEquals=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPersonasByPrimerNombreIsInShouldWork() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where primerNombre in DEFAULT_PRIMER_NOMBRE or UPDATED_PRIMER_NOMBRE
        defaultPersonaShouldBeFound("primerNombre.in=" + DEFAULT_PRIMER_NOMBRE + "," + UPDATED_PRIMER_NOMBRE);

        // Get all the personaList where primerNombre equals to UPDATED_PRIMER_NOMBRE
        defaultPersonaShouldNotBeFound("primerNombre.in=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPersonasByPrimerNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where primerNombre is not null
        defaultPersonaShouldBeFound("primerNombre.specified=true");

        // Get all the personaList where primerNombre is null
        defaultPersonaShouldNotBeFound("primerNombre.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonasByPrimerNombreContainsSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where primerNombre contains DEFAULT_PRIMER_NOMBRE
        defaultPersonaShouldBeFound("primerNombre.contains=" + DEFAULT_PRIMER_NOMBRE);

        // Get all the personaList where primerNombre contains UPDATED_PRIMER_NOMBRE
        defaultPersonaShouldNotBeFound("primerNombre.contains=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPersonasByPrimerNombreNotContainsSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where primerNombre does not contain DEFAULT_PRIMER_NOMBRE
        defaultPersonaShouldNotBeFound("primerNombre.doesNotContain=" + DEFAULT_PRIMER_NOMBRE);

        // Get all the personaList where primerNombre does not contain UPDATED_PRIMER_NOMBRE
        defaultPersonaShouldBeFound("primerNombre.doesNotContain=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPersonasBySegundoNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where segundoNombre equals to DEFAULT_SEGUNDO_NOMBRE
        defaultPersonaShouldBeFound("segundoNombre.equals=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the personaList where segundoNombre equals to UPDATED_SEGUNDO_NOMBRE
        defaultPersonaShouldNotBeFound("segundoNombre.equals=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPersonasBySegundoNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where segundoNombre not equals to DEFAULT_SEGUNDO_NOMBRE
        defaultPersonaShouldNotBeFound("segundoNombre.notEquals=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the personaList where segundoNombre not equals to UPDATED_SEGUNDO_NOMBRE
        defaultPersonaShouldBeFound("segundoNombre.notEquals=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPersonasBySegundoNombreIsInShouldWork() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where segundoNombre in DEFAULT_SEGUNDO_NOMBRE or UPDATED_SEGUNDO_NOMBRE
        defaultPersonaShouldBeFound("segundoNombre.in=" + DEFAULT_SEGUNDO_NOMBRE + "," + UPDATED_SEGUNDO_NOMBRE);

        // Get all the personaList where segundoNombre equals to UPDATED_SEGUNDO_NOMBRE
        defaultPersonaShouldNotBeFound("segundoNombre.in=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPersonasBySegundoNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where segundoNombre is not null
        defaultPersonaShouldBeFound("segundoNombre.specified=true");

        // Get all the personaList where segundoNombre is null
        defaultPersonaShouldNotBeFound("segundoNombre.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonasBySegundoNombreContainsSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where segundoNombre contains DEFAULT_SEGUNDO_NOMBRE
        defaultPersonaShouldBeFound("segundoNombre.contains=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the personaList where segundoNombre contains UPDATED_SEGUNDO_NOMBRE
        defaultPersonaShouldNotBeFound("segundoNombre.contains=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPersonasBySegundoNombreNotContainsSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where segundoNombre does not contain DEFAULT_SEGUNDO_NOMBRE
        defaultPersonaShouldNotBeFound("segundoNombre.doesNotContain=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the personaList where segundoNombre does not contain UPDATED_SEGUNDO_NOMBRE
        defaultPersonaShouldBeFound("segundoNombre.doesNotContain=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllPersonasByPrimerApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where primerApellido equals to DEFAULT_PRIMER_APELLIDO
        defaultPersonaShouldBeFound("primerApellido.equals=" + DEFAULT_PRIMER_APELLIDO);

        // Get all the personaList where primerApellido equals to UPDATED_PRIMER_APELLIDO
        defaultPersonaShouldNotBeFound("primerApellido.equals=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllPersonasByPrimerApellidoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where primerApellido not equals to DEFAULT_PRIMER_APELLIDO
        defaultPersonaShouldNotBeFound("primerApellido.notEquals=" + DEFAULT_PRIMER_APELLIDO);

        // Get all the personaList where primerApellido not equals to UPDATED_PRIMER_APELLIDO
        defaultPersonaShouldBeFound("primerApellido.notEquals=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllPersonasByPrimerApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where primerApellido in DEFAULT_PRIMER_APELLIDO or UPDATED_PRIMER_APELLIDO
        defaultPersonaShouldBeFound("primerApellido.in=" + DEFAULT_PRIMER_APELLIDO + "," + UPDATED_PRIMER_APELLIDO);

        // Get all the personaList where primerApellido equals to UPDATED_PRIMER_APELLIDO
        defaultPersonaShouldNotBeFound("primerApellido.in=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllPersonasByPrimerApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where primerApellido is not null
        defaultPersonaShouldBeFound("primerApellido.specified=true");

        // Get all the personaList where primerApellido is null
        defaultPersonaShouldNotBeFound("primerApellido.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonasByPrimerApellidoContainsSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where primerApellido contains DEFAULT_PRIMER_APELLIDO
        defaultPersonaShouldBeFound("primerApellido.contains=" + DEFAULT_PRIMER_APELLIDO);

        // Get all the personaList where primerApellido contains UPDATED_PRIMER_APELLIDO
        defaultPersonaShouldNotBeFound("primerApellido.contains=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllPersonasByPrimerApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where primerApellido does not contain DEFAULT_PRIMER_APELLIDO
        defaultPersonaShouldNotBeFound("primerApellido.doesNotContain=" + DEFAULT_PRIMER_APELLIDO);

        // Get all the personaList where primerApellido does not contain UPDATED_PRIMER_APELLIDO
        defaultPersonaShouldBeFound("primerApellido.doesNotContain=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllPersonasBySegundoApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where segundoApellido equals to DEFAULT_SEGUNDO_APELLIDO
        defaultPersonaShouldBeFound("segundoApellido.equals=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the personaList where segundoApellido equals to UPDATED_SEGUNDO_APELLIDO
        defaultPersonaShouldNotBeFound("segundoApellido.equals=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllPersonasBySegundoApellidoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where segundoApellido not equals to DEFAULT_SEGUNDO_APELLIDO
        defaultPersonaShouldNotBeFound("segundoApellido.notEquals=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the personaList where segundoApellido not equals to UPDATED_SEGUNDO_APELLIDO
        defaultPersonaShouldBeFound("segundoApellido.notEquals=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllPersonasBySegundoApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where segundoApellido in DEFAULT_SEGUNDO_APELLIDO or UPDATED_SEGUNDO_APELLIDO
        defaultPersonaShouldBeFound("segundoApellido.in=" + DEFAULT_SEGUNDO_APELLIDO + "," + UPDATED_SEGUNDO_APELLIDO);

        // Get all the personaList where segundoApellido equals to UPDATED_SEGUNDO_APELLIDO
        defaultPersonaShouldNotBeFound("segundoApellido.in=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllPersonasBySegundoApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where segundoApellido is not null
        defaultPersonaShouldBeFound("segundoApellido.specified=true");

        // Get all the personaList where segundoApellido is null
        defaultPersonaShouldNotBeFound("segundoApellido.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonasBySegundoApellidoContainsSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where segundoApellido contains DEFAULT_SEGUNDO_APELLIDO
        defaultPersonaShouldBeFound("segundoApellido.contains=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the personaList where segundoApellido contains UPDATED_SEGUNDO_APELLIDO
        defaultPersonaShouldNotBeFound("segundoApellido.contains=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllPersonasBySegundoApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where segundoApellido does not contain DEFAULT_SEGUNDO_APELLIDO
        defaultPersonaShouldNotBeFound("segundoApellido.doesNotContain=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the personaList where segundoApellido does not contain UPDATED_SEGUNDO_APELLIDO
        defaultPersonaShouldBeFound("segundoApellido.doesNotContain=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllPersonasByFechaNacimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento equals to DEFAULT_FECHA_NACIMIENTO
        defaultPersonaShouldBeFound("fechaNacimiento.equals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the personaList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultPersonaShouldNotBeFound("fechaNacimiento.equals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByFechaNacimientoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento not equals to DEFAULT_FECHA_NACIMIENTO
        defaultPersonaShouldNotBeFound("fechaNacimiento.notEquals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the personaList where fechaNacimiento not equals to UPDATED_FECHA_NACIMIENTO
        defaultPersonaShouldBeFound("fechaNacimiento.notEquals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByFechaNacimientoIsInShouldWork() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento in DEFAULT_FECHA_NACIMIENTO or UPDATED_FECHA_NACIMIENTO
        defaultPersonaShouldBeFound("fechaNacimiento.in=" + DEFAULT_FECHA_NACIMIENTO + "," + UPDATED_FECHA_NACIMIENTO);

        // Get all the personaList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultPersonaShouldNotBeFound("fechaNacimiento.in=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByFechaNacimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento is not null
        defaultPersonaShouldBeFound("fechaNacimiento.specified=true");

        // Get all the personaList where fechaNacimiento is null
        defaultPersonaShouldNotBeFound("fechaNacimiento.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonasByFechaNacimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento is greater than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultPersonaShouldBeFound("fechaNacimiento.greaterThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the personaList where fechaNacimiento is greater than or equal to UPDATED_FECHA_NACIMIENTO
        defaultPersonaShouldNotBeFound("fechaNacimiento.greaterThanOrEqual=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByFechaNacimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento is less than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultPersonaShouldBeFound("fechaNacimiento.lessThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the personaList where fechaNacimiento is less than or equal to SMALLER_FECHA_NACIMIENTO
        defaultPersonaShouldNotBeFound("fechaNacimiento.lessThanOrEqual=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByFechaNacimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento is less than DEFAULT_FECHA_NACIMIENTO
        defaultPersonaShouldNotBeFound("fechaNacimiento.lessThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the personaList where fechaNacimiento is less than UPDATED_FECHA_NACIMIENTO
        defaultPersonaShouldBeFound("fechaNacimiento.lessThan=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByFechaNacimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento is greater than DEFAULT_FECHA_NACIMIENTO
        defaultPersonaShouldNotBeFound("fechaNacimiento.greaterThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the personaList where fechaNacimiento is greater than SMALLER_FECHA_NACIMIENTO
        defaultPersonaShouldBeFound("fechaNacimiento.greaterThan=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllPersonasByGeneroIsEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where genero equals to DEFAULT_GENERO
        defaultPersonaShouldBeFound("genero.equals=" + DEFAULT_GENERO);

        // Get all the personaList where genero equals to UPDATED_GENERO
        defaultPersonaShouldNotBeFound("genero.equals=" + UPDATED_GENERO);
    }

    @Test
    @Transactional
    public void getAllPersonasByGeneroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where genero not equals to DEFAULT_GENERO
        defaultPersonaShouldNotBeFound("genero.notEquals=" + DEFAULT_GENERO);

        // Get all the personaList where genero not equals to UPDATED_GENERO
        defaultPersonaShouldBeFound("genero.notEquals=" + UPDATED_GENERO);
    }

    @Test
    @Transactional
    public void getAllPersonasByGeneroIsInShouldWork() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where genero in DEFAULT_GENERO or UPDATED_GENERO
        defaultPersonaShouldBeFound("genero.in=" + DEFAULT_GENERO + "," + UPDATED_GENERO);

        // Get all the personaList where genero equals to UPDATED_GENERO
        defaultPersonaShouldNotBeFound("genero.in=" + UPDATED_GENERO);
    }

    @Test
    @Transactional
    public void getAllPersonasByGeneroIsNullOrNotNull() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList where genero is not null
        defaultPersonaShouldBeFound("genero.specified=true");

        // Get all the personaList where genero is null
        defaultPersonaShouldNotBeFound("genero.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonasBySocioIsEqualToSomething() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);
        Socio socio = SocioResourceIT.createEntity(em);
        em.persist(socio);
        em.flush();
        ///persona.setSocio(socio);
        personaRepository.saveAndFlush(persona);
        Long socioId = socio.getId();

        // Get all the personaList where socio equals to socioId
        defaultPersonaShouldBeFound("socioId.equals=" + socioId);

        // Get all the personaList where socio equals to socioId + 1
        defaultPersonaShouldNotBeFound("socioId.equals=" + (socioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonaShouldBeFound(String filter) throws Exception {
        restPersonaMockMvc
            .perform(get("/api/personas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persona.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].numDocumento").value(hasItem(DEFAULT_NUM_DOCUEMNTO.intValue())))
            .andExpect(jsonPath("$.[*].primerNombre").value(hasItem(DEFAULT_PRIMER_NOMBRE)))
            .andExpect(jsonPath("$.[*].segundoNombre").value(hasItem(DEFAULT_SEGUNDO_NOMBRE)))
            .andExpect(jsonPath("$.[*].primerApellido").value(hasItem(DEFAULT_PRIMER_APELLIDO)))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())));

        // Check, that the count call also returns 1
        restPersonaMockMvc
            .perform(get("/api/personas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonaShouldNotBeFound(String filter) throws Exception {
        restPersonaMockMvc
            .perform(get("/api/personas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonaMockMvc
            .perform(get("/api/personas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPersona() throws Exception {
        // Get the persona
        restPersonaMockMvc.perform(get("/api/personas/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersona() throws Exception {
        // Initialize the database
        personaService.save(persona);

        int databaseSizeBeforeUpdate = personaRepository.findAll().size();

        // Update the persona
        Persona updatedPersona = personaRepository.findById(persona.getId()).get();
        // Disconnect from session so that the updates on updatedPersona are not directly saved in db
        em.detach(updatedPersona);
        updatedPersona
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .numDocumento(UPDATED_NUM_DOCUEMNTO)
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .genero(UPDATED_GENERO);

        restPersonaMockMvc
            .perform(
                put("/api/personas")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersona))
            )
            .andExpect(status().isOk());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testPersona.getNumDocumento()).isEqualTo(UPDATED_NUM_DOCUEMNTO);
        assertThat(testPersona.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testPersona.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testPersona.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testPersona.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testPersona.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testPersona.getGenero()).isEqualTo(UPDATED_GENERO);

        // Validate the Persona in Elasticsearch
        verify(mockPersonaSearchRepository, times(2)).save(testPersona);
    }

    @Test
    @Transactional
    public void updateNonExistingPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(
                put("/api/personas")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(persona))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Persona in Elasticsearch
        verify(mockPersonaSearchRepository, times(0)).save(persona);
    }

    @Test
    @Transactional
    public void deletePersona() throws Exception {
        // Initialize the database
        personaService.save(persona);

        int databaseSizeBeforeDelete = personaRepository.findAll().size();

        // Delete the persona
        restPersonaMockMvc
            .perform(delete("/api/personas/{id}", persona.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Persona in Elasticsearch
        verify(mockPersonaSearchRepository, times(1)).deleteById(persona.getId());
    }

    @Test
    @Transactional
    public void searchPersona() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        personaService.save(persona);
        when(mockPersonaSearchRepository.search(queryStringQuery("id:" + persona.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(persona), PageRequest.of(0, 1), 1));

        // Search the persona
        restPersonaMockMvc
            .perform(get("/api/_search/personas?query=id:" + persona.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persona.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].numDocumento").value(hasItem(DEFAULT_NUM_DOCUEMNTO.intValue())))
            .andExpect(jsonPath("$.[*].primerNombre").value(hasItem(DEFAULT_PRIMER_NOMBRE)))
            .andExpect(jsonPath("$.[*].segundoNombre").value(hasItem(DEFAULT_SEGUNDO_NOMBRE)))
            .andExpect(jsonPath("$.[*].primerApellido").value(hasItem(DEFAULT_PRIMER_APELLIDO)))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())));
    }
}
