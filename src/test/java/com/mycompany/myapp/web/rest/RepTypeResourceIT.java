package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.RepType;
import com.mycompany.myapp.repository.RepTypeRepository;
import com.mycompany.myapp.repository.search.RepTypeSearchRepository;
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
 * Integration tests for the {@link RepTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RepTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rep-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rep-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RepTypeRepository repTypeRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.RepTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private RepTypeSearchRepository mockRepTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRepTypeMockMvc;

    private RepType repType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RepType createEntity(EntityManager em) {
        RepType repType = new RepType().name(DEFAULT_NAME).display(DEFAULT_DISPLAY);
        return repType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RepType createUpdatedEntity(EntityManager em) {
        RepType repType = new RepType().name(UPDATED_NAME).display(UPDATED_DISPLAY);
        return repType;
    }

    @BeforeEach
    public void initTest() {
        repType = createEntity(em);
    }

    @Test
    @Transactional
    void createRepType() throws Exception {
        int databaseSizeBeforeCreate = repTypeRepository.findAll().size();
        // Create the RepType
        restRepTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repType))
            )
            .andExpect(status().isCreated());

        // Validate the RepType in the database
        List<RepType> repTypeList = repTypeRepository.findAll();
        assertThat(repTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RepType testRepType = repTypeList.get(repTypeList.size() - 1);
        assertThat(testRepType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRepType.getDisplay()).isEqualTo(DEFAULT_DISPLAY);

        // Validate the RepType in Elasticsearch
        verify(mockRepTypeSearchRepository, times(1)).save(testRepType);
    }

    @Test
    @Transactional
    void createRepTypeWithExistingId() throws Exception {
        // Create the RepType with an existing ID
        repType.setId(1L);

        int databaseSizeBeforeCreate = repTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RepType in the database
        List<RepType> repTypeList = repTypeRepository.findAll();
        assertThat(repTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the RepType in Elasticsearch
        verify(mockRepTypeSearchRepository, times(0)).save(repType);
    }

    @Test
    @Transactional
    void getAllRepTypes() throws Exception {
        // Initialize the database
        repTypeRepository.saveAndFlush(repType);

        // Get all the repTypeList
        restRepTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].display").value(hasItem(DEFAULT_DISPLAY)));
    }

    @Test
    @Transactional
    void getRepType() throws Exception {
        // Initialize the database
        repTypeRepository.saveAndFlush(repType);

        // Get the repType
        restRepTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, repType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(repType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.display").value(DEFAULT_DISPLAY));
    }

    @Test
    @Transactional
    void getNonExistingRepType() throws Exception {
        // Get the repType
        restRepTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRepType() throws Exception {
        // Initialize the database
        repTypeRepository.saveAndFlush(repType);

        int databaseSizeBeforeUpdate = repTypeRepository.findAll().size();

        // Update the repType
        RepType updatedRepType = repTypeRepository.findById(repType.getId()).get();
        // Disconnect from session so that the updates on updatedRepType are not directly saved in db
        em.detach(updatedRepType);
        updatedRepType.name(UPDATED_NAME).display(UPDATED_DISPLAY);

        restRepTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRepType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRepType))
            )
            .andExpect(status().isOk());

        // Validate the RepType in the database
        List<RepType> repTypeList = repTypeRepository.findAll();
        assertThat(repTypeList).hasSize(databaseSizeBeforeUpdate);
        RepType testRepType = repTypeList.get(repTypeList.size() - 1);
        assertThat(testRepType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRepType.getDisplay()).isEqualTo(UPDATED_DISPLAY);

        // Validate the RepType in Elasticsearch
        verify(mockRepTypeSearchRepository).save(testRepType);
    }

    @Test
    @Transactional
    void putNonExistingRepType() throws Exception {
        int databaseSizeBeforeUpdate = repTypeRepository.findAll().size();
        repType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, repType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RepType in the database
        List<RepType> repTypeList = repTypeRepository.findAll();
        assertThat(repTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RepType in Elasticsearch
        verify(mockRepTypeSearchRepository, times(0)).save(repType);
    }

    @Test
    @Transactional
    void putWithIdMismatchRepType() throws Exception {
        int databaseSizeBeforeUpdate = repTypeRepository.findAll().size();
        repType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RepType in the database
        List<RepType> repTypeList = repTypeRepository.findAll();
        assertThat(repTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RepType in Elasticsearch
        verify(mockRepTypeSearchRepository, times(0)).save(repType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRepType() throws Exception {
        int databaseSizeBeforeUpdate = repTypeRepository.findAll().size();
        repType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepTypeMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RepType in the database
        List<RepType> repTypeList = repTypeRepository.findAll();
        assertThat(repTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RepType in Elasticsearch
        verify(mockRepTypeSearchRepository, times(0)).save(repType);
    }

    @Test
    @Transactional
    void partialUpdateRepTypeWithPatch() throws Exception {
        // Initialize the database
        repTypeRepository.saveAndFlush(repType);

        int databaseSizeBeforeUpdate = repTypeRepository.findAll().size();

        // Update the repType using partial update
        RepType partialUpdatedRepType = new RepType();
        partialUpdatedRepType.setId(repType.getId());

        restRepTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepType))
            )
            .andExpect(status().isOk());

        // Validate the RepType in the database
        List<RepType> repTypeList = repTypeRepository.findAll();
        assertThat(repTypeList).hasSize(databaseSizeBeforeUpdate);
        RepType testRepType = repTypeList.get(repTypeList.size() - 1);
        assertThat(testRepType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRepType.getDisplay()).isEqualTo(DEFAULT_DISPLAY);
    }

    @Test
    @Transactional
    void fullUpdateRepTypeWithPatch() throws Exception {
        // Initialize the database
        repTypeRepository.saveAndFlush(repType);

        int databaseSizeBeforeUpdate = repTypeRepository.findAll().size();

        // Update the repType using partial update
        RepType partialUpdatedRepType = new RepType();
        partialUpdatedRepType.setId(repType.getId());

        partialUpdatedRepType.name(UPDATED_NAME).display(UPDATED_DISPLAY);

        restRepTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepType))
            )
            .andExpect(status().isOk());

        // Validate the RepType in the database
        List<RepType> repTypeList = repTypeRepository.findAll();
        assertThat(repTypeList).hasSize(databaseSizeBeforeUpdate);
        RepType testRepType = repTypeList.get(repTypeList.size() - 1);
        assertThat(testRepType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRepType.getDisplay()).isEqualTo(UPDATED_DISPLAY);
    }

    @Test
    @Transactional
    void patchNonExistingRepType() throws Exception {
        int databaseSizeBeforeUpdate = repTypeRepository.findAll().size();
        repType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, repType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RepType in the database
        List<RepType> repTypeList = repTypeRepository.findAll();
        assertThat(repTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RepType in Elasticsearch
        verify(mockRepTypeSearchRepository, times(0)).save(repType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRepType() throws Exception {
        int databaseSizeBeforeUpdate = repTypeRepository.findAll().size();
        repType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RepType in the database
        List<RepType> repTypeList = repTypeRepository.findAll();
        assertThat(repTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RepType in Elasticsearch
        verify(mockRepTypeSearchRepository, times(0)).save(repType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRepType() throws Exception {
        int databaseSizeBeforeUpdate = repTypeRepository.findAll().size();
        repType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RepType in the database
        List<RepType> repTypeList = repTypeRepository.findAll();
        assertThat(repTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RepType in Elasticsearch
        verify(mockRepTypeSearchRepository, times(0)).save(repType);
    }

    @Test
    @Transactional
    void deleteRepType() throws Exception {
        // Initialize the database
        repTypeRepository.saveAndFlush(repType);

        int databaseSizeBeforeDelete = repTypeRepository.findAll().size();

        // Delete the repType
        restRepTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, repType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RepType> repTypeList = repTypeRepository.findAll();
        assertThat(repTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RepType in Elasticsearch
        verify(mockRepTypeSearchRepository, times(1)).deleteById(repType.getId());
    }

    @Test
    @Transactional
    void searchRepType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        repTypeRepository.saveAndFlush(repType);
        when(mockRepTypeSearchRepository.search("id:" + repType.getId())).thenReturn(Stream.of(repType));

        // Search the repType
        restRepTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + repType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].display").value(hasItem(DEFAULT_DISPLAY)));
    }
}
