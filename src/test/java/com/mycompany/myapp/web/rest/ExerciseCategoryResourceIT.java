package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ExerciseCategory;
import com.mycompany.myapp.repository.ExerciseCategoryRepository;
import com.mycompany.myapp.repository.search.ExerciseCategorySearchRepository;
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
 * Integration tests for the {@link ExerciseCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExerciseCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CATEGORY_ORDER = 1;
    private static final Integer UPDATED_CATEGORY_ORDER = 2;

    private static final String ENTITY_API_URL = "/api/exercise-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/exercise-categories";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExerciseCategoryRepository exerciseCategoryRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.ExerciseCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private ExerciseCategorySearchRepository mockExerciseCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExerciseCategoryMockMvc;

    private ExerciseCategory exerciseCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseCategory createEntity(EntityManager em) {
        ExerciseCategory exerciseCategory = new ExerciseCategory().name(DEFAULT_NAME).categoryOrder(DEFAULT_CATEGORY_ORDER);
        return exerciseCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseCategory createUpdatedEntity(EntityManager em) {
        ExerciseCategory exerciseCategory = new ExerciseCategory().name(UPDATED_NAME).categoryOrder(UPDATED_CATEGORY_ORDER);
        return exerciseCategory;
    }

    @BeforeEach
    public void initTest() {
        exerciseCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createExerciseCategory() throws Exception {
        int databaseSizeBeforeCreate = exerciseCategoryRepository.findAll().size();
        // Create the ExerciseCategory
        restExerciseCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseCategory))
            )
            .andExpect(status().isCreated());

        // Validate the ExerciseCategory in the database
        List<ExerciseCategory> exerciseCategoryList = exerciseCategoryRepository.findAll();
        assertThat(exerciseCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ExerciseCategory testExerciseCategory = exerciseCategoryList.get(exerciseCategoryList.size() - 1);
        assertThat(testExerciseCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExerciseCategory.getCategoryOrder()).isEqualTo(DEFAULT_CATEGORY_ORDER);

        // Validate the ExerciseCategory in Elasticsearch
        verify(mockExerciseCategorySearchRepository, times(1)).save(testExerciseCategory);
    }

    @Test
    @Transactional
    void createExerciseCategoryWithExistingId() throws Exception {
        // Create the ExerciseCategory with an existing ID
        exerciseCategory.setId(1L);

        int databaseSizeBeforeCreate = exerciseCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseCategory in the database
        List<ExerciseCategory> exerciseCategoryList = exerciseCategoryRepository.findAll();
        assertThat(exerciseCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the ExerciseCategory in Elasticsearch
        verify(mockExerciseCategorySearchRepository, times(0)).save(exerciseCategory);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseCategoryRepository.findAll().size();
        // set the field null
        exerciseCategory.setName(null);

        // Create the ExerciseCategory, which fails.

        restExerciseCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseCategory))
            )
            .andExpect(status().isBadRequest());

        List<ExerciseCategory> exerciseCategoryList = exerciseCategoryRepository.findAll();
        assertThat(exerciseCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExerciseCategories() throws Exception {
        // Initialize the database
        exerciseCategoryRepository.saveAndFlush(exerciseCategory);

        // Get all the exerciseCategoryList
        restExerciseCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].categoryOrder").value(hasItem(DEFAULT_CATEGORY_ORDER)));
    }

    @Test
    @Transactional
    void getExerciseCategory() throws Exception {
        // Initialize the database
        exerciseCategoryRepository.saveAndFlush(exerciseCategory);

        // Get the exerciseCategory
        restExerciseCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, exerciseCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exerciseCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.categoryOrder").value(DEFAULT_CATEGORY_ORDER));
    }

    @Test
    @Transactional
    void getNonExistingExerciseCategory() throws Exception {
        // Get the exerciseCategory
        restExerciseCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExerciseCategory() throws Exception {
        // Initialize the database
        exerciseCategoryRepository.saveAndFlush(exerciseCategory);

        int databaseSizeBeforeUpdate = exerciseCategoryRepository.findAll().size();

        // Update the exerciseCategory
        ExerciseCategory updatedExerciseCategory = exerciseCategoryRepository.findById(exerciseCategory.getId()).get();
        // Disconnect from session so that the updates on updatedExerciseCategory are not directly saved in db
        em.detach(updatedExerciseCategory);
        updatedExerciseCategory.name(UPDATED_NAME).categoryOrder(UPDATED_CATEGORY_ORDER);

        restExerciseCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExerciseCategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExerciseCategory))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseCategory in the database
        List<ExerciseCategory> exerciseCategoryList = exerciseCategoryRepository.findAll();
        assertThat(exerciseCategoryList).hasSize(databaseSizeBeforeUpdate);
        ExerciseCategory testExerciseCategory = exerciseCategoryList.get(exerciseCategoryList.size() - 1);
        assertThat(testExerciseCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExerciseCategory.getCategoryOrder()).isEqualTo(UPDATED_CATEGORY_ORDER);

        // Validate the ExerciseCategory in Elasticsearch
        verify(mockExerciseCategorySearchRepository).save(testExerciseCategory);
    }

    @Test
    @Transactional
    void putNonExistingExerciseCategory() throws Exception {
        int databaseSizeBeforeUpdate = exerciseCategoryRepository.findAll().size();
        exerciseCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseCategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseCategory in the database
        List<ExerciseCategory> exerciseCategoryList = exerciseCategoryRepository.findAll();
        assertThat(exerciseCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExerciseCategory in Elasticsearch
        verify(mockExerciseCategorySearchRepository, times(0)).save(exerciseCategory);
    }

    @Test
    @Transactional
    void putWithIdMismatchExerciseCategory() throws Exception {
        int databaseSizeBeforeUpdate = exerciseCategoryRepository.findAll().size();
        exerciseCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseCategory in the database
        List<ExerciseCategory> exerciseCategoryList = exerciseCategoryRepository.findAll();
        assertThat(exerciseCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExerciseCategory in Elasticsearch
        verify(mockExerciseCategorySearchRepository, times(0)).save(exerciseCategory);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExerciseCategory() throws Exception {
        int databaseSizeBeforeUpdate = exerciseCategoryRepository.findAll().size();
        exerciseCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseCategory in the database
        List<ExerciseCategory> exerciseCategoryList = exerciseCategoryRepository.findAll();
        assertThat(exerciseCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExerciseCategory in Elasticsearch
        verify(mockExerciseCategorySearchRepository, times(0)).save(exerciseCategory);
    }

    @Test
    @Transactional
    void partialUpdateExerciseCategoryWithPatch() throws Exception {
        // Initialize the database
        exerciseCategoryRepository.saveAndFlush(exerciseCategory);

        int databaseSizeBeforeUpdate = exerciseCategoryRepository.findAll().size();

        // Update the exerciseCategory using partial update
        ExerciseCategory partialUpdatedExerciseCategory = new ExerciseCategory();
        partialUpdatedExerciseCategory.setId(exerciseCategory.getId());

        partialUpdatedExerciseCategory.name(UPDATED_NAME).categoryOrder(UPDATED_CATEGORY_ORDER);

        restExerciseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseCategory))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseCategory in the database
        List<ExerciseCategory> exerciseCategoryList = exerciseCategoryRepository.findAll();
        assertThat(exerciseCategoryList).hasSize(databaseSizeBeforeUpdate);
        ExerciseCategory testExerciseCategory = exerciseCategoryList.get(exerciseCategoryList.size() - 1);
        assertThat(testExerciseCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExerciseCategory.getCategoryOrder()).isEqualTo(UPDATED_CATEGORY_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateExerciseCategoryWithPatch() throws Exception {
        // Initialize the database
        exerciseCategoryRepository.saveAndFlush(exerciseCategory);

        int databaseSizeBeforeUpdate = exerciseCategoryRepository.findAll().size();

        // Update the exerciseCategory using partial update
        ExerciseCategory partialUpdatedExerciseCategory = new ExerciseCategory();
        partialUpdatedExerciseCategory.setId(exerciseCategory.getId());

        partialUpdatedExerciseCategory.name(UPDATED_NAME).categoryOrder(UPDATED_CATEGORY_ORDER);

        restExerciseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseCategory))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseCategory in the database
        List<ExerciseCategory> exerciseCategoryList = exerciseCategoryRepository.findAll();
        assertThat(exerciseCategoryList).hasSize(databaseSizeBeforeUpdate);
        ExerciseCategory testExerciseCategory = exerciseCategoryList.get(exerciseCategoryList.size() - 1);
        assertThat(testExerciseCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExerciseCategory.getCategoryOrder()).isEqualTo(UPDATED_CATEGORY_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingExerciseCategory() throws Exception {
        int databaseSizeBeforeUpdate = exerciseCategoryRepository.findAll().size();
        exerciseCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exerciseCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseCategory in the database
        List<ExerciseCategory> exerciseCategoryList = exerciseCategoryRepository.findAll();
        assertThat(exerciseCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExerciseCategory in Elasticsearch
        verify(mockExerciseCategorySearchRepository, times(0)).save(exerciseCategory);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExerciseCategory() throws Exception {
        int databaseSizeBeforeUpdate = exerciseCategoryRepository.findAll().size();
        exerciseCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseCategory in the database
        List<ExerciseCategory> exerciseCategoryList = exerciseCategoryRepository.findAll();
        assertThat(exerciseCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExerciseCategory in Elasticsearch
        verify(mockExerciseCategorySearchRepository, times(0)).save(exerciseCategory);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExerciseCategory() throws Exception {
        int databaseSizeBeforeUpdate = exerciseCategoryRepository.findAll().size();
        exerciseCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseCategory in the database
        List<ExerciseCategory> exerciseCategoryList = exerciseCategoryRepository.findAll();
        assertThat(exerciseCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExerciseCategory in Elasticsearch
        verify(mockExerciseCategorySearchRepository, times(0)).save(exerciseCategory);
    }

    @Test
    @Transactional
    void deleteExerciseCategory() throws Exception {
        // Initialize the database
        exerciseCategoryRepository.saveAndFlush(exerciseCategory);

        int databaseSizeBeforeDelete = exerciseCategoryRepository.findAll().size();

        // Delete the exerciseCategory
        restExerciseCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, exerciseCategory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExerciseCategory> exerciseCategoryList = exerciseCategoryRepository.findAll();
        assertThat(exerciseCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ExerciseCategory in Elasticsearch
        verify(mockExerciseCategorySearchRepository, times(1)).deleteById(exerciseCategory.getId());
    }

    @Test
    @Transactional
    void searchExerciseCategory() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        exerciseCategoryRepository.saveAndFlush(exerciseCategory);
        when(mockExerciseCategorySearchRepository.search("id:" + exerciseCategory.getId())).thenReturn(Stream.of(exerciseCategory));

        // Search the exerciseCategory
        restExerciseCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + exerciseCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].categoryOrder").value(hasItem(DEFAULT_CATEGORY_ORDER)));
    }
}
