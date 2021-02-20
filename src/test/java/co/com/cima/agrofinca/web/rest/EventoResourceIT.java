package co.com.cima.agrofinca.web.rest;

import co.com.cima.agrofinca.AgrofincaApp;
import co.com.cima.agrofinca.config.TestSecurityConfiguration;
import co.com.cima.agrofinca.domain.Evento;
import co.com.cima.agrofinca.domain.AnimalEvento;
import co.com.cima.agrofinca.domain.Parametros;
import co.com.cima.agrofinca.repository.EventoRepository;
import co.com.cima.agrofinca.repository.search.EventoSearchRepository;
import co.com.cima.agrofinca.service.EventoService;
import co.com.cima.agrofinca.service.dto.EventoCriteria;
import co.com.cima.agrofinca.service.EventoQueryService;

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

/**
 * Integration tests for the {@link EventoResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class EventoResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EventoService eventoService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.EventoSearchRepositoryMockConfiguration
     */
    @Autowired
    private EventoSearchRepository mockEventoSearchRepository;

    @Autowired
    private EventoQueryService eventoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventoMockMvc;

    private Evento evento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evento createEntity(EntityManager em) {
        Evento evento = new Evento()
            .fecha(DEFAULT_FECHA)
            .observacion(DEFAULT_OBSERVACION);
        return evento;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evento createUpdatedEntity(EntityManager em) {
        Evento evento = new Evento()
            .fecha(UPDATED_FECHA)
            .observacion(UPDATED_OBSERVACION);
        return evento;
    }

    @BeforeEach
    public void initTest() {
        evento = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvento() throws Exception {
        int databaseSizeBeforeCreate = eventoRepository.findAll().size();
        // Create the Evento
        restEventoMockMvc.perform(post("/api/eventos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isCreated());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeCreate + 1);
        Evento testEvento = eventoList.get(eventoList.size() - 1);
        assertThat(testEvento.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testEvento.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);

        // Validate the Evento in Elasticsearch
        verify(mockEventoSearchRepository, times(1)).save(testEvento);
    }

    @Test
    @Transactional
    public void createEventoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventoRepository.findAll().size();

        // Create the Evento with an existing ID
        evento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventoMockMvc.perform(post("/api/eventos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Evento in Elasticsearch
        verify(mockEventoSearchRepository, times(0)).save(evento);
    }


    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setFecha(null);

        // Create the Evento, which fails.


        restEventoMockMvc.perform(post("/api/eventos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEventos() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList
        restEventoMockMvc.perform(get("/api/eventos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evento.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION)));
    }
    
    @Test
    @Transactional
    public void getEvento() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get the evento
        restEventoMockMvc.perform(get("/api/eventos/{id}", evento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evento.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.observacion").value(DEFAULT_OBSERVACION));
    }


    @Test
    @Transactional
    public void getEventosByIdFiltering() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        Long id = evento.getId();

        defaultEventoShouldBeFound("id.equals=" + id);
        defaultEventoShouldNotBeFound("id.notEquals=" + id);

        defaultEventoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventoShouldNotBeFound("id.greaterThan=" + id);

        defaultEventoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEventosByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where fecha equals to DEFAULT_FECHA
        defaultEventoShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the eventoList where fecha equals to UPDATED_FECHA
        defaultEventoShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllEventosByFechaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where fecha not equals to DEFAULT_FECHA
        defaultEventoShouldNotBeFound("fecha.notEquals=" + DEFAULT_FECHA);

        // Get all the eventoList where fecha not equals to UPDATED_FECHA
        defaultEventoShouldBeFound("fecha.notEquals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllEventosByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultEventoShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the eventoList where fecha equals to UPDATED_FECHA
        defaultEventoShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllEventosByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where fecha is not null
        defaultEventoShouldBeFound("fecha.specified=true");

        // Get all the eventoList where fecha is null
        defaultEventoShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventosByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where fecha is greater than or equal to DEFAULT_FECHA
        defaultEventoShouldBeFound("fecha.greaterThanOrEqual=" + DEFAULT_FECHA);

        // Get all the eventoList where fecha is greater than or equal to UPDATED_FECHA
        defaultEventoShouldNotBeFound("fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllEventosByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where fecha is less than or equal to DEFAULT_FECHA
        defaultEventoShouldBeFound("fecha.lessThanOrEqual=" + DEFAULT_FECHA);

        // Get all the eventoList where fecha is less than or equal to SMALLER_FECHA
        defaultEventoShouldNotBeFound("fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    public void getAllEventosByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where fecha is less than DEFAULT_FECHA
        defaultEventoShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the eventoList where fecha is less than UPDATED_FECHA
        defaultEventoShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllEventosByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where fecha is greater than DEFAULT_FECHA
        defaultEventoShouldNotBeFound("fecha.greaterThan=" + DEFAULT_FECHA);

        // Get all the eventoList where fecha is greater than SMALLER_FECHA
        defaultEventoShouldBeFound("fecha.greaterThan=" + SMALLER_FECHA);
    }


    @Test
    @Transactional
    public void getAllEventosByObservacionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where observacion equals to DEFAULT_OBSERVACION
        defaultEventoShouldBeFound("observacion.equals=" + DEFAULT_OBSERVACION);

        // Get all the eventoList where observacion equals to UPDATED_OBSERVACION
        defaultEventoShouldNotBeFound("observacion.equals=" + UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    public void getAllEventosByObservacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where observacion not equals to DEFAULT_OBSERVACION
        defaultEventoShouldNotBeFound("observacion.notEquals=" + DEFAULT_OBSERVACION);

        // Get all the eventoList where observacion not equals to UPDATED_OBSERVACION
        defaultEventoShouldBeFound("observacion.notEquals=" + UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    public void getAllEventosByObservacionIsInShouldWork() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where observacion in DEFAULT_OBSERVACION or UPDATED_OBSERVACION
        defaultEventoShouldBeFound("observacion.in=" + DEFAULT_OBSERVACION + "," + UPDATED_OBSERVACION);

        // Get all the eventoList where observacion equals to UPDATED_OBSERVACION
        defaultEventoShouldNotBeFound("observacion.in=" + UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    public void getAllEventosByObservacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where observacion is not null
        defaultEventoShouldBeFound("observacion.specified=true");

        // Get all the eventoList where observacion is null
        defaultEventoShouldNotBeFound("observacion.specified=false");
    }
                @Test
    @Transactional
    public void getAllEventosByObservacionContainsSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where observacion contains DEFAULT_OBSERVACION
        defaultEventoShouldBeFound("observacion.contains=" + DEFAULT_OBSERVACION);

        // Get all the eventoList where observacion contains UPDATED_OBSERVACION
        defaultEventoShouldNotBeFound("observacion.contains=" + UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    public void getAllEventosByObservacionNotContainsSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where observacion does not contain DEFAULT_OBSERVACION
        defaultEventoShouldNotBeFound("observacion.doesNotContain=" + DEFAULT_OBSERVACION);

        // Get all the eventoList where observacion does not contain UPDATED_OBSERVACION
        defaultEventoShouldBeFound("observacion.doesNotContain=" + UPDATED_OBSERVACION);
    }


    @Test
    @Transactional
    public void getAllEventosByEventosIsEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);
        AnimalEvento eventos = AnimalEventoResourceIT.createEntity(em);
        em.persist(eventos);
        em.flush();
        evento.addEventos(eventos);
        eventoRepository.saveAndFlush(evento);
        Long eventosId = eventos.getId();

        // Get all the eventoList where eventos equals to eventosId
        defaultEventoShouldBeFound("eventosId.equals=" + eventosId);

        // Get all the eventoList where eventos equals to eventosId + 1
        defaultEventoShouldNotBeFound("eventosId.equals=" + (eventosId + 1));
    }


    @Test
    @Transactional
    public void getAllEventosByEventoIsEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);
        Parametros evento = ParametrosResourceIT.createEntity(em);
        em.persist(evento);
        em.flush();
        evento.setEvento(evento);
        eventoRepository.saveAndFlush(evento);
        Long eventoId = evento.getId();

        // Get all the eventoList where evento equals to eventoId
        defaultEventoShouldBeFound("eventoId.equals=" + eventoId);

        // Get all the eventoList where evento equals to eventoId + 1
        defaultEventoShouldNotBeFound("eventoId.equals=" + (eventoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventoShouldBeFound(String filter) throws Exception {
        restEventoMockMvc.perform(get("/api/eventos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evento.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION)));

        // Check, that the count call also returns 1
        restEventoMockMvc.perform(get("/api/eventos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventoShouldNotBeFound(String filter) throws Exception {
        restEventoMockMvc.perform(get("/api/eventos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventoMockMvc.perform(get("/api/eventos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEvento() throws Exception {
        // Get the evento
        restEventoMockMvc.perform(get("/api/eventos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvento() throws Exception {
        // Initialize the database
        eventoService.save(evento);

        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();

        // Update the evento
        Evento updatedEvento = eventoRepository.findById(evento.getId()).get();
        // Disconnect from session so that the updates on updatedEvento are not directly saved in db
        em.detach(updatedEvento);
        updatedEvento
            .fecha(UPDATED_FECHA)
            .observacion(UPDATED_OBSERVACION);

        restEventoMockMvc.perform(put("/api/eventos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEvento)))
            .andExpect(status().isOk());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
        Evento testEvento = eventoList.get(eventoList.size() - 1);
        assertThat(testEvento.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEvento.getObservacion()).isEqualTo(UPDATED_OBSERVACION);

        // Validate the Evento in Elasticsearch
        verify(mockEventoSearchRepository, times(2)).save(testEvento);
    }

    @Test
    @Transactional
    public void updateNonExistingEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventoMockMvc.perform(put("/api/eventos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Evento in Elasticsearch
        verify(mockEventoSearchRepository, times(0)).save(evento);
    }

    @Test
    @Transactional
    public void deleteEvento() throws Exception {
        // Initialize the database
        eventoService.save(evento);

        int databaseSizeBeforeDelete = eventoRepository.findAll().size();

        // Delete the evento
        restEventoMockMvc.perform(delete("/api/eventos/{id}", evento.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Evento in Elasticsearch
        verify(mockEventoSearchRepository, times(1)).deleteById(evento.getId());
    }

    @Test
    @Transactional
    public void searchEvento() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        eventoService.save(evento);
        when(mockEventoSearchRepository.search(queryStringQuery("id:" + evento.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(evento), PageRequest.of(0, 1), 1));

        // Search the evento
        restEventoMockMvc.perform(get("/api/_search/eventos?query=id:" + evento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evento.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION)));
    }
}
