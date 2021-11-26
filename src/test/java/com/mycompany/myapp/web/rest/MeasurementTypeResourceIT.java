package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MeasurementType;
import com.mycompany.myapp.repository.MeasurementTypeRepository;
import com.mycompany.myapp.repository.search.MeasurementTypeSearchRepository;
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
 * Integration tests for the {@link MeasurementTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MeasurementTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MEASUREMENT_ORDER = 1;
    private static final Integer UPDATED_MEASUREMENT_ORDER = 2;

    private static final String DEFAULT_MEASUREMENT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_MEASUREMENT_UNIT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/measurement-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/measurement-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MeasurementTypeRepository measurementTypeRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.MeasurementTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private MeasurementTypeSearchRepository mockMeasurementTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeasurementTypeMockMvc;

    private MeasurementType measurementType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasurementType createEntity(EntityManager em) {
        MeasurementType measurementType = new MeasurementType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .measurementOrder(DEFAULT_MEASUREMENT_ORDER)
            .measurementUnit(DEFAULT_MEASUREMENT_UNIT);
        return measurementType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasurementType createUpdatedEntity(EntityManager em) {
        MeasurementType measurementType = new MeasurementType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .measurementOrder(UPDATED_MEASUREMENT_ORDER)
            .measurementUnit(UPDATED_MEASUREMENT_UNIT);
        return measurementType;
    }

    @BeforeEach
    public void initTest() {
        measurementType = createEntity(em);
    }

    @Test
    @Transactional
    void createMeasurementType() throws Exception {
        int databaseSizeBeforeCreate = measurementTypeRepository.findAll().size();
        // Create the MeasurementType
        restMeasurementTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementType))
            )
            .andExpect(status().isCreated());

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeCreate + 1);
        MeasurementType testMeasurementType = measurementTypeList.get(measurementTypeList.size() - 1);
        assertThat(testMeasurementType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMeasurementType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMeasurementType.getMeasurementOrder()).isEqualTo(DEFAULT_MEASUREMENT_ORDER);
        assertThat(testMeasurementType.getMeasurementUnit()).isEqualTo(DEFAULT_MEASUREMENT_UNIT);

        // Validate the MeasurementType in Elasticsearch
        verify(mockMeasurementTypeSearchRepository, times(1)).save(testMeasurementType);
    }

    @Test
    @Transactional
    void createMeasurementTypeWithExistingId() throws Exception {
        // Create the MeasurementType with an existing ID
        measurementType.setId(1L);

        int databaseSizeBeforeCreate = measurementTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasurementTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementType))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the MeasurementType in Elasticsearch
        verify(mockMeasurementTypeSearchRepository, times(0)).save(measurementType);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = measurementTypeRepository.findAll().size();
        // set the field null
        measurementType.setName(null);

        // Create the MeasurementType, which fails.

        restMeasurementTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementType))
            )
            .andExpect(status().isBadRequest());

        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMeasurementTypes() throws Exception {
        // Initialize the database
        measurementTypeRepository.saveAndFlush(measurementType);

        // Get all the measurementTypeList
        restMeasurementTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measurementType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].measurementOrder").value(hasItem(DEFAULT_MEASUREMENT_ORDER)))
            .andExpect(jsonPath("$.[*].measurementUnit").value(hasItem(DEFAULT_MEASUREMENT_UNIT)));
    }

    @Test
    @Transactional
    void getMeasurementType() throws Exception {
        // Initialize the database
        measurementTypeRepository.saveAndFlush(measurementType);

        // Get the measurementType
        restMeasurementTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, measurementType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(measurementType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.measurementOrder").value(DEFAULT_MEASUREMENT_ORDER))
            .andExpect(jsonPath("$.measurementUnit").value(DEFAULT_MEASUREMENT_UNIT));
    }

    @Test
    @Transactional
    void getNonExistingMeasurementType() throws Exception {
        // Get the measurementType
        restMeasurementTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMeasurementType() throws Exception {
        // Initialize the database
        measurementTypeRepository.saveAndFlush(measurementType);

        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().size();

        // Update the measurementType
        MeasurementType updatedMeasurementType = measurementTypeRepository.findById(measurementType.getId()).get();
        // Disconnect from session so that the updates on updatedMeasurementType are not directly saved in db
        em.detach(updatedMeasurementType);
        updatedMeasurementType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .measurementOrder(UPDATED_MEASUREMENT_ORDER)
            .measurementUnit(UPDATED_MEASUREMENT_UNIT);

        restMeasurementTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMeasurementType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMeasurementType))
            )
            .andExpect(status().isOk());

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);
        MeasurementType testMeasurementType = measurementTypeList.get(measurementTypeList.size() - 1);
        assertThat(testMeasurementType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeasurementType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMeasurementType.getMeasurementOrder()).isEqualTo(UPDATED_MEASUREMENT_ORDER);
        assertThat(testMeasurementType.getMeasurementUnit()).isEqualTo(UPDATED_MEASUREMENT_UNIT);

        // Validate the MeasurementType in Elasticsearch
        verify(mockMeasurementTypeSearchRepository).save(testMeasurementType);
    }

    @Test
    @Transactional
    void putNonExistingMeasurementType() throws Exception {
        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().size();
        measurementType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasurementTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, measurementType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementType))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MeasurementType in Elasticsearch
        verify(mockMeasurementTypeSearchRepository, times(0)).save(measurementType);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeasurementType() throws Exception {
        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().size();
        measurementType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasurementTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementType))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MeasurementType in Elasticsearch
        verify(mockMeasurementTypeSearchRepository, times(0)).save(measurementType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeasurementType() throws Exception {
        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().size();
        measurementType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasurementTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MeasurementType in Elasticsearch
        verify(mockMeasurementTypeSearchRepository, times(0)).save(measurementType);
    }

    @Test
    @Transactional
    void partialUpdateMeasurementTypeWithPatch() throws Exception {
        // Initialize the database
        measurementTypeRepository.saveAndFlush(measurementType);

        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().size();

        // Update the measurementType using partial update
        MeasurementType partialUpdatedMeasurementType = new MeasurementType();
        partialUpdatedMeasurementType.setId(measurementType.getId());

        partialUpdatedMeasurementType.name(UPDATED_NAME);

        restMeasurementTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeasurementType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeasurementType))
            )
            .andExpect(status().isOk());

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);
        MeasurementType testMeasurementType = measurementTypeList.get(measurementTypeList.size() - 1);
        assertThat(testMeasurementType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeasurementType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMeasurementType.getMeasurementOrder()).isEqualTo(DEFAULT_MEASUREMENT_ORDER);
        assertThat(testMeasurementType.getMeasurementUnit()).isEqualTo(DEFAULT_MEASUREMENT_UNIT);
    }

    @Test
    @Transactional
    void fullUpdateMeasurementTypeWithPatch() throws Exception {
        // Initialize the database
        measurementTypeRepository.saveAndFlush(measurementType);

        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().size();

        // Update the measurementType using partial update
        MeasurementType partialUpdatedMeasurementType = new MeasurementType();
        partialUpdatedMeasurementType.setId(measurementType.getId());

        partialUpdatedMeasurementType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .measurementOrder(UPDATED_MEASUREMENT_ORDER)
            .measurementUnit(UPDATED_MEASUREMENT_UNIT);

        restMeasurementTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeasurementType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeasurementType))
            )
            .andExpect(status().isOk());

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);
        MeasurementType testMeasurementType = measurementTypeList.get(measurementTypeList.size() - 1);
        assertThat(testMeasurementType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeasurementType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMeasurementType.getMeasurementOrder()).isEqualTo(UPDATED_MEASUREMENT_ORDER);
        assertThat(testMeasurementType.getMeasurementUnit()).isEqualTo(UPDATED_MEASUREMENT_UNIT);
    }

    @Test
    @Transactional
    void patchNonExistingMeasurementType() throws Exception {
        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().size();
        measurementType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasurementTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, measurementType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(measurementType))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MeasurementType in Elasticsearch
        verify(mockMeasurementTypeSearchRepository, times(0)).save(measurementType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeasurementType() throws Exception {
        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().size();
        measurementType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasurementTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(measurementType))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MeasurementType in Elasticsearch
        verify(mockMeasurementTypeSearchRepository, times(0)).save(measurementType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeasurementType() throws Exception {
        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().size();
        measurementType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasurementTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(measurementType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MeasurementType in Elasticsearch
        verify(mockMeasurementTypeSearchRepository, times(0)).save(measurementType);
    }

    @Test
    @Transactional
    void deleteMeasurementType() throws Exception {
        // Initialize the database
        measurementTypeRepository.saveAndFlush(measurementType);

        int databaseSizeBeforeDelete = measurementTypeRepository.findAll().size();

        // Delete the measurementType
        restMeasurementTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, measurementType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MeasurementType in Elasticsearch
        verify(mockMeasurementTypeSearchRepository, times(1)).deleteById(measurementType.getId());
    }

    @Test
    @Transactional
    void searchMeasurementType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        measurementTypeRepository.saveAndFlush(measurementType);
        when(mockMeasurementTypeSearchRepository.search("id:" + measurementType.getId())).thenReturn(Stream.of(measurementType));

        // Search the measurementType
        restMeasurementTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + measurementType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measurementType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].measurementOrder").value(hasItem(DEFAULT_MEASUREMENT_ORDER)))
            .andExpect(jsonPath("$.[*].measurementUnit").value(hasItem(DEFAULT_MEASUREMENT_UNIT)));
    }
}
