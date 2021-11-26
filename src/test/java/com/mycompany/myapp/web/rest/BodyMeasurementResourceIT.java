package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BodyMeasurement;
import com.mycompany.myapp.repository.BodyMeasurementRepository;
import com.mycompany.myapp.repository.search.BodyMeasurementSearchRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BodyMeasurementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BodyMeasurementResourceIT {

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final ZonedDateTime DEFAULT_BODY_MEASUREMENT_DATE_TIME = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_BODY_MEASUREMENT_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/body-measurements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/body-measurements";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BodyMeasurementRepository bodyMeasurementRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.BodyMeasurementSearchRepositoryMockConfiguration
     */
    @Autowired
    private BodyMeasurementSearchRepository mockBodyMeasurementSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBodyMeasurementMockMvc;

    private BodyMeasurement bodyMeasurement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BodyMeasurement createEntity(EntityManager em) {
        BodyMeasurement bodyMeasurement = new BodyMeasurement()
            .value(DEFAULT_VALUE)
            .bodyMeasurementDateTime(DEFAULT_BODY_MEASUREMENT_DATE_TIME);
        return bodyMeasurement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BodyMeasurement createUpdatedEntity(EntityManager em) {
        BodyMeasurement bodyMeasurement = new BodyMeasurement()
            .value(UPDATED_VALUE)
            .bodyMeasurementDateTime(UPDATED_BODY_MEASUREMENT_DATE_TIME);
        return bodyMeasurement;
    }

    @BeforeEach
    public void initTest() {
        bodyMeasurement = createEntity(em);
    }

    @Test
    @Transactional
    void createBodyMeasurement() throws Exception {
        int databaseSizeBeforeCreate = bodyMeasurementRepository.findAll().size();
        // Create the BodyMeasurement
        restBodyMeasurementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bodyMeasurement))
            )
            .andExpect(status().isCreated());

        // Validate the BodyMeasurement in the database
        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findAll();
        assertThat(bodyMeasurementList).hasSize(databaseSizeBeforeCreate + 1);
        BodyMeasurement testBodyMeasurement = bodyMeasurementList.get(bodyMeasurementList.size() - 1);
        assertThat(testBodyMeasurement.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testBodyMeasurement.getBodyMeasurementDateTime()).isEqualTo(DEFAULT_BODY_MEASUREMENT_DATE_TIME);

        // Validate the BodyMeasurement in Elasticsearch
        verify(mockBodyMeasurementSearchRepository, times(1)).save(testBodyMeasurement);
    }

    @Test
    @Transactional
    void createBodyMeasurementWithExistingId() throws Exception {
        // Create the BodyMeasurement with an existing ID
        bodyMeasurement.setId(1L);

        int databaseSizeBeforeCreate = bodyMeasurementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBodyMeasurementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bodyMeasurement))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyMeasurement in the database
        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findAll();
        assertThat(bodyMeasurementList).hasSize(databaseSizeBeforeCreate);

        // Validate the BodyMeasurement in Elasticsearch
        verify(mockBodyMeasurementSearchRepository, times(0)).save(bodyMeasurement);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = bodyMeasurementRepository.findAll().size();
        // set the field null
        bodyMeasurement.setValue(null);

        // Create the BodyMeasurement, which fails.

        restBodyMeasurementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bodyMeasurement))
            )
            .andExpect(status().isBadRequest());

        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findAll();
        assertThat(bodyMeasurementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBodyMeasurements() throws Exception {
        // Initialize the database
        bodyMeasurementRepository.saveAndFlush(bodyMeasurement);

        // Get all the bodyMeasurementList
        restBodyMeasurementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bodyMeasurement.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].bodyMeasurementDateTime").value(hasItem(sameInstant(DEFAULT_BODY_MEASUREMENT_DATE_TIME))));
    }

    @Test
    @Transactional
    void getBodyMeasurement() throws Exception {
        // Initialize the database
        bodyMeasurementRepository.saveAndFlush(bodyMeasurement);

        // Get the bodyMeasurement
        restBodyMeasurementMockMvc
            .perform(get(ENTITY_API_URL_ID, bodyMeasurement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bodyMeasurement.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.bodyMeasurementDateTime").value(sameInstant(DEFAULT_BODY_MEASUREMENT_DATE_TIME)));
    }

    @Test
    @Transactional
    void getNonExistingBodyMeasurement() throws Exception {
        // Get the bodyMeasurement
        restBodyMeasurementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBodyMeasurement() throws Exception {
        // Initialize the database
        bodyMeasurementRepository.saveAndFlush(bodyMeasurement);

        int databaseSizeBeforeUpdate = bodyMeasurementRepository.findAll().size();

        // Update the bodyMeasurement
        BodyMeasurement updatedBodyMeasurement = bodyMeasurementRepository.findById(bodyMeasurement.getId()).get();
        // Disconnect from session so that the updates on updatedBodyMeasurement are not directly saved in db
        em.detach(updatedBodyMeasurement);
        updatedBodyMeasurement.value(UPDATED_VALUE).bodyMeasurementDateTime(UPDATED_BODY_MEASUREMENT_DATE_TIME);

        restBodyMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBodyMeasurement.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBodyMeasurement))
            )
            .andExpect(status().isOk());

        // Validate the BodyMeasurement in the database
        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findAll();
        assertThat(bodyMeasurementList).hasSize(databaseSizeBeforeUpdate);
        BodyMeasurement testBodyMeasurement = bodyMeasurementList.get(bodyMeasurementList.size() - 1);
        assertThat(testBodyMeasurement.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testBodyMeasurement.getBodyMeasurementDateTime()).isEqualTo(UPDATED_BODY_MEASUREMENT_DATE_TIME);

        // Validate the BodyMeasurement in Elasticsearch
        verify(mockBodyMeasurementSearchRepository).save(testBodyMeasurement);
    }

    @Test
    @Transactional
    void putNonExistingBodyMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = bodyMeasurementRepository.findAll().size();
        bodyMeasurement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodyMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bodyMeasurement.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bodyMeasurement))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyMeasurement in the database
        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findAll();
        assertThat(bodyMeasurementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BodyMeasurement in Elasticsearch
        verify(mockBodyMeasurementSearchRepository, times(0)).save(bodyMeasurement);
    }

    @Test
    @Transactional
    void putWithIdMismatchBodyMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = bodyMeasurementRepository.findAll().size();
        bodyMeasurement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bodyMeasurement))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyMeasurement in the database
        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findAll();
        assertThat(bodyMeasurementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BodyMeasurement in Elasticsearch
        verify(mockBodyMeasurementSearchRepository, times(0)).save(bodyMeasurement);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBodyMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = bodyMeasurementRepository.findAll().size();
        bodyMeasurement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bodyMeasurement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BodyMeasurement in the database
        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findAll();
        assertThat(bodyMeasurementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BodyMeasurement in Elasticsearch
        verify(mockBodyMeasurementSearchRepository, times(0)).save(bodyMeasurement);
    }

    @Test
    @Transactional
    void partialUpdateBodyMeasurementWithPatch() throws Exception {
        // Initialize the database
        bodyMeasurementRepository.saveAndFlush(bodyMeasurement);

        int databaseSizeBeforeUpdate = bodyMeasurementRepository.findAll().size();

        // Update the bodyMeasurement using partial update
        BodyMeasurement partialUpdatedBodyMeasurement = new BodyMeasurement();
        partialUpdatedBodyMeasurement.setId(bodyMeasurement.getId());

        partialUpdatedBodyMeasurement.bodyMeasurementDateTime(UPDATED_BODY_MEASUREMENT_DATE_TIME);

        restBodyMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBodyMeasurement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBodyMeasurement))
            )
            .andExpect(status().isOk());

        // Validate the BodyMeasurement in the database
        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findAll();
        assertThat(bodyMeasurementList).hasSize(databaseSizeBeforeUpdate);
        BodyMeasurement testBodyMeasurement = bodyMeasurementList.get(bodyMeasurementList.size() - 1);
        assertThat(testBodyMeasurement.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testBodyMeasurement.getBodyMeasurementDateTime()).isEqualTo(UPDATED_BODY_MEASUREMENT_DATE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateBodyMeasurementWithPatch() throws Exception {
        // Initialize the database
        bodyMeasurementRepository.saveAndFlush(bodyMeasurement);

        int databaseSizeBeforeUpdate = bodyMeasurementRepository.findAll().size();

        // Update the bodyMeasurement using partial update
        BodyMeasurement partialUpdatedBodyMeasurement = new BodyMeasurement();
        partialUpdatedBodyMeasurement.setId(bodyMeasurement.getId());

        partialUpdatedBodyMeasurement.value(UPDATED_VALUE).bodyMeasurementDateTime(UPDATED_BODY_MEASUREMENT_DATE_TIME);

        restBodyMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBodyMeasurement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBodyMeasurement))
            )
            .andExpect(status().isOk());

        // Validate the BodyMeasurement in the database
        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findAll();
        assertThat(bodyMeasurementList).hasSize(databaseSizeBeforeUpdate);
        BodyMeasurement testBodyMeasurement = bodyMeasurementList.get(bodyMeasurementList.size() - 1);
        assertThat(testBodyMeasurement.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testBodyMeasurement.getBodyMeasurementDateTime()).isEqualTo(UPDATED_BODY_MEASUREMENT_DATE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingBodyMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = bodyMeasurementRepository.findAll().size();
        bodyMeasurement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodyMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bodyMeasurement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bodyMeasurement))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyMeasurement in the database
        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findAll();
        assertThat(bodyMeasurementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BodyMeasurement in Elasticsearch
        verify(mockBodyMeasurementSearchRepository, times(0)).save(bodyMeasurement);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBodyMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = bodyMeasurementRepository.findAll().size();
        bodyMeasurement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bodyMeasurement))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyMeasurement in the database
        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findAll();
        assertThat(bodyMeasurementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BodyMeasurement in Elasticsearch
        verify(mockBodyMeasurementSearchRepository, times(0)).save(bodyMeasurement);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBodyMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = bodyMeasurementRepository.findAll().size();
        bodyMeasurement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bodyMeasurement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BodyMeasurement in the database
        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findAll();
        assertThat(bodyMeasurementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BodyMeasurement in Elasticsearch
        verify(mockBodyMeasurementSearchRepository, times(0)).save(bodyMeasurement);
    }

    @Test
    @Transactional
    void deleteBodyMeasurement() throws Exception {
        // Initialize the database
        bodyMeasurementRepository.saveAndFlush(bodyMeasurement);

        int databaseSizeBeforeDelete = bodyMeasurementRepository.findAll().size();

        // Delete the bodyMeasurement
        restBodyMeasurementMockMvc
            .perform(delete(ENTITY_API_URL_ID, bodyMeasurement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BodyMeasurement> bodyMeasurementList = bodyMeasurementRepository.findAll();
        assertThat(bodyMeasurementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BodyMeasurement in Elasticsearch
        verify(mockBodyMeasurementSearchRepository, times(1)).deleteById(bodyMeasurement.getId());
    }

    @Test
    @Transactional
    void searchBodyMeasurement() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        bodyMeasurementRepository.saveAndFlush(bodyMeasurement);
        when(mockBodyMeasurementSearchRepository.search("id:" + bodyMeasurement.getId())).thenReturn(Stream.of(bodyMeasurement));

        // Search the bodyMeasurement
        restBodyMeasurementMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + bodyMeasurement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bodyMeasurement.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].bodyMeasurementDateTime").value(hasItem(sameInstant(DEFAULT_BODY_MEASUREMENT_DATE_TIME))));
    }
}
