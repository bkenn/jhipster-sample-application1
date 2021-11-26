package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.WorkoutExerciseSet;
import com.mycompany.myapp.repository.WorkoutExerciseSetRepository;
import com.mycompany.myapp.repository.search.WorkoutExerciseSetSearchRepository;
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
 * Integration tests for the {@link WorkoutExerciseSetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkoutExerciseSetResourceIT {

    private static final Integer DEFAULT_REPS = 1;
    private static final Integer UPDATED_REPS = 2;

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final Duration DEFAULT_TIME = Duration.ofHours(6);
    private static final Duration UPDATED_TIME = Duration.ofHours(12);

    private static final Boolean DEFAULT_COMPLETE = false;
    private static final Boolean UPDATED_COMPLETE = true;

    private static final Duration DEFAULT_COMPLETE_TIME = Duration.ofHours(6);
    private static final Duration UPDATED_COMPLETE_TIME = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/workout-exercise-sets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/workout-exercise-sets";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutExerciseSetRepository workoutExerciseSetRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.WorkoutExerciseSetSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkoutExerciseSetSearchRepository mockWorkoutExerciseSetSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkoutExerciseSetMockMvc;

    private WorkoutExerciseSet workoutExerciseSet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutExerciseSet createEntity(EntityManager em) {
        WorkoutExerciseSet workoutExerciseSet = new WorkoutExerciseSet()
            .reps(DEFAULT_REPS)
            .weight(DEFAULT_WEIGHT)
            .time(DEFAULT_TIME)
            .complete(DEFAULT_COMPLETE)
            .completeTime(DEFAULT_COMPLETE_TIME);
        return workoutExerciseSet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutExerciseSet createUpdatedEntity(EntityManager em) {
        WorkoutExerciseSet workoutExerciseSet = new WorkoutExerciseSet()
            .reps(UPDATED_REPS)
            .weight(UPDATED_WEIGHT)
            .time(UPDATED_TIME)
            .complete(UPDATED_COMPLETE)
            .completeTime(UPDATED_COMPLETE_TIME);
        return workoutExerciseSet;
    }

    @BeforeEach
    public void initTest() {
        workoutExerciseSet = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkoutExerciseSet() throws Exception {
        int databaseSizeBeforeCreate = workoutExerciseSetRepository.findAll().size();
        // Create the WorkoutExerciseSet
        restWorkoutExerciseSetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutExerciseSet))
            )
            .andExpect(status().isCreated());

        // Validate the WorkoutExerciseSet in the database
        List<WorkoutExerciseSet> workoutExerciseSetList = workoutExerciseSetRepository.findAll();
        assertThat(workoutExerciseSetList).hasSize(databaseSizeBeforeCreate + 1);
        WorkoutExerciseSet testWorkoutExerciseSet = workoutExerciseSetList.get(workoutExerciseSetList.size() - 1);
        assertThat(testWorkoutExerciseSet.getReps()).isEqualTo(DEFAULT_REPS);
        assertThat(testWorkoutExerciseSet.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testWorkoutExerciseSet.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testWorkoutExerciseSet.getComplete()).isEqualTo(DEFAULT_COMPLETE);
        assertThat(testWorkoutExerciseSet.getCompleteTime()).isEqualTo(DEFAULT_COMPLETE_TIME);

        // Validate the WorkoutExerciseSet in Elasticsearch
        verify(mockWorkoutExerciseSetSearchRepository, times(1)).save(testWorkoutExerciseSet);
    }

    @Test
    @Transactional
    void createWorkoutExerciseSetWithExistingId() throws Exception {
        // Create the WorkoutExerciseSet with an existing ID
        workoutExerciseSet.setId(1L);

        int databaseSizeBeforeCreate = workoutExerciseSetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutExerciseSetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutExerciseSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutExerciseSet in the database
        List<WorkoutExerciseSet> workoutExerciseSetList = workoutExerciseSetRepository.findAll();
        assertThat(workoutExerciseSetList).hasSize(databaseSizeBeforeCreate);

        // Validate the WorkoutExerciseSet in Elasticsearch
        verify(mockWorkoutExerciseSetSearchRepository, times(0)).save(workoutExerciseSet);
    }

    @Test
    @Transactional
    void getAllWorkoutExerciseSets() throws Exception {
        // Initialize the database
        workoutExerciseSetRepository.saveAndFlush(workoutExerciseSet);

        // Get all the workoutExerciseSetList
        restWorkoutExerciseSetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutExerciseSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].reps").value(hasItem(DEFAULT_REPS)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].complete").value(hasItem(DEFAULT_COMPLETE.booleanValue())))
            .andExpect(jsonPath("$.[*].completeTime").value(hasItem(DEFAULT_COMPLETE_TIME.toString())));
    }

    @Test
    @Transactional
    void getWorkoutExerciseSet() throws Exception {
        // Initialize the database
        workoutExerciseSetRepository.saveAndFlush(workoutExerciseSet);

        // Get the workoutExerciseSet
        restWorkoutExerciseSetMockMvc
            .perform(get(ENTITY_API_URL_ID, workoutExerciseSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workoutExerciseSet.getId().intValue()))
            .andExpect(jsonPath("$.reps").value(DEFAULT_REPS))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.complete").value(DEFAULT_COMPLETE.booleanValue()))
            .andExpect(jsonPath("$.completeTime").value(DEFAULT_COMPLETE_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWorkoutExerciseSet() throws Exception {
        // Get the workoutExerciseSet
        restWorkoutExerciseSetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkoutExerciseSet() throws Exception {
        // Initialize the database
        workoutExerciseSetRepository.saveAndFlush(workoutExerciseSet);

        int databaseSizeBeforeUpdate = workoutExerciseSetRepository.findAll().size();

        // Update the workoutExerciseSet
        WorkoutExerciseSet updatedWorkoutExerciseSet = workoutExerciseSetRepository.findById(workoutExerciseSet.getId()).get();
        // Disconnect from session so that the updates on updatedWorkoutExerciseSet are not directly saved in db
        em.detach(updatedWorkoutExerciseSet);
        updatedWorkoutExerciseSet
            .reps(UPDATED_REPS)
            .weight(UPDATED_WEIGHT)
            .time(UPDATED_TIME)
            .complete(UPDATED_COMPLETE)
            .completeTime(UPDATED_COMPLETE_TIME);

        restWorkoutExerciseSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkoutExerciseSet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkoutExerciseSet))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutExerciseSet in the database
        List<WorkoutExerciseSet> workoutExerciseSetList = workoutExerciseSetRepository.findAll();
        assertThat(workoutExerciseSetList).hasSize(databaseSizeBeforeUpdate);
        WorkoutExerciseSet testWorkoutExerciseSet = workoutExerciseSetList.get(workoutExerciseSetList.size() - 1);
        assertThat(testWorkoutExerciseSet.getReps()).isEqualTo(UPDATED_REPS);
        assertThat(testWorkoutExerciseSet.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testWorkoutExerciseSet.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testWorkoutExerciseSet.getComplete()).isEqualTo(UPDATED_COMPLETE);
        assertThat(testWorkoutExerciseSet.getCompleteTime()).isEqualTo(UPDATED_COMPLETE_TIME);

        // Validate the WorkoutExerciseSet in Elasticsearch
        verify(mockWorkoutExerciseSetSearchRepository).save(testWorkoutExerciseSet);
    }

    @Test
    @Transactional
    void putNonExistingWorkoutExerciseSet() throws Exception {
        int databaseSizeBeforeUpdate = workoutExerciseSetRepository.findAll().size();
        workoutExerciseSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutExerciseSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutExerciseSet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutExerciseSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutExerciseSet in the database
        List<WorkoutExerciseSet> workoutExerciseSetList = workoutExerciseSetRepository.findAll();
        assertThat(workoutExerciseSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutExerciseSet in Elasticsearch
        verify(mockWorkoutExerciseSetSearchRepository, times(0)).save(workoutExerciseSet);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkoutExerciseSet() throws Exception {
        int databaseSizeBeforeUpdate = workoutExerciseSetRepository.findAll().size();
        workoutExerciseSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutExerciseSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutExerciseSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutExerciseSet in the database
        List<WorkoutExerciseSet> workoutExerciseSetList = workoutExerciseSetRepository.findAll();
        assertThat(workoutExerciseSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutExerciseSet in Elasticsearch
        verify(mockWorkoutExerciseSetSearchRepository, times(0)).save(workoutExerciseSet);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkoutExerciseSet() throws Exception {
        int databaseSizeBeforeUpdate = workoutExerciseSetRepository.findAll().size();
        workoutExerciseSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutExerciseSetMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutExerciseSet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutExerciseSet in the database
        List<WorkoutExerciseSet> workoutExerciseSetList = workoutExerciseSetRepository.findAll();
        assertThat(workoutExerciseSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutExerciseSet in Elasticsearch
        verify(mockWorkoutExerciseSetSearchRepository, times(0)).save(workoutExerciseSet);
    }

    @Test
    @Transactional
    void partialUpdateWorkoutExerciseSetWithPatch() throws Exception {
        // Initialize the database
        workoutExerciseSetRepository.saveAndFlush(workoutExerciseSet);

        int databaseSizeBeforeUpdate = workoutExerciseSetRepository.findAll().size();

        // Update the workoutExerciseSet using partial update
        WorkoutExerciseSet partialUpdatedWorkoutExerciseSet = new WorkoutExerciseSet();
        partialUpdatedWorkoutExerciseSet.setId(workoutExerciseSet.getId());

        partialUpdatedWorkoutExerciseSet.reps(UPDATED_REPS).weight(UPDATED_WEIGHT).completeTime(UPDATED_COMPLETE_TIME);

        restWorkoutExerciseSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutExerciseSet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutExerciseSet))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutExerciseSet in the database
        List<WorkoutExerciseSet> workoutExerciseSetList = workoutExerciseSetRepository.findAll();
        assertThat(workoutExerciseSetList).hasSize(databaseSizeBeforeUpdate);
        WorkoutExerciseSet testWorkoutExerciseSet = workoutExerciseSetList.get(workoutExerciseSetList.size() - 1);
        assertThat(testWorkoutExerciseSet.getReps()).isEqualTo(UPDATED_REPS);
        assertThat(testWorkoutExerciseSet.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testWorkoutExerciseSet.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testWorkoutExerciseSet.getComplete()).isEqualTo(DEFAULT_COMPLETE);
        assertThat(testWorkoutExerciseSet.getCompleteTime()).isEqualTo(UPDATED_COMPLETE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateWorkoutExerciseSetWithPatch() throws Exception {
        // Initialize the database
        workoutExerciseSetRepository.saveAndFlush(workoutExerciseSet);

        int databaseSizeBeforeUpdate = workoutExerciseSetRepository.findAll().size();

        // Update the workoutExerciseSet using partial update
        WorkoutExerciseSet partialUpdatedWorkoutExerciseSet = new WorkoutExerciseSet();
        partialUpdatedWorkoutExerciseSet.setId(workoutExerciseSet.getId());

        partialUpdatedWorkoutExerciseSet
            .reps(UPDATED_REPS)
            .weight(UPDATED_WEIGHT)
            .time(UPDATED_TIME)
            .complete(UPDATED_COMPLETE)
            .completeTime(UPDATED_COMPLETE_TIME);

        restWorkoutExerciseSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutExerciseSet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutExerciseSet))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutExerciseSet in the database
        List<WorkoutExerciseSet> workoutExerciseSetList = workoutExerciseSetRepository.findAll();
        assertThat(workoutExerciseSetList).hasSize(databaseSizeBeforeUpdate);
        WorkoutExerciseSet testWorkoutExerciseSet = workoutExerciseSetList.get(workoutExerciseSetList.size() - 1);
        assertThat(testWorkoutExerciseSet.getReps()).isEqualTo(UPDATED_REPS);
        assertThat(testWorkoutExerciseSet.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testWorkoutExerciseSet.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testWorkoutExerciseSet.getComplete()).isEqualTo(UPDATED_COMPLETE);
        assertThat(testWorkoutExerciseSet.getCompleteTime()).isEqualTo(UPDATED_COMPLETE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingWorkoutExerciseSet() throws Exception {
        int databaseSizeBeforeUpdate = workoutExerciseSetRepository.findAll().size();
        workoutExerciseSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutExerciseSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workoutExerciseSet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutExerciseSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutExerciseSet in the database
        List<WorkoutExerciseSet> workoutExerciseSetList = workoutExerciseSetRepository.findAll();
        assertThat(workoutExerciseSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutExerciseSet in Elasticsearch
        verify(mockWorkoutExerciseSetSearchRepository, times(0)).save(workoutExerciseSet);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkoutExerciseSet() throws Exception {
        int databaseSizeBeforeUpdate = workoutExerciseSetRepository.findAll().size();
        workoutExerciseSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutExerciseSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutExerciseSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutExerciseSet in the database
        List<WorkoutExerciseSet> workoutExerciseSetList = workoutExerciseSetRepository.findAll();
        assertThat(workoutExerciseSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutExerciseSet in Elasticsearch
        verify(mockWorkoutExerciseSetSearchRepository, times(0)).save(workoutExerciseSet);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkoutExerciseSet() throws Exception {
        int databaseSizeBeforeUpdate = workoutExerciseSetRepository.findAll().size();
        workoutExerciseSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutExerciseSetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutExerciseSet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutExerciseSet in the database
        List<WorkoutExerciseSet> workoutExerciseSetList = workoutExerciseSetRepository.findAll();
        assertThat(workoutExerciseSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutExerciseSet in Elasticsearch
        verify(mockWorkoutExerciseSetSearchRepository, times(0)).save(workoutExerciseSet);
    }

    @Test
    @Transactional
    void deleteWorkoutExerciseSet() throws Exception {
        // Initialize the database
        workoutExerciseSetRepository.saveAndFlush(workoutExerciseSet);

        int databaseSizeBeforeDelete = workoutExerciseSetRepository.findAll().size();

        // Delete the workoutExerciseSet
        restWorkoutExerciseSetMockMvc
            .perform(delete(ENTITY_API_URL_ID, workoutExerciseSet.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkoutExerciseSet> workoutExerciseSetList = workoutExerciseSetRepository.findAll();
        assertThat(workoutExerciseSetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WorkoutExerciseSet in Elasticsearch
        verify(mockWorkoutExerciseSetSearchRepository, times(1)).deleteById(workoutExerciseSet.getId());
    }

    @Test
    @Transactional
    void searchWorkoutExerciseSet() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workoutExerciseSetRepository.saveAndFlush(workoutExerciseSet);
        when(mockWorkoutExerciseSetSearchRepository.search("id:" + workoutExerciseSet.getId())).thenReturn(Stream.of(workoutExerciseSet));

        // Search the workoutExerciseSet
        restWorkoutExerciseSetMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workoutExerciseSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutExerciseSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].reps").value(hasItem(DEFAULT_REPS)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].complete").value(hasItem(DEFAULT_COMPLETE.booleanValue())))
            .andExpect(jsonPath("$.[*].completeTime").value(hasItem(DEFAULT_COMPLETE_TIME.toString())));
    }
}
