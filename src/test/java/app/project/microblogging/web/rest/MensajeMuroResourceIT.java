package app.project.microblogging.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.project.microblogging.IntegrationTest;
import app.project.microblogging.domain.MensajeMuro;
import app.project.microblogging.repository.MensajeMuroRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MensajeMuroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MensajeMuroResourceIT {

    private static final String DEFAULT_MENSAJE = "AAAAAAAAAA";
    private static final String UPDATED_MENSAJE = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mensaje-muros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MensajeMuroRepository mensajeMuroRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMensajeMuroMockMvc;

    private MensajeMuro mensajeMuro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MensajeMuro createEntity(EntityManager em) {
        MensajeMuro mensajeMuro = new MensajeMuro().mensaje(DEFAULT_MENSAJE).fecha(DEFAULT_FECHA).tags(DEFAULT_TAGS);
        return mensajeMuro;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MensajeMuro createUpdatedEntity(EntityManager em) {
        MensajeMuro mensajeMuro = new MensajeMuro().mensaje(UPDATED_MENSAJE).fecha(UPDATED_FECHA).tags(UPDATED_TAGS);
        return mensajeMuro;
    }

    @BeforeEach
    public void initTest() {
        mensajeMuro = createEntity(em);
    }

    @Test
    @Transactional
    void createMensajeMuro() throws Exception {
        int databaseSizeBeforeCreate = mensajeMuroRepository.findAll().size();
        // Create the MensajeMuro
        restMensajeMuroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensajeMuro)))
            .andExpect(status().isCreated());

        // Validate the MensajeMuro in the database
        List<MensajeMuro> mensajeMuroList = mensajeMuroRepository.findAll();
        assertThat(mensajeMuroList).hasSize(databaseSizeBeforeCreate + 1);
        MensajeMuro testMensajeMuro = mensajeMuroList.get(mensajeMuroList.size() - 1);
        assertThat(testMensajeMuro.getMensaje()).isEqualTo(DEFAULT_MENSAJE);
        assertThat(testMensajeMuro.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testMensajeMuro.getTags()).isEqualTo(DEFAULT_TAGS);
    }

    @Test
    @Transactional
    void createMensajeMuroWithExistingId() throws Exception {
        // Create the MensajeMuro with an existing ID
        mensajeMuro.setId(1L);

        int databaseSizeBeforeCreate = mensajeMuroRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMensajeMuroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensajeMuro)))
            .andExpect(status().isBadRequest());

        // Validate the MensajeMuro in the database
        List<MensajeMuro> mensajeMuroList = mensajeMuroRepository.findAll();
        assertThat(mensajeMuroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMensajeMuros() throws Exception {
        // Initialize the database
        mensajeMuroRepository.saveAndFlush(mensajeMuro);

        // Get all the mensajeMuroList
        restMensajeMuroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mensajeMuro.getId().intValue())))
            .andExpect(jsonPath("$.[*].mensaje").value(hasItem(DEFAULT_MENSAJE)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)));
    }

    @Test
    @Transactional
    void getMensajeMuro() throws Exception {
        // Initialize the database
        mensajeMuroRepository.saveAndFlush(mensajeMuro);

        // Get the mensajeMuro
        restMensajeMuroMockMvc
            .perform(get(ENTITY_API_URL_ID, mensajeMuro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mensajeMuro.getId().intValue()))
            .andExpect(jsonPath("$.mensaje").value(DEFAULT_MENSAJE))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS));
    }

    @Test
    @Transactional
    void getNonExistingMensajeMuro() throws Exception {
        // Get the mensajeMuro
        restMensajeMuroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMensajeMuro() throws Exception {
        // Initialize the database
        mensajeMuroRepository.saveAndFlush(mensajeMuro);

        int databaseSizeBeforeUpdate = mensajeMuroRepository.findAll().size();

        // Update the mensajeMuro
        MensajeMuro updatedMensajeMuro = mensajeMuroRepository.findById(mensajeMuro.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMensajeMuro are not directly saved in db
        em.detach(updatedMensajeMuro);
        updatedMensajeMuro.mensaje(UPDATED_MENSAJE).fecha(UPDATED_FECHA).tags(UPDATED_TAGS);

        restMensajeMuroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMensajeMuro.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMensajeMuro))
            )
            .andExpect(status().isOk());

        // Validate the MensajeMuro in the database
        List<MensajeMuro> mensajeMuroList = mensajeMuroRepository.findAll();
        assertThat(mensajeMuroList).hasSize(databaseSizeBeforeUpdate);
        MensajeMuro testMensajeMuro = mensajeMuroList.get(mensajeMuroList.size() - 1);
        assertThat(testMensajeMuro.getMensaje()).isEqualTo(UPDATED_MENSAJE);
        assertThat(testMensajeMuro.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testMensajeMuro.getTags()).isEqualTo(UPDATED_TAGS);
    }

    @Test
    @Transactional
    void putNonExistingMensajeMuro() throws Exception {
        int databaseSizeBeforeUpdate = mensajeMuroRepository.findAll().size();
        mensajeMuro.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMensajeMuroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mensajeMuro.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mensajeMuro))
            )
            .andExpect(status().isBadRequest());

        // Validate the MensajeMuro in the database
        List<MensajeMuro> mensajeMuroList = mensajeMuroRepository.findAll();
        assertThat(mensajeMuroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMensajeMuro() throws Exception {
        int databaseSizeBeforeUpdate = mensajeMuroRepository.findAll().size();
        mensajeMuro.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensajeMuroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mensajeMuro))
            )
            .andExpect(status().isBadRequest());

        // Validate the MensajeMuro in the database
        List<MensajeMuro> mensajeMuroList = mensajeMuroRepository.findAll();
        assertThat(mensajeMuroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMensajeMuro() throws Exception {
        int databaseSizeBeforeUpdate = mensajeMuroRepository.findAll().size();
        mensajeMuro.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensajeMuroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensajeMuro)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MensajeMuro in the database
        List<MensajeMuro> mensajeMuroList = mensajeMuroRepository.findAll();
        assertThat(mensajeMuroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMensajeMuroWithPatch() throws Exception {
        // Initialize the database
        mensajeMuroRepository.saveAndFlush(mensajeMuro);

        int databaseSizeBeforeUpdate = mensajeMuroRepository.findAll().size();

        // Update the mensajeMuro using partial update
        MensajeMuro partialUpdatedMensajeMuro = new MensajeMuro();
        partialUpdatedMensajeMuro.setId(mensajeMuro.getId());

        partialUpdatedMensajeMuro.tags(UPDATED_TAGS);

        restMensajeMuroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMensajeMuro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMensajeMuro))
            )
            .andExpect(status().isOk());

        // Validate the MensajeMuro in the database
        List<MensajeMuro> mensajeMuroList = mensajeMuroRepository.findAll();
        assertThat(mensajeMuroList).hasSize(databaseSizeBeforeUpdate);
        MensajeMuro testMensajeMuro = mensajeMuroList.get(mensajeMuroList.size() - 1);
        assertThat(testMensajeMuro.getMensaje()).isEqualTo(DEFAULT_MENSAJE);
        assertThat(testMensajeMuro.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testMensajeMuro.getTags()).isEqualTo(UPDATED_TAGS);
    }

    @Test
    @Transactional
    void fullUpdateMensajeMuroWithPatch() throws Exception {
        // Initialize the database
        mensajeMuroRepository.saveAndFlush(mensajeMuro);

        int databaseSizeBeforeUpdate = mensajeMuroRepository.findAll().size();

        // Update the mensajeMuro using partial update
        MensajeMuro partialUpdatedMensajeMuro = new MensajeMuro();
        partialUpdatedMensajeMuro.setId(mensajeMuro.getId());

        partialUpdatedMensajeMuro.mensaje(UPDATED_MENSAJE).fecha(UPDATED_FECHA).tags(UPDATED_TAGS);

        restMensajeMuroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMensajeMuro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMensajeMuro))
            )
            .andExpect(status().isOk());

        // Validate the MensajeMuro in the database
        List<MensajeMuro> mensajeMuroList = mensajeMuroRepository.findAll();
        assertThat(mensajeMuroList).hasSize(databaseSizeBeforeUpdate);
        MensajeMuro testMensajeMuro = mensajeMuroList.get(mensajeMuroList.size() - 1);
        assertThat(testMensajeMuro.getMensaje()).isEqualTo(UPDATED_MENSAJE);
        assertThat(testMensajeMuro.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testMensajeMuro.getTags()).isEqualTo(UPDATED_TAGS);
    }

    @Test
    @Transactional
    void patchNonExistingMensajeMuro() throws Exception {
        int databaseSizeBeforeUpdate = mensajeMuroRepository.findAll().size();
        mensajeMuro.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMensajeMuroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mensajeMuro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mensajeMuro))
            )
            .andExpect(status().isBadRequest());

        // Validate the MensajeMuro in the database
        List<MensajeMuro> mensajeMuroList = mensajeMuroRepository.findAll();
        assertThat(mensajeMuroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMensajeMuro() throws Exception {
        int databaseSizeBeforeUpdate = mensajeMuroRepository.findAll().size();
        mensajeMuro.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensajeMuroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mensajeMuro))
            )
            .andExpect(status().isBadRequest());

        // Validate the MensajeMuro in the database
        List<MensajeMuro> mensajeMuroList = mensajeMuroRepository.findAll();
        assertThat(mensajeMuroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMensajeMuro() throws Exception {
        int databaseSizeBeforeUpdate = mensajeMuroRepository.findAll().size();
        mensajeMuro.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensajeMuroMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mensajeMuro))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MensajeMuro in the database
        List<MensajeMuro> mensajeMuroList = mensajeMuroRepository.findAll();
        assertThat(mensajeMuroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMensajeMuro() throws Exception {
        // Initialize the database
        mensajeMuroRepository.saveAndFlush(mensajeMuro);

        int databaseSizeBeforeDelete = mensajeMuroRepository.findAll().size();

        // Delete the mensajeMuro
        restMensajeMuroMockMvc
            .perform(delete(ENTITY_API_URL_ID, mensajeMuro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MensajeMuro> mensajeMuroList = mensajeMuroRepository.findAll();
        assertThat(mensajeMuroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
