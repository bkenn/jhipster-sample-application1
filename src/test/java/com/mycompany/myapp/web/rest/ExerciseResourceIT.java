package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Exercise;
import com.mycompany.myapp.repository.ExerciseRepository;
import com.mycompany.myapp.repository.search.ExerciseSearchRepository;
import com.mycompany.myapp.service.ExerciseService;
import java.util.ArrayList;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ExerciseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExerciseResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/exercises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/exercises";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Mock
    private ExerciseRepository exerciseRepositoryMock;

    @Mock
    private ExerciseService exerciseServiceMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.ExerciseSearchRepositoryMockConfiguration
     */
    @Autowired
    private ExerciseSearchRepository mockExerciseSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExerciseMockMvc;

    private Exercise exercise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exercise createEntity(EntityManager em) {
        Exercise exercise = new Exercise().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return exercise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exercise createUpdatedEntity(EntityManager em) {
        Exercise exercise = new Exercise().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return exercise;
    }

    @BeforeEach
    public void initTest() {
        exercise = createEntity(em);
    }

    @Test
    @Transactional
    void createExercise() throws Exception {
        int databaseSizeBeforeCreate = exerciseRepository.findAll().size();
        // Create the Exercise
        restExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exercise))
            )
            .andExpect(status().isCreated());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeCreate + 1);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExercise.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Exercise in Elasticsearch
        verify(mockExerciseSearchRepository, times(1)).save(testExercise);
    }

    @Test
    @Transactional
    void createExerciseWithExistingId() throws Exception {
        // Create the Exercise with an existing ID
        exercise.setId(1L);

        int databaseSizeBeforeCreate = exerciseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Exercise in Elasticsearch
        verify(mockExerciseSearchRepository, times(0)).save(exercise);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseRepository.findAll().size();
        // set the field null
        exercise.setName(null);

        // Create the Exercise, which fails.

        restExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exercise))
            )
            .andExpect(status().isBadRequest());

        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExercises() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExercisesWithEagerRelationshipsIsEnabled() throws Exception {
        when(exerciseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExerciseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(exerciseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExercisesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(exerciseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExerciseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(exerciseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get the exercise
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL_ID, exercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exercise.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingExercise() throws Exception {
        // Get the exercise
        restExerciseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();

        // Update the exercise
        Exercise updatedExercise = exerciseRepository.findById(exercise.getId()).get();
        // Disconnect from session so that the updates on updatedExercise are not directly saved in db
        em.detach(updatedExercise);
        updatedExercise.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExercise.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExercise))
            )
            .andExpect(status().isOk());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExercise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Exercise in Elasticsearch
        verify(mockExerciseSearchRepository).save(testExercise);
    }

    @Test
    @Transactional
    void putNonExistingExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exercise.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Exercise in Elasticsearch
        verify(mockExerciseSearchRepository, times(0)).save(exercise);
    }

    @Test
    @Transactional
    void putWithIdMismatchExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Exercise in Elasticsearch
        verify(mockExerciseSearchRepository, times(0)).save(exercise);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exercise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Exercise in Elasticsearch
        verify(mockExerciseSearchRepository, times(0)).save(exercise);
    }

    @Test
    @Transactional
    void partialUpdateExerciseWithPatch() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();

        // Update the exercise using partial update
        Exercise partialUpdatedExercise = new Exercise();
        partialUpdatedExercise.setId(exercise.getId());

        partialUpdatedExercise.description(UPDATED_DESCRIPTION);

        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExercise.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExercise))
            )
            .andExpect(status().isOk());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExercise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateExerciseWithPatch() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();

        // Update the exercise using partial update
        Exercise partialUpdatedExercise = new Exercise();
        partialUpdatedExercise.setId(exercise.getId());

        partialUpdatedExercise.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExercise.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExercise))
            )
            .andExpect(status().isOk());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExercise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exercise.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Exercise in Elasticsearch
        verify(mockExerciseSearchRepository, times(0)).save(exercise);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Exercise in Elasticsearch
        verify(mockExerciseSearchRepository, times(0)).save(exercise);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exercise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Exercise in Elasticsearch
        verify(mockExerciseSearchRepository, times(0)).save(exercise);
    }

    @Test
    @Transactional
    void deleteExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeDelete = exerciseRepository.findAll().size();

        // Delete the exercise
        restExerciseMockMvc
            .perform(delete(ENTITY_API_URL_ID, exercise.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Exercise in Elasticsearch
        verify(mockExerciseSearchRepository, times(1)).deleteById(exercise.getId());
    }

    @Test
    @Transactional
    void searchExercise() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);
        when(mockExerciseSearchRepository.search("id:" + exercise.getId())).thenReturn(Stream.of(exercise));

        // Search the exercise
        restExerciseMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + exercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
