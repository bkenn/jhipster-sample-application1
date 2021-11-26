package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.WorkoutExercise;
import com.mycompany.myapp.repository.WorkoutExerciseRepository;
import com.mycompany.myapp.repository.search.WorkoutExerciseSearchRepository;
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
 * Integration tests for the {@link WorkoutExerciseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkoutExerciseResourceIT {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Duration DEFAULT_TIMER = Duration.ofHours(6);
    private static final Duration UPDATED_TIMER = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/workout-exercises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/workout-exercises";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.WorkoutExerciseSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkoutExerciseSearchRepository mockWorkoutExerciseSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkoutExerciseMockMvc;

    private WorkoutExercise workoutExercise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutExercise createEntity(EntityManager em) {
        WorkoutExercise workoutExercise = new WorkoutExercise().note(DEFAULT_NOTE).timer(DEFAULT_TIMER);
        return workoutExercise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutExercise createUpdatedEntity(EntityManager em) {
        WorkoutExercise workoutExercise = new WorkoutExercise().note(UPDATED_NOTE).timer(UPDATED_TIMER);
        return workoutExercise;
    }

    @BeforeEach
    public void initTest() {
        workoutExercise = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkoutExercise() throws Exception {
        int databaseSizeBeforeCreate = workoutExerciseRepository.findAll().size();
        // Create the WorkoutExercise
        restWorkoutExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutExercise))
            )
            .andExpect(status().isCreated());

        // Validate the WorkoutExercise in the database
        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAll();
        assertThat(workoutExerciseList).hasSize(databaseSizeBeforeCreate + 1);
        WorkoutExercise testWorkoutExercise = workoutExerciseList.get(workoutExerciseList.size() - 1);
        assertThat(testWorkoutExercise.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testWorkoutExercise.getTimer()).isEqualTo(DEFAULT_TIMER);

        // Validate the WorkoutExercise in Elasticsearch
        verify(mockWorkoutExerciseSearchRepository, times(1)).save(testWorkoutExercise);
    }

    @Test
    @Transactional
    void createWorkoutExerciseWithExistingId() throws Exception {
        // Create the WorkoutExercise with an existing ID
        workoutExercise.setId(1L);

        int databaseSizeBeforeCreate = workoutExerciseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutExercise in the database
        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAll();
        assertThat(workoutExerciseList).hasSize(databaseSizeBeforeCreate);

        // Validate the WorkoutExercise in Elasticsearch
        verify(mockWorkoutExerciseSearchRepository, times(0)).save(workoutExercise);
    }

    @Test
    @Transactional
    void getAllWorkoutExercises() throws Exception {
        // Initialize the database
        workoutExerciseRepository.saveAndFlush(workoutExercise);

        // Get all the workoutExerciseList
        restWorkoutExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutExercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].timer").value(hasItem(DEFAULT_TIMER.toString())));
    }

    @Test
    @Transactional
    void getWorkoutExercise() throws Exception {
        // Initialize the database
        workoutExerciseRepository.saveAndFlush(workoutExercise);

        // Get the workoutExercise
        restWorkoutExerciseMockMvc
            .perform(get(ENTITY_API_URL_ID, workoutExercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workoutExercise.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.timer").value(DEFAULT_TIMER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWorkoutExercise() throws Exception {
        // Get the workoutExercise
        restWorkoutExerciseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkoutExercise() throws Exception {
        // Initialize the database
        workoutExerciseRepository.saveAndFlush(workoutExercise);

        int databaseSizeBeforeUpdate = workoutExerciseRepository.findAll().size();

        // Update the workoutExercise
        WorkoutExercise updatedWorkoutExercise = workoutExerciseRepository.findById(workoutExercise.getId()).get();
        // Disconnect from session so that the updates on updatedWorkoutExercise are not directly saved in db
        em.detach(updatedWorkoutExercise);
        updatedWorkoutExercise.note(UPDATED_NOTE).timer(UPDATED_TIMER);

        restWorkoutExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkoutExercise.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkoutExercise))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutExercise in the database
        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAll();
        assertThat(workoutExerciseList).hasSize(databaseSizeBeforeUpdate);
        WorkoutExercise testWorkoutExercise = workoutExerciseList.get(workoutExerciseList.size() - 1);
        assertThat(testWorkoutExercise.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testWorkoutExercise.getTimer()).isEqualTo(UPDATED_TIMER);

        // Validate the WorkoutExercise in Elasticsearch
        verify(mockWorkoutExerciseSearchRepository).save(testWorkoutExercise);
    }

    @Test
    @Transactional
    void putNonExistingWorkoutExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutExerciseRepository.findAll().size();
        workoutExercise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutExercise.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutExercise in the database
        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAll();
        assertThat(workoutExerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutExercise in Elasticsearch
        verify(mockWorkoutExerciseSearchRepository, times(0)).save(workoutExercise);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkoutExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutExerciseRepository.findAll().size();
        workoutExercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutExercise in the database
        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAll();
        assertThat(workoutExerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutExercise in Elasticsearch
        verify(mockWorkoutExerciseSearchRepository, times(0)).save(workoutExercise);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkoutExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutExerciseRepository.findAll().size();
        workoutExercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutExerciseMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutExercise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutExercise in the database
        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAll();
        assertThat(workoutExerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutExercise in Elasticsearch
        verify(mockWorkoutExerciseSearchRepository, times(0)).save(workoutExercise);
    }

    @Test
    @Transactional
    void partialUpdateWorkoutExerciseWithPatch() throws Exception {
        // Initialize the database
        workoutExerciseRepository.saveAndFlush(workoutExercise);

        int databaseSizeBeforeUpdate = workoutExerciseRepository.findAll().size();

        // Update the workoutExercise using partial update
        WorkoutExercise partialUpdatedWorkoutExercise = new WorkoutExercise();
        partialUpdatedWorkoutExercise.setId(workoutExercise.getId());

        partialUpdatedWorkoutExercise.note(UPDATED_NOTE);

        restWorkoutExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutExercise.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutExercise))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutExercise in the database
        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAll();
        assertThat(workoutExerciseList).hasSize(databaseSizeBeforeUpdate);
        WorkoutExercise testWorkoutExercise = workoutExerciseList.get(workoutExerciseList.size() - 1);
        assertThat(testWorkoutExercise.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testWorkoutExercise.getTimer()).isEqualTo(DEFAULT_TIMER);
    }

    @Test
    @Transactional
    void fullUpdateWorkoutExerciseWithPatch() throws Exception {
        // Initialize the database
        workoutExerciseRepository.saveAndFlush(workoutExercise);

        int databaseSizeBeforeUpdate = workoutExerciseRepository.findAll().size();

        // Update the workoutExercise using partial update
        WorkoutExercise partialUpdatedWorkoutExercise = new WorkoutExercise();
        partialUpdatedWorkoutExercise.setId(workoutExercise.getId());

        partialUpdatedWorkoutExercise.note(UPDATED_NOTE).timer(UPDATED_TIMER);

        restWorkoutExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutExercise.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutExercise))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutExercise in the database
        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAll();
        assertThat(workoutExerciseList).hasSize(databaseSizeBeforeUpdate);
        WorkoutExercise testWorkoutExercise = workoutExerciseList.get(workoutExerciseList.size() - 1);
        assertThat(testWorkoutExercise.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testWorkoutExercise.getTimer()).isEqualTo(UPDATED_TIMER);
    }

    @Test
    @Transactional
    void patchNonExistingWorkoutExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutExerciseRepository.findAll().size();
        workoutExercise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workoutExercise.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutExercise in the database
        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAll();
        assertThat(workoutExerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutExercise in Elasticsearch
        verify(mockWorkoutExerciseSearchRepository, times(0)).save(workoutExercise);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkoutExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutExerciseRepository.findAll().size();
        workoutExercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutExercise in the database
        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAll();
        assertThat(workoutExerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutExercise in Elasticsearch
        verify(mockWorkoutExerciseSearchRepository, times(0)).save(workoutExercise);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkoutExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutExerciseRepository.findAll().size();
        workoutExercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutExercise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutExercise in the database
        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAll();
        assertThat(workoutExerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutExercise in Elasticsearch
        verify(mockWorkoutExerciseSearchRepository, times(0)).save(workoutExercise);
    }

    @Test
    @Transactional
    void deleteWorkoutExercise() throws Exception {
        // Initialize the database
        workoutExerciseRepository.saveAndFlush(workoutExercise);

        int databaseSizeBeforeDelete = workoutExerciseRepository.findAll().size();

        // Delete the workoutExercise
        restWorkoutExerciseMockMvc
            .perform(delete(ENTITY_API_URL_ID, workoutExercise.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAll();
        assertThat(workoutExerciseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WorkoutExercise in Elasticsearch
        verify(mockWorkoutExerciseSearchRepository, times(1)).deleteById(workoutExercise.getId());
    }

    @Test
    @Transactional
    void searchWorkoutExercise() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workoutExerciseRepository.saveAndFlush(workoutExercise);
        when(mockWorkoutExerciseSearchRepository.search("id:" + workoutExercise.getId())).thenReturn(Stream.of(workoutExercise));

        // Search the workoutExercise
        restWorkoutExerciseMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workoutExercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutExercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].timer").value(hasItem(DEFAULT_TIMER.toString())));
    }
}
