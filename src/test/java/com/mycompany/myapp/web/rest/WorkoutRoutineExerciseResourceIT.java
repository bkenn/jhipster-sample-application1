package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.WorkoutRoutineExercise;
import com.mycompany.myapp.repository.WorkoutRoutineExerciseRepository;
import com.mycompany.myapp.repository.search.WorkoutRoutineExerciseSearchRepository;
import java.time.Duration;
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
 * Integration tests for the {@link WorkoutRoutineExerciseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkoutRoutineExerciseResourceIT {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Duration DEFAULT_TIMER = Duration.ofHours(6);
    private static final Duration UPDATED_TIMER = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/workout-routine-exercises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/workout-routine-exercises";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutRoutineExerciseRepository workoutRoutineExerciseRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.WorkoutRoutineExerciseSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkoutRoutineExerciseSearchRepository mockWorkoutRoutineExerciseSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkoutRoutineExerciseMockMvc;

    private WorkoutRoutineExercise workoutRoutineExercise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutRoutineExercise createEntity(EntityManager em) {
        WorkoutRoutineExercise workoutRoutineExercise = new WorkoutRoutineExercise().note(DEFAULT_NOTE).timer(DEFAULT_TIMER);
        return workoutRoutineExercise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutRoutineExercise createUpdatedEntity(EntityManager em) {
        WorkoutRoutineExercise workoutRoutineExercise = new WorkoutRoutineExercise().note(UPDATED_NOTE).timer(UPDATED_TIMER);
        return workoutRoutineExercise;
    }

    @BeforeEach
    public void initTest() {
        workoutRoutineExercise = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkoutRoutineExercise() throws Exception {
        int databaseSizeBeforeCreate = workoutRoutineExerciseRepository.findAll().size();
        // Create the WorkoutRoutineExercise
        restWorkoutRoutineExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExercise))
            )
            .andExpect(status().isCreated());

        // Validate the WorkoutRoutineExercise in the database
        List<WorkoutRoutineExercise> workoutRoutineExerciseList = workoutRoutineExerciseRepository.findAll();
        assertThat(workoutRoutineExerciseList).hasSize(databaseSizeBeforeCreate + 1);
        WorkoutRoutineExercise testWorkoutRoutineExercise = workoutRoutineExerciseList.get(workoutRoutineExerciseList.size() - 1);
        assertThat(testWorkoutRoutineExercise.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testWorkoutRoutineExercise.getTimer()).isEqualTo(DEFAULT_TIMER);

        // Validate the WorkoutRoutineExercise in Elasticsearch
        verify(mockWorkoutRoutineExerciseSearchRepository, times(1)).save(testWorkoutRoutineExercise);
    }

    @Test
    @Transactional
    void createWorkoutRoutineExerciseWithExistingId() throws Exception {
        // Create the WorkoutRoutineExercise with an existing ID
        workoutRoutineExercise.setId(1L);

        int databaseSizeBeforeCreate = workoutRoutineExerciseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutRoutineExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineExercise in the database
        List<WorkoutRoutineExercise> workoutRoutineExerciseList = workoutRoutineExerciseRepository.findAll();
        assertThat(workoutRoutineExerciseList).hasSize(databaseSizeBeforeCreate);

        // Validate the WorkoutRoutineExercise in Elasticsearch
        verify(mockWorkoutRoutineExerciseSearchRepository, times(0)).save(workoutRoutineExercise);
    }

    @Test
    @Transactional
    void getAllWorkoutRoutineExercises() throws Exception {
        // Initialize the database
        workoutRoutineExerciseRepository.saveAndFlush(workoutRoutineExercise);

        // Get all the workoutRoutineExerciseList
        restWorkoutRoutineExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutRoutineExercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].timer").value(hasItem(DEFAULT_TIMER.toString())));
    }

    @Test
    @Transactional
    void getWorkoutRoutineExercise() throws Exception {
        // Initialize the database
        workoutRoutineExerciseRepository.saveAndFlush(workoutRoutineExercise);

        // Get the workoutRoutineExercise
        restWorkoutRoutineExerciseMockMvc
            .perform(get(ENTITY_API_URL_ID, workoutRoutineExercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workoutRoutineExercise.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.timer").value(DEFAULT_TIMER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWorkoutRoutineExercise() throws Exception {
        // Get the workoutRoutineExercise
        restWorkoutRoutineExerciseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkoutRoutineExercise() throws Exception {
        // Initialize the database
        workoutRoutineExerciseRepository.saveAndFlush(workoutRoutineExercise);

        int databaseSizeBeforeUpdate = workoutRoutineExerciseRepository.findAll().size();

        // Update the workoutRoutineExercise
        WorkoutRoutineExercise updatedWorkoutRoutineExercise = workoutRoutineExerciseRepository
            .findById(workoutRoutineExercise.getId())
            .get();
        // Disconnect from session so that the updates on updatedWorkoutRoutineExercise are not directly saved in db
        em.detach(updatedWorkoutRoutineExercise);
        updatedWorkoutRoutineExercise.note(UPDATED_NOTE).timer(UPDATED_TIMER);

        restWorkoutRoutineExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkoutRoutineExercise.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkoutRoutineExercise))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutRoutineExercise in the database
        List<WorkoutRoutineExercise> workoutRoutineExerciseList = workoutRoutineExerciseRepository.findAll();
        assertThat(workoutRoutineExerciseList).hasSize(databaseSizeBeforeUpdate);
        WorkoutRoutineExercise testWorkoutRoutineExercise = workoutRoutineExerciseList.get(workoutRoutineExerciseList.size() - 1);
        assertThat(testWorkoutRoutineExercise.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testWorkoutRoutineExercise.getTimer()).isEqualTo(UPDATED_TIMER);

        // Validate the WorkoutRoutineExercise in Elasticsearch
        verify(mockWorkoutRoutineExerciseSearchRepository).save(testWorkoutRoutineExercise);
    }

    @Test
    @Transactional
    void putNonExistingWorkoutRoutineExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineExerciseRepository.findAll().size();
        workoutRoutineExercise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutRoutineExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutRoutineExercise.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineExercise in the database
        List<WorkoutRoutineExercise> workoutRoutineExerciseList = workoutRoutineExerciseRepository.findAll();
        assertThat(workoutRoutineExerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineExercise in Elasticsearch
        verify(mockWorkoutRoutineExerciseSearchRepository, times(0)).save(workoutRoutineExercise);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkoutRoutineExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineExerciseRepository.findAll().size();
        workoutRoutineExercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineExercise in the database
        List<WorkoutRoutineExercise> workoutRoutineExerciseList = workoutRoutineExerciseRepository.findAll();
        assertThat(workoutRoutineExerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineExercise in Elasticsearch
        verify(mockWorkoutRoutineExerciseSearchRepository, times(0)).save(workoutRoutineExercise);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkoutRoutineExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineExerciseRepository.findAll().size();
        workoutRoutineExercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineExerciseMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExercise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutRoutineExercise in the database
        List<WorkoutRoutineExercise> workoutRoutineExerciseList = workoutRoutineExerciseRepository.findAll();
        assertThat(workoutRoutineExerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineExercise in Elasticsearch
        verify(mockWorkoutRoutineExerciseSearchRepository, times(0)).save(workoutRoutineExercise);
    }

    @Test
    @Transactional
    void partialUpdateWorkoutRoutineExerciseWithPatch() throws Exception {
        // Initialize the database
        workoutRoutineExerciseRepository.saveAndFlush(workoutRoutineExercise);

        int databaseSizeBeforeUpdate = workoutRoutineExerciseRepository.findAll().size();

        // Update the workoutRoutineExercise using partial update
        WorkoutRoutineExercise partialUpdatedWorkoutRoutineExercise = new WorkoutRoutineExercise();
        partialUpdatedWorkoutRoutineExercise.setId(workoutRoutineExercise.getId());

        partialUpdatedWorkoutRoutineExercise.timer(UPDATED_TIMER);

        restWorkoutRoutineExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutRoutineExercise.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutRoutineExercise))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutRoutineExercise in the database
        List<WorkoutRoutineExercise> workoutRoutineExerciseList = workoutRoutineExerciseRepository.findAll();
        assertThat(workoutRoutineExerciseList).hasSize(databaseSizeBeforeUpdate);
        WorkoutRoutineExercise testWorkoutRoutineExercise = workoutRoutineExerciseList.get(workoutRoutineExerciseList.size() - 1);
        assertThat(testWorkoutRoutineExercise.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testWorkoutRoutineExercise.getTimer()).isEqualTo(UPDATED_TIMER);
    }

    @Test
    @Transactional
    void fullUpdateWorkoutRoutineExerciseWithPatch() throws Exception {
        // Initialize the database
        workoutRoutineExerciseRepository.saveAndFlush(workoutRoutineExercise);

        int databaseSizeBeforeUpdate = workoutRoutineExerciseRepository.findAll().size();

        // Update the workoutRoutineExercise using partial update
        WorkoutRoutineExercise partialUpdatedWorkoutRoutineExercise = new WorkoutRoutineExercise();
        partialUpdatedWorkoutRoutineExercise.setId(workoutRoutineExercise.getId());

        partialUpdatedWorkoutRoutineExercise.note(UPDATED_NOTE).timer(UPDATED_TIMER);

        restWorkoutRoutineExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutRoutineExercise.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutRoutineExercise))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutRoutineExercise in the database
        List<WorkoutRoutineExercise> workoutRoutineExerciseList = workoutRoutineExerciseRepository.findAll();
        assertThat(workoutRoutineExerciseList).hasSize(databaseSizeBeforeUpdate);
        WorkoutRoutineExercise testWorkoutRoutineExercise = workoutRoutineExerciseList.get(workoutRoutineExerciseList.size() - 1);
        assertThat(testWorkoutRoutineExercise.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testWorkoutRoutineExercise.getTimer()).isEqualTo(UPDATED_TIMER);
    }

    @Test
    @Transactional
    void patchNonExistingWorkoutRoutineExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineExerciseRepository.findAll().size();
        workoutRoutineExercise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutRoutineExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workoutRoutineExercise.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineExercise in the database
        List<WorkoutRoutineExercise> workoutRoutineExerciseList = workoutRoutineExerciseRepository.findAll();
        assertThat(workoutRoutineExerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineExercise in Elasticsearch
        verify(mockWorkoutRoutineExerciseSearchRepository, times(0)).save(workoutRoutineExercise);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkoutRoutineExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineExerciseRepository.findAll().size();
        workoutRoutineExercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineExercise in the database
        List<WorkoutRoutineExercise> workoutRoutineExerciseList = workoutRoutineExerciseRepository.findAll();
        assertThat(workoutRoutineExerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineExercise in Elasticsearch
        verify(mockWorkoutRoutineExerciseSearchRepository, times(0)).save(workoutRoutineExercise);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkoutRoutineExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineExerciseRepository.findAll().size();
        workoutRoutineExercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExercise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutRoutineExercise in the database
        List<WorkoutRoutineExercise> workoutRoutineExerciseList = workoutRoutineExerciseRepository.findAll();
        assertThat(workoutRoutineExerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineExercise in Elasticsearch
        verify(mockWorkoutRoutineExerciseSearchRepository, times(0)).save(workoutRoutineExercise);
    }

    @Test
    @Transactional
    void deleteWorkoutRoutineExercise() throws Exception {
        // Initialize the database
        workoutRoutineExerciseRepository.saveAndFlush(workoutRoutineExercise);

        int databaseSizeBeforeDelete = workoutRoutineExerciseRepository.findAll().size();

        // Delete the workoutRoutineExercise
        restWorkoutRoutineExerciseMockMvc
            .perform(delete(ENTITY_API_URL_ID, workoutRoutineExercise.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkoutRoutineExercise> workoutRoutineExerciseList = workoutRoutineExerciseRepository.findAll();
        assertThat(workoutRoutineExerciseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WorkoutRoutineExercise in Elasticsearch
        verify(mockWorkoutRoutineExerciseSearchRepository, times(1)).deleteById(workoutRoutineExercise.getId());
    }

    @Test
    @Transactional
    void searchWorkoutRoutineExercise() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workoutRoutineExerciseRepository.saveAndFlush(workoutRoutineExercise);
        when(mockWorkoutRoutineExerciseSearchRepository.search("id:" + workoutRoutineExercise.getId()))
            .thenReturn(Stream.of(workoutRoutineExercise));

        // Search the workoutRoutineExercise
        restWorkoutRoutineExerciseMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workoutRoutineExercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutRoutineExercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].timer").value(hasItem(DEFAULT_TIMER.toString())));
    }
}
