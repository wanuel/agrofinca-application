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
import co.com.cima.agrofinca.domain.AnimalLote;
import co.com.cima.agrofinca.domain.Lote;
import co.com.cima.agrofinca.domain.Parametros;
import co.com.cima.agrofinca.domain.PotreroPastoreo;
import co.com.cima.agrofinca.domain.enumeration.ESTADOLOTE;
import co.com.cima.agrofinca.repository.LoteRepository;
import co.com.cima.agrofinca.repository.search.LoteSearchRepository;
import co.com.cima.agrofinca.service.LoteQueryService;
import co.com.cima.agrofinca.service.LoteService;
import co.com.cima.agrofinca.service.dto.LoteCriteria;
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
 * Integration tests for the {@link LoteResource} REST controller.
 */
@SpringBootTest(classes = { AgrofincaApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class LoteResourceIT {
    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final ESTADOLOTE DEFAULT_ESTADO = ESTADOLOTE.ACTIVO;
    private static final ESTADOLOTE UPDATED_ESTADO = ESTADOLOTE.INACTIVO;

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private LoteService loteService;

    /**
     * This repository is mocked in the co.com.cima.agrofinca.repository.search test package.
     *
     * @see co.com.cima.agrofinca.repository.search.LoteSearchRepositoryMockConfiguration
     */
    @Autowired
    private LoteSearchRepository mockLoteSearchRepository;

    @Autowired
    private LoteQueryService loteQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoteMockMvc;

    private Lote lote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lote createEntity(EntityManager em) {
        Lote lote = new Lote().nombre(DEFAULT_NOMBRE).fecha(DEFAULT_FECHA).estado(DEFAULT_ESTADO);
        return lote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lote createUpdatedEntity(EntityManager em) {
        Lote lote = new Lote().nombre(UPDATED_NOMBRE).fecha(UPDATED_FECHA).estado(UPDATED_ESTADO);
        return lote;
    }

    @BeforeEach
    public void initTest() {
        lote = createEntity(em);
    }

    @Test
    @Transactional
    public void createLote() throws Exception {
        int databaseSizeBeforeCreate = loteRepository.findAll().size();
        // Create the Lote
        restLoteMockMvc
            .perform(
                post("/api/lotes").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lote))
            )
            .andExpect(status().isCreated());

        // Validate the Lote in the database
        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeCreate + 1);
        Lote testLote = loteList.get(loteList.size() - 1);
        assertThat(testLote.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testLote.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testLote.getEstado()).isEqualTo(DEFAULT_ESTADO);

        // Validate the Lote in Elasticsearch
        verify(mockLoteSearchRepository, times(1)).save(testLote);
    }

    @Test
    @Transactional
    public void createLoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = loteRepository.findAll().size();

        // Create the Lote with an existing ID
        lote.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoteMockMvc
            .perform(
                post("/api/lotes").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lote in the database
        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeCreate);

        // Validate the Lote in Elasticsearch
        verify(mockLoteSearchRepository, times(0)).save(lote);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = loteRepository.findAll().size();
        // set the field null
        lote.setNombre(null);

        // Create the Lote, which fails.

        restLoteMockMvc
            .perform(
                post("/api/lotes").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lote))
            )
            .andExpect(status().isBadRequest());

        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = loteRepository.findAll().size();
        // set the field null
        lote.setFecha(null);

        // Create the Lote, which fails.

        restLoteMockMvc
            .perform(
                post("/api/lotes").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lote))
            )
            .andExpect(status().isBadRequest());

        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = loteRepository.findAll().size();
        // set the field null
        lote.setEstado(null);

        // Create the Lote, which fails.

        restLoteMockMvc
            .perform(
                post("/api/lotes").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lote))
            )
            .andExpect(status().isBadRequest());

        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLotes() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList
        restLoteMockMvc
            .perform(get("/api/lotes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lote.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    public void getLote() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get the lote
        restLoteMockMvc
            .perform(get("/api/lotes/{id}", lote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lote.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    public void getLotesByIdFiltering() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        Long id = lote.getId();

        defaultLoteShouldBeFound("id.equals=" + id);
        defaultLoteShouldNotBeFound("id.notEquals=" + id);

        defaultLoteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLoteShouldNotBeFound("id.greaterThan=" + id);

        defaultLoteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLoteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllLotesByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where nombre equals to DEFAULT_NOMBRE
        defaultLoteShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the loteList where nombre equals to UPDATED_NOMBRE
        defaultLoteShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllLotesByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where nombre not equals to DEFAULT_NOMBRE
        defaultLoteShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the loteList where nombre not equals to UPDATED_NOMBRE
        defaultLoteShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllLotesByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultLoteShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the loteList where nombre equals to UPDATED_NOMBRE
        defaultLoteShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllLotesByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where nombre is not null
        defaultLoteShouldBeFound("nombre.specified=true");

        // Get all the loteList where nombre is null
        defaultLoteShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    public void getAllLotesByNombreContainsSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where nombre contains DEFAULT_NOMBRE
        defaultLoteShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the loteList where nombre contains UPDATED_NOMBRE
        defaultLoteShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllLotesByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where nombre does not contain DEFAULT_NOMBRE
        defaultLoteShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the loteList where nombre does not contain UPDATED_NOMBRE
        defaultLoteShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllLotesByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where fecha equals to DEFAULT_FECHA
        defaultLoteShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the loteList where fecha equals to UPDATED_FECHA
        defaultLoteShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllLotesByFechaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where fecha not equals to DEFAULT_FECHA
        defaultLoteShouldNotBeFound("fecha.notEquals=" + DEFAULT_FECHA);

        // Get all the loteList where fecha not equals to UPDATED_FECHA
        defaultLoteShouldBeFound("fecha.notEquals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllLotesByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultLoteShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the loteList where fecha equals to UPDATED_FECHA
        defaultLoteShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllLotesByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where fecha is not null
        defaultLoteShouldBeFound("fecha.specified=true");

        // Get all the loteList where fecha is null
        defaultLoteShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    public void getAllLotesByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where fecha is greater than or equal to DEFAULT_FECHA
        defaultLoteShouldBeFound("fecha.greaterThanOrEqual=" + DEFAULT_FECHA);

        // Get all the loteList where fecha is greater than or equal to UPDATED_FECHA
        defaultLoteShouldNotBeFound("fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllLotesByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where fecha is less than or equal to DEFAULT_FECHA
        defaultLoteShouldBeFound("fecha.lessThanOrEqual=" + DEFAULT_FECHA);

        // Get all the loteList where fecha is less than or equal to SMALLER_FECHA
        defaultLoteShouldNotBeFound("fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    public void getAllLotesByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where fecha is less than DEFAULT_FECHA
        defaultLoteShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the loteList where fecha is less than UPDATED_FECHA
        defaultLoteShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllLotesByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where fecha is greater than DEFAULT_FECHA
        defaultLoteShouldNotBeFound("fecha.greaterThan=" + DEFAULT_FECHA);

        // Get all the loteList where fecha is greater than SMALLER_FECHA
        defaultLoteShouldBeFound("fecha.greaterThan=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    public void getAllLotesByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where estado equals to DEFAULT_ESTADO
        defaultLoteShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the loteList where estado equals to UPDATED_ESTADO
        defaultLoteShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllLotesByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where estado not equals to DEFAULT_ESTADO
        defaultLoteShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the loteList where estado not equals to UPDATED_ESTADO
        defaultLoteShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllLotesByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultLoteShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the loteList where estado equals to UPDATED_ESTADO
        defaultLoteShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllLotesByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);

        // Get all the loteList where estado is not null
        defaultLoteShouldBeFound("estado.specified=true");

        // Get all the loteList where estado is null
        defaultLoteShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    public void getAllLotesByPastoreosIsEqualToSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);
        PotreroPastoreo pastoreos = PotreroPastoreoResourceIT.createEntity(em);
        em.persist(pastoreos);
        em.flush();
        lote.addPastoreos(pastoreos);
        loteRepository.saveAndFlush(lote);
        Long pastoreosId = pastoreos.getId();

        // Get all the loteList where pastoreos equals to pastoreosId
        defaultLoteShouldBeFound("pastoreosId.equals=" + pastoreosId);

        // Get all the loteList where pastoreos equals to pastoreosId + 1
        defaultLoteShouldNotBeFound("pastoreosId.equals=" + (pastoreosId + 1));
    }

    @Test
    @Transactional
    public void getAllLotesByAnimalesIsEqualToSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);
        AnimalLote animales = AnimalLoteResourceIT.createEntity(em);
        em.persist(animales);
        em.flush();
        lote.addAnimales(animales);
        loteRepository.saveAndFlush(lote);
        Long animalesId = animales.getId();

        // Get all the loteList where animales equals to animalesId
        defaultLoteShouldBeFound("animalesId.equals=" + animalesId);

        // Get all the loteList where animales equals to animalesId + 1
        defaultLoteShouldNotBeFound("animalesId.equals=" + (animalesId + 1));
    }

    @Test
    @Transactional
    public void getAllLotesByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        loteRepository.saveAndFlush(lote);
        Parametros tipo = ParametrosResourceIT.createEntity(em);
        em.persist(tipo);
        em.flush();
        lote.setTipo(tipo);
        loteRepository.saveAndFlush(lote);
        Long tipoId = tipo.getId();

        // Get all the loteList where tipo equals to tipoId
        defaultLoteShouldBeFound("tipoId.equals=" + tipoId);

        // Get all the loteList where tipo equals to tipoId + 1
        defaultLoteShouldNotBeFound("tipoId.equals=" + (tipoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoteShouldBeFound(String filter) throws Exception {
        restLoteMockMvc
            .perform(get("/api/lotes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lote.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));

        // Check, that the count call also returns 1
        restLoteMockMvc
            .perform(get("/api/lotes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoteShouldNotBeFound(String filter) throws Exception {
        restLoteMockMvc
            .perform(get("/api/lotes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoteMockMvc
            .perform(get("/api/lotes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLote() throws Exception {
        // Get the lote
        restLoteMockMvc.perform(get("/api/lotes/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLote() throws Exception {
        // Initialize the database
        loteService.save(lote);

        int databaseSizeBeforeUpdate = loteRepository.findAll().size();

        // Update the lote
        Lote updatedLote = loteRepository.findById(lote.getId()).get();
        // Disconnect from session so that the updates on updatedLote are not directly saved in db
        em.detach(updatedLote);
        updatedLote.nombre(UPDATED_NOMBRE).fecha(UPDATED_FECHA).estado(UPDATED_ESTADO);

        restLoteMockMvc
            .perform(
                put("/api/lotes")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLote))
            )
            .andExpect(status().isOk());

        // Validate the Lote in the database
        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeUpdate);
        Lote testLote = loteList.get(loteList.size() - 1);
        assertThat(testLote.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testLote.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testLote.getEstado()).isEqualTo(UPDATED_ESTADO);

        // Validate the Lote in Elasticsearch
        verify(mockLoteSearchRepository, times(2)).save(testLote);
    }

    @Test
    @Transactional
    public void updateNonExistingLote() throws Exception {
        int databaseSizeBeforeUpdate = loteRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoteMockMvc
            .perform(
                put("/api/lotes").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lote in the database
        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Lote in Elasticsearch
        verify(mockLoteSearchRepository, times(0)).save(lote);
    }

    @Test
    @Transactional
    public void deleteLote() throws Exception {
        // Initialize the database
        loteService.save(lote);

        int databaseSizeBeforeDelete = loteRepository.findAll().size();

        // Delete the lote
        restLoteMockMvc
            .perform(delete("/api/lotes/{id}", lote.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lote> loteList = loteRepository.findAll();
        assertThat(loteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Lote in Elasticsearch
        verify(mockLoteSearchRepository, times(1)).deleteById(lote.getId());
    }

    @Test
    @Transactional
    public void searchLote() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        loteService.save(lote);
        when(mockLoteSearchRepository.search(queryStringQuery("id:" + lote.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(lote), PageRequest.of(0, 1), 1));

        // Search the lote
        restLoteMockMvc
            .perform(get("/api/_search/lotes?query=id:" + lote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lote.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }
}
