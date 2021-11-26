package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.WorkoutRoutine;
import com.mycompany.myapp.repository.WorkoutRoutineRepository;
import com.mycompany.myapp.repository.search.WorkoutRoutineSearchRepository;
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
 * Integration tests for the {@link WorkoutRoutineResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkoutRoutineResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/workout-routines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/workout-routines";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutRoutineRepository workoutRoutineRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.WorkoutRoutineSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkoutRoutineSearchRepository mockWorkoutRoutineSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkoutRoutineMockMvc;

    private WorkoutRoutine workoutRoutine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutRoutine createEntity(EntityManager em) {
        WorkoutRoutine workoutRoutine = new WorkoutRoutine().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return workoutRoutine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutRoutine createUpdatedEntity(EntityManager em) {
        WorkoutRoutine workoutRoutine = new WorkoutRoutine().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return workoutRoutine;
    }

    @BeforeEach
    public void initTest() {
        workoutRoutine = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkoutRoutine() throws Exception {
        int databaseSizeBeforeCreate = workoutRoutineRepository.findAll().size();
        // Create the WorkoutRoutine
        restWorkoutRoutineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutine))
            )
            .andExpect(status().isCreated());

        // Validate the WorkoutRoutine in the database
        List<WorkoutRoutine> workoutRoutineList = workoutRoutineRepository.findAll();
        assertThat(workoutRoutineList).hasSize(databaseSizeBeforeCreate + 1);
        WorkoutRoutine testWorkoutRoutine = workoutRoutineList.get(workoutRoutineList.size() - 1);
        assertThat(testWorkoutRoutine.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkoutRoutine.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the WorkoutRoutine in Elasticsearch
        verify(mockWorkoutRoutineSearchRepository, times(1)).save(testWorkoutRoutine);
    }

    @Test
    @Transactional
    void createWorkoutRoutineWithExistingId() throws Exception {
        // Create the WorkoutRoutine with an existing ID
        workoutRoutine.setId(1L);

        int databaseSizeBeforeCreate = workoutRoutineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutRoutineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutine))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutine in the database
        List<WorkoutRoutine> workoutRoutineList = workoutRoutineRepository.findAll();
        assertThat(workoutRoutineList).hasSize(databaseSizeBeforeCreate);

        // Validate the WorkoutRoutine in Elasticsearch
        verify(mockWorkoutRoutineSearchRepository, times(0)).save(workoutRoutine);
    }

    @Test
    @Transactional
    void getAllWorkoutRoutines() throws Exception {
        // Initialize the database
        workoutRoutineRepository.saveAndFlush(workoutRoutine);

        // Get all the workoutRoutineList
        restWorkoutRoutineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutRoutine.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getWorkoutRoutine() throws Exception {
        // Initialize the database
        workoutRoutineRepository.saveAndFlush(workoutRoutine);

        // Get the workoutRoutine
        restWorkoutRoutineMockMvc
            .perform(get(ENTITY_API_URL_ID, workoutRoutine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workoutRoutine.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingWorkoutRoutine() throws Exception {
        // Get the workoutRoutine
        restWorkoutRoutineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkoutRoutine() throws Exception {
        // Initialize the database
        workoutRoutineRepository.saveAndFlush(workoutRoutine);

        int databaseSizeBeforeUpdate = workoutRoutineRepository.findAll().size();

        // Update the workoutRoutine
        WorkoutRoutine updatedWorkoutRoutine = workoutRoutineRepository.findById(workoutRoutine.getId()).get();
        // Disconnect from session so that the updates on updatedWorkoutRoutine are not directly saved in db
        em.detach(updatedWorkoutRoutine);
        updatedWorkoutRoutine.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restWorkoutRoutineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkoutRoutine.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkoutRoutine))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutRoutine in the database
        List<WorkoutRoutine> workoutRoutineList = workoutRoutineRepository.findAll();
        assertThat(workoutRoutineList).hasSize(databaseSizeBeforeUpdate);
        WorkoutRoutine testWorkoutRoutine = workoutRoutineList.get(workoutRoutineList.size() - 1);
        assertThat(testWorkoutRoutine.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkoutRoutine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the WorkoutRoutine in Elasticsearch
        verify(mockWorkoutRoutineSearchRepository).save(testWorkoutRoutine);
    }

    @Test
    @Transactional
    void putNonExistingWorkoutRoutine() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineRepository.findAll().size();
        workoutRoutine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutRoutineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutRoutine.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutine))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutine in the database
        List<WorkoutRoutine> workoutRoutineList = workoutRoutineRepository.findAll();
        assertThat(workoutRoutineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutine in Elasticsearch
        verify(mockWorkoutRoutineSearchRepository, times(0)).save(workoutRoutine);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkoutRoutine() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineRepository.findAll().size();
        workoutRoutine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutine))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutine in the database
        List<WorkoutRoutine> workoutRoutineList = workoutRoutineRepository.findAll();
        assertThat(workoutRoutineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutine in Elasticsearch
        verify(mockWorkoutRoutineSearchRepository, times(0)).save(workoutRoutine);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkoutRoutine() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineRepository.findAll().size();
        workoutRoutine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutine))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutRoutine in the database
        List<WorkoutRoutine> workoutRoutineList = workoutRoutineRepository.findAll();
        assertThat(workoutRoutineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutine in Elasticsearch
        verify(mockWorkoutRoutineSearchRepository, times(0)).save(workoutRoutine);
    }

    @Test
    @Transactional
    void partialUpdateWorkoutRoutineWithPatch() throws Exception {
        // Initialize the database
        workoutRoutineRepository.saveAndFlush(workoutRoutine);

        int databaseSizeBeforeUpdate = workoutRoutineRepository.findAll().size();

        // Update the workoutRoutine using partial update
        WorkoutRoutine partialUpdatedWorkoutRoutine = new WorkoutRoutine();
        partialUpdatedWorkoutRoutine.setId(workoutRoutine.getId());

        partialUpdatedWorkoutRoutine.title(UPDATED_TITLE);

        restWorkoutRoutineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutRoutine.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutRoutine))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutRoutine in the database
        List<WorkoutRoutine> workoutRoutineList = workoutRoutineRepository.findAll();
        assertThat(workoutRoutineList).hasSize(databaseSizeBeforeUpdate);
        WorkoutRoutine testWorkoutRoutine = workoutRoutineList.get(workoutRoutineList.size() - 1);
        assertThat(testWorkoutRoutine.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkoutRoutine.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateWorkoutRoutineWithPatch() throws Exception {
        // Initialize the database
        workoutRoutineRepository.saveAndFlush(workoutRoutine);

        int databaseSizeBeforeUpdate = workoutRoutineRepository.findAll().size();

        // Update the workoutRoutine using partial update
        WorkoutRoutine partialUpdatedWorkoutRoutine = new WorkoutRoutine();
        partialUpdatedWorkoutRoutine.setId(workoutRoutine.getId());

        partialUpdatedWorkoutRoutine.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restWorkoutRoutineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutRoutine.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutRoutine))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutRoutine in the database
        List<WorkoutRoutine> workoutRoutineList = workoutRoutineRepository.findAll();
        assertThat(workoutRoutineList).hasSize(databaseSizeBeforeUpdate);
        WorkoutRoutine testWorkoutRoutine = workoutRoutineList.get(workoutRoutineList.size() - 1);
        assertThat(testWorkoutRoutine.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkoutRoutine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingWorkoutRoutine() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineRepository.findAll().size();
        workoutRoutine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutRoutineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workoutRoutine.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutine))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutine in the database
        List<WorkoutRoutine> workoutRoutineList = workoutRoutineRepository.findAll();
        assertThat(workoutRoutineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutine in Elasticsearch
        verify(mockWorkoutRoutineSearchRepository, times(0)).save(workoutRoutine);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkoutRoutine() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineRepository.findAll().size();
        workoutRoutine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutine))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutine in the database
        List<WorkoutRoutine> workoutRoutineList = workoutRoutineRepository.findAll();
        assertThat(workoutRoutineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutine in Elasticsearch
        verify(mockWorkoutRoutineSearchRepository, times(0)).save(workoutRoutine);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkoutRoutine() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineRepository.findAll().size();
        workoutRoutine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutine))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutRoutine in the database
        List<WorkoutRoutine> workoutRoutineList = workoutRoutineRepository.findAll();
        assertThat(workoutRoutineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutine in Elasticsearch
        verify(mockWorkoutRoutineSearchRepository, times(0)).save(workoutRoutine);
    }

    @Test
    @Transactional
    void deleteWorkoutRoutine() throws Exception {
        // Initialize the database
        workoutRoutineRepository.saveAndFlush(workoutRoutine);

        int databaseSizeBeforeDelete = workoutRoutineRepository.findAll().size();

        // Delete the workoutRoutine
        restWorkoutRoutineMockMvc
            .perform(delete(ENTITY_API_URL_ID, workoutRoutine.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkoutRoutine> workoutRoutineList = workoutRoutineRepository.findAll();
        assertThat(workoutRoutineList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WorkoutRoutine in Elasticsearch
        verify(mockWorkoutRoutineSearchRepository, times(1)).deleteById(workoutRoutine.getId());
    }

    @Test
    @Transactional
    void searchWorkoutRoutine() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workoutRoutineRepository.saveAndFlush(workoutRoutine);
        when(mockWorkoutRoutineSearchRepository.search("id:" + workoutRoutine.getId())).thenReturn(Stream.of(workoutRoutine));

        // Search the workoutRoutine
        restWorkoutRoutineMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workoutRoutine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutRoutine.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
