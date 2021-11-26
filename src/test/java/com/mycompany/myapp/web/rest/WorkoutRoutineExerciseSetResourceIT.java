package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.WorkoutRoutineExerciseSet;
import com.mycompany.myapp.repository.WorkoutRoutineExerciseSetRepository;
import com.mycompany.myapp.repository.search.WorkoutRoutineExerciseSetSearchRepository;
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
 * Integration tests for the {@link WorkoutRoutineExerciseSetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkoutRoutineExerciseSetResourceIT {

    private static final Integer DEFAULT_REPS = 1;
    private static final Integer UPDATED_REPS = 2;

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final Duration DEFAULT_TIME = Duration.ofHours(6);
    private static final Duration UPDATED_TIME = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/workout-routine-exercise-sets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/workout-routine-exercise-sets";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutRoutineExerciseSetRepository workoutRoutineExerciseSetRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.WorkoutRoutineExerciseSetSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkoutRoutineExerciseSetSearchRepository mockWorkoutRoutineExerciseSetSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkoutRoutineExerciseSetMockMvc;

    private WorkoutRoutineExerciseSet workoutRoutineExerciseSet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutRoutineExerciseSet createEntity(EntityManager em) {
        WorkoutRoutineExerciseSet workoutRoutineExerciseSet = new WorkoutRoutineExerciseSet()
            .reps(DEFAULT_REPS)
            .weight(DEFAULT_WEIGHT)
            .time(DEFAULT_TIME);
        return workoutRoutineExerciseSet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutRoutineExerciseSet createUpdatedEntity(EntityManager em) {
        WorkoutRoutineExerciseSet workoutRoutineExerciseSet = new WorkoutRoutineExerciseSet()
            .reps(UPDATED_REPS)
            .weight(UPDATED_WEIGHT)
            .time(UPDATED_TIME);
        return workoutRoutineExerciseSet;
    }

    @BeforeEach
    public void initTest() {
        workoutRoutineExerciseSet = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkoutRoutineExerciseSet() throws Exception {
        int databaseSizeBeforeCreate = workoutRoutineExerciseSetRepository.findAll().size();
        // Create the WorkoutRoutineExerciseSet
        restWorkoutRoutineExerciseSetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExerciseSet))
            )
            .andExpect(status().isCreated());

        // Validate the WorkoutRoutineExerciseSet in the database
        List<WorkoutRoutineExerciseSet> workoutRoutineExerciseSetList = workoutRoutineExerciseSetRepository.findAll();
        assertThat(workoutRoutineExerciseSetList).hasSize(databaseSizeBeforeCreate + 1);
        WorkoutRoutineExerciseSet testWorkoutRoutineExerciseSet = workoutRoutineExerciseSetList.get(
            workoutRoutineExerciseSetList.size() - 1
        );
        assertThat(testWorkoutRoutineExerciseSet.getReps()).isEqualTo(DEFAULT_REPS);
        assertThat(testWorkoutRoutineExerciseSet.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testWorkoutRoutineExerciseSet.getTime()).isEqualTo(DEFAULT_TIME);

        // Validate the WorkoutRoutineExerciseSet in Elasticsearch
        verify(mockWorkoutRoutineExerciseSetSearchRepository, times(1)).save(testWorkoutRoutineExerciseSet);
    }

    @Test
    @Transactional
    void createWorkoutRoutineExerciseSetWithExistingId() throws Exception {
        // Create the WorkoutRoutineExerciseSet with an existing ID
        workoutRoutineExerciseSet.setId(1L);

        int databaseSizeBeforeCreate = workoutRoutineExerciseSetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutRoutineExerciseSetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExerciseSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineExerciseSet in the database
        List<WorkoutRoutineExerciseSet> workoutRoutineExerciseSetList = workoutRoutineExerciseSetRepository.findAll();
        assertThat(workoutRoutineExerciseSetList).hasSize(databaseSizeBeforeCreate);

        // Validate the WorkoutRoutineExerciseSet in Elasticsearch
        verify(mockWorkoutRoutineExerciseSetSearchRepository, times(0)).save(workoutRoutineExerciseSet);
    }

    @Test
    @Transactional
    void getAllWorkoutRoutineExerciseSets() throws Exception {
        // Initialize the database
        workoutRoutineExerciseSetRepository.saveAndFlush(workoutRoutineExerciseSet);

        // Get all the workoutRoutineExerciseSetList
        restWorkoutRoutineExerciseSetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutRoutineExerciseSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].reps").value(hasItem(DEFAULT_REPS)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())));
    }

    @Test
    @Transactional
    void getWorkoutRoutineExerciseSet() throws Exception {
        // Initialize the database
        workoutRoutineExerciseSetRepository.saveAndFlush(workoutRoutineExerciseSet);

        // Get the workoutRoutineExerciseSet
        restWorkoutRoutineExerciseSetMockMvc
            .perform(get(ENTITY_API_URL_ID, workoutRoutineExerciseSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workoutRoutineExerciseSet.getId().intValue()))
            .andExpect(jsonPath("$.reps").value(DEFAULT_REPS))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWorkoutRoutineExerciseSet() throws Exception {
        // Get the workoutRoutineExerciseSet
        restWorkoutRoutineExerciseSetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkoutRoutineExerciseSet() throws Exception {
        // Initialize the database
        workoutRoutineExerciseSetRepository.saveAndFlush(workoutRoutineExerciseSet);

        int databaseSizeBeforeUpdate = workoutRoutineExerciseSetRepository.findAll().size();

        // Update the workoutRoutineExerciseSet
        WorkoutRoutineExerciseSet updatedWorkoutRoutineExerciseSet = workoutRoutineExerciseSetRepository
            .findById(workoutRoutineExerciseSet.getId())
            .get();
        // Disconnect from session so that the updates on updatedWorkoutRoutineExerciseSet are not directly saved in db
        em.detach(updatedWorkoutRoutineExerciseSet);
        updatedWorkoutRoutineExerciseSet.reps(UPDATED_REPS).weight(UPDATED_WEIGHT).time(UPDATED_TIME);

        restWorkoutRoutineExerciseSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkoutRoutineExerciseSet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkoutRoutineExerciseSet))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutRoutineExerciseSet in the database
        List<WorkoutRoutineExerciseSet> workoutRoutineExerciseSetList = workoutRoutineExerciseSetRepository.findAll();
        assertThat(workoutRoutineExerciseSetList).hasSize(databaseSizeBeforeUpdate);
        WorkoutRoutineExerciseSet testWorkoutRoutineExerciseSet = workoutRoutineExerciseSetList.get(
            workoutRoutineExerciseSetList.size() - 1
        );
        assertThat(testWorkoutRoutineExerciseSet.getReps()).isEqualTo(UPDATED_REPS);
        assertThat(testWorkoutRoutineExerciseSet.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testWorkoutRoutineExerciseSet.getTime()).isEqualTo(UPDATED_TIME);

        // Validate the WorkoutRoutineExerciseSet in Elasticsearch
        verify(mockWorkoutRoutineExerciseSetSearchRepository).save(testWorkoutRoutineExerciseSet);
    }

    @Test
    @Transactional
    void putNonExistingWorkoutRoutineExerciseSet() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineExerciseSetRepository.findAll().size();
        workoutRoutineExerciseSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutRoutineExerciseSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutRoutineExerciseSet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExerciseSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineExerciseSet in the database
        List<WorkoutRoutineExerciseSet> workoutRoutineExerciseSetList = workoutRoutineExerciseSetRepository.findAll();
        assertThat(workoutRoutineExerciseSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineExerciseSet in Elasticsearch
        verify(mockWorkoutRoutineExerciseSetSearchRepository, times(0)).save(workoutRoutineExerciseSet);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkoutRoutineExerciseSet() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineExerciseSetRepository.findAll().size();
        workoutRoutineExerciseSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineExerciseSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExerciseSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineExerciseSet in the database
        List<WorkoutRoutineExerciseSet> workoutRoutineExerciseSetList = workoutRoutineExerciseSetRepository.findAll();
        assertThat(workoutRoutineExerciseSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineExerciseSet in Elasticsearch
        verify(mockWorkoutRoutineExerciseSetSearchRepository, times(0)).save(workoutRoutineExerciseSet);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkoutRoutineExerciseSet() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineExerciseSetRepository.findAll().size();
        workoutRoutineExerciseSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineExerciseSetMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExerciseSet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutRoutineExerciseSet in the database
        List<WorkoutRoutineExerciseSet> workoutRoutineExerciseSetList = workoutRoutineExerciseSetRepository.findAll();
        assertThat(workoutRoutineExerciseSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineExerciseSet in Elasticsearch
        verify(mockWorkoutRoutineExerciseSetSearchRepository, times(0)).save(workoutRoutineExerciseSet);
    }

    @Test
    @Transactional
    void partialUpdateWorkoutRoutineExerciseSetWithPatch() throws Exception {
        // Initialize the database
        workoutRoutineExerciseSetRepository.saveAndFlush(workoutRoutineExerciseSet);

        int databaseSizeBeforeUpdate = workoutRoutineExerciseSetRepository.findAll().size();

        // Update the workoutRoutineExerciseSet using partial update
        WorkoutRoutineExerciseSet partialUpdatedWorkoutRoutineExerciseSet = new WorkoutRoutineExerciseSet();
        partialUpdatedWorkoutRoutineExerciseSet.setId(workoutRoutineExerciseSet.getId());

        partialUpdatedWorkoutRoutineExerciseSet.reps(UPDATED_REPS).weight(UPDATED_WEIGHT).time(UPDATED_TIME);

        restWorkoutRoutineExerciseSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutRoutineExerciseSet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutRoutineExerciseSet))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutRoutineExerciseSet in the database
        List<WorkoutRoutineExerciseSet> workoutRoutineExerciseSetList = workoutRoutineExerciseSetRepository.findAll();
        assertThat(workoutRoutineExerciseSetList).hasSize(databaseSizeBeforeUpdate);
        WorkoutRoutineExerciseSet testWorkoutRoutineExerciseSet = workoutRoutineExerciseSetList.get(
            workoutRoutineExerciseSetList.size() - 1
        );
        assertThat(testWorkoutRoutineExerciseSet.getReps()).isEqualTo(UPDATED_REPS);
        assertThat(testWorkoutRoutineExerciseSet.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testWorkoutRoutineExerciseSet.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    void fullUpdateWorkoutRoutineExerciseSetWithPatch() throws Exception {
        // Initialize the database
        workoutRoutineExerciseSetRepository.saveAndFlush(workoutRoutineExerciseSet);

        int databaseSizeBeforeUpdate = workoutRoutineExerciseSetRepository.findAll().size();

        // Update the workoutRoutineExerciseSet using partial update
        WorkoutRoutineExerciseSet partialUpdatedWorkoutRoutineExerciseSet = new WorkoutRoutineExerciseSet();
        partialUpdatedWorkoutRoutineExerciseSet.setId(workoutRoutineExerciseSet.getId());

        partialUpdatedWorkoutRoutineExerciseSet.reps(UPDATED_REPS).weight(UPDATED_WEIGHT).time(UPDATED_TIME);

        restWorkoutRoutineExerciseSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutRoutineExerciseSet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutRoutineExerciseSet))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutRoutineExerciseSet in the database
        List<WorkoutRoutineExerciseSet> workoutRoutineExerciseSetList = workoutRoutineExerciseSetRepository.findAll();
        assertThat(workoutRoutineExerciseSetList).hasSize(databaseSizeBeforeUpdate);
        WorkoutRoutineExerciseSet testWorkoutRoutineExerciseSet = workoutRoutineExerciseSetList.get(
            workoutRoutineExerciseSetList.size() - 1
        );
        assertThat(testWorkoutRoutineExerciseSet.getReps()).isEqualTo(UPDATED_REPS);
        assertThat(testWorkoutRoutineExerciseSet.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testWorkoutRoutineExerciseSet.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingWorkoutRoutineExerciseSet() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineExerciseSetRepository.findAll().size();
        workoutRoutineExerciseSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutRoutineExerciseSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workoutRoutineExerciseSet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExerciseSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineExerciseSet in the database
        List<WorkoutRoutineExerciseSet> workoutRoutineExerciseSetList = workoutRoutineExerciseSetRepository.findAll();
        assertThat(workoutRoutineExerciseSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineExerciseSet in Elasticsearch
        verify(mockWorkoutRoutineExerciseSetSearchRepository, times(0)).save(workoutRoutineExerciseSet);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkoutRoutineExerciseSet() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineExerciseSetRepository.findAll().size();
        workoutRoutineExerciseSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineExerciseSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExerciseSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineExerciseSet in the database
        List<WorkoutRoutineExerciseSet> workoutRoutineExerciseSetList = workoutRoutineExerciseSetRepository.findAll();
        assertThat(workoutRoutineExerciseSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineExerciseSet in Elasticsearch
        verify(mockWorkoutRoutineExerciseSetSearchRepository, times(0)).save(workoutRoutineExerciseSet);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkoutRoutineExerciseSet() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineExerciseSetRepository.findAll().size();
        workoutRoutineExerciseSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineExerciseSetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineExerciseSet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutRoutineExerciseSet in the database
        List<WorkoutRoutineExerciseSet> workoutRoutineExerciseSetList = workoutRoutineExerciseSetRepository.findAll();
        assertThat(workoutRoutineExerciseSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineExerciseSet in Elasticsearch
        verify(mockWorkoutRoutineExerciseSetSearchRepository, times(0)).save(workoutRoutineExerciseSet);
    }

    @Test
    @Transactional
    void deleteWorkoutRoutineExerciseSet() throws Exception {
        // Initialize the database
        workoutRoutineExerciseSetRepository.saveAndFlush(workoutRoutineExerciseSet);

        int databaseSizeBeforeDelete = workoutRoutineExerciseSetRepository.findAll().size();

        // Delete the workoutRoutineExerciseSet
        restWorkoutRoutineExerciseSetMockMvc
            .perform(delete(ENTITY_API_URL_ID, workoutRoutineExerciseSet.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkoutRoutineExerciseSet> workoutRoutineExerciseSetList = workoutRoutineExerciseSetRepository.findAll();
        assertThat(workoutRoutineExerciseSetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WorkoutRoutineExerciseSet in Elasticsearch
        verify(mockWorkoutRoutineExerciseSetSearchRepository, times(1)).deleteById(workoutRoutineExerciseSet.getId());
    }

    @Test
    @Transactional
    void searchWorkoutRoutineExerciseSet() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workoutRoutineExerciseSetRepository.saveAndFlush(workoutRoutineExerciseSet);
        when(mockWorkoutRoutineExerciseSetSearchRepository.search("id:" + workoutRoutineExerciseSet.getId()))
            .thenReturn(Stream.of(workoutRoutineExerciseSet));

        // Search the workoutRoutineExerciseSet
        restWorkoutRoutineExerciseSetMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workoutRoutineExerciseSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutRoutineExerciseSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].reps").value(hasItem(DEFAULT_REPS)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())));
    }
}
