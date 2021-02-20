package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Persona;
import co.com.cima.agrofinca.repository.PersonaRepository;
import co.com.cima.agrofinca.repository.search.PersonaSearchRepository;
import co.com.cima.agrofinca.service.PersonaService;

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

import co.com.cima.agrofinca.domain.enumeration.TIPODOCUMENTO;
import co.com.cima.agrofinca.domain.enumeration.GENERO;
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
            .numDocuemnto(DEFAULT_NUM_DOCUEMNTO)
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
            .numDocuemnto(UPDATED_NUM_DOCUEMNTO)
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
        restPersonaMockMvc.perform(post("/api/personas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(persona)))
            .andExpect(status().isCreated());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeCreate + 1);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getTipoDocumento()).isEqualTo(DEFAULT_TIPO_DOCUMENTO);
        assertThat(testPersona.getNumDocuemnto()).isEqualTo(DEFAULT_NUM_DOCUEMNTO);
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
        restPersonaMockMvc.perform(post("/api/personas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(persona)))
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


        restPersonaMockMvc.perform(post("/api/personas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(persona)))
            .andExpect(status().isBadRequest());

        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumDocuemntoIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().size();
        // set the field null
        persona.setNumDocuemnto(null);

        // Create the Persona, which fails.


        restPersonaMockMvc.perform(post("/api/personas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(persona)))
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


        restPersonaMockMvc.perform(post("/api/personas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(persona)))
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
        restPersonaMockMvc.perform(get("/api/personas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persona.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].numDocuemnto").value(hasItem(DEFAULT_NUM_DOCUEMNTO.intValue())))
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
        restPersonaMockMvc.perform(get("/api/personas/{id}", persona.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(persona.getId().intValue()))
            .andExpect(jsonPath("$.tipoDocumento").value(DEFAULT_TIPO_DOCUMENTO.toString()))
            .andExpect(jsonPath("$.numDocuemnto").value(DEFAULT_NUM_DOCUEMNTO.intValue()))
            .andExpect(jsonPath("$.primerNombre").value(DEFAULT_PRIMER_NOMBRE))
            .andExpect(jsonPath("$.segundoNombre").value(DEFAULT_SEGUNDO_NOMBRE))
            .andExpect(jsonPath("$.primerApellido").value(DEFAULT_PRIMER_APELLIDO))
            .andExpect(jsonPath("$.segundoApellido").value(DEFAULT_SEGUNDO_APELLIDO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.genero").value(DEFAULT_GENERO.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingPersona() throws Exception {
        // Get the persona
        restPersonaMockMvc.perform(get("/api/personas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
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
            .numDocuemnto(UPDATED_NUM_DOCUEMNTO)
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .genero(UPDATED_GENERO);

        restPersonaMockMvc.perform(put("/api/personas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPersona)))
            .andExpect(status().isOk());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testPersona.getNumDocuemnto()).isEqualTo(UPDATED_NUM_DOCUEMNTO);
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
        restPersonaMockMvc.perform(put("/api/personas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(persona)))
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
        restPersonaMockMvc.perform(delete("/api/personas/{id}", persona.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
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
        restPersonaMockMvc.perform(get("/api/_search/personas?query=id:" + persona.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persona.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].numDocuemnto").value(hasItem(DEFAULT_NUM_DOCUEMNTO.intValue())))
            .andExpect(jsonPath("$.[*].primerNombre").value(hasItem(DEFAULT_PRIMER_NOMBRE)))
            .andExpect(jsonPath("$.[*].segundoNombre").value(hasItem(DEFAULT_SEGUNDO_NOMBRE)))
            .andExpect(jsonPath("$.[*].primerApellido").value(hasItem(DEFAULT_PRIMER_APELLIDO)))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())));
    }
}
