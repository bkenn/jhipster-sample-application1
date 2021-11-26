package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Workout;
import com.mycompany.myapp.repository.WorkoutRepository;
import com.mycompany.myapp.repository.search.WorkoutSearchRepository;
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
 * Integration tests for the {@link WorkoutResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkoutResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_WORKOUT_START_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_WORKOUT_START_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_WORKOUT_END_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_WORKOUT_END_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/workouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/workouts";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutRepository workoutRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.WorkoutSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkoutSearchRepository mockWorkoutSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkoutMockMvc;

    private Workout workout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workout createEntity(EntityManager em) {
        Workout workout = new Workout()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .workoutStartDateTime(DEFAULT_WORKOUT_START_DATE_TIME)
            .workoutEndDateTime(DEFAULT_WORKOUT_END_DATE_TIME);
        return workout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workout createUpdatedEntity(EntityManager em) {
        Workout workout = new Workout()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .workoutStartDateTime(UPDATED_WORKOUT_START_DATE_TIME)
            .workoutEndDateTime(UPDATED_WORKOUT_END_DATE_TIME);
        return workout;
    }

    @BeforeEach
    public void initTest() {
        workout = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkout() throws Exception {
        int databaseSizeBeforeCreate = workoutRepository.findAll().size();
        // Create the Workout
        restWorkoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workout))
            )
            .andExpect(status().isCreated());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeCreate + 1);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkout.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkout.getWorkoutStartDateTime()).isEqualTo(DEFAULT_WORKOUT_START_DATE_TIME);
        assertThat(testWorkout.getWorkoutEndDateTime()).isEqualTo(DEFAULT_WORKOUT_END_DATE_TIME);

        // Validate the Workout in Elasticsearch
        verify(mockWorkoutSearchRepository, times(1)).save(testWorkout);
    }

    @Test
    @Transactional
    void createWorkoutWithExistingId() throws Exception {
        // Create the Workout with an existing ID
        workout.setId(1L);

        int databaseSizeBeforeCreate = workoutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workout))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeCreate);

        // Validate the Workout in Elasticsearch
        verify(mockWorkoutSearchRepository, times(0)).save(workout);
    }

    @Test
    @Transactional
    void getAllWorkouts() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList
        restWorkoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workout.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].workoutStartDateTime").value(hasItem(sameInstant(DEFAULT_WORKOUT_START_DATE_TIME))))
            .andExpect(jsonPath("$.[*].workoutEndDateTime").value(hasItem(sameInstant(DEFAULT_WORKOUT_END_DATE_TIME))));
    }

    @Test
    @Transactional
    void getWorkout() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get the workout
        restWorkoutMockMvc
            .perform(get(ENTITY_API_URL_ID, workout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workout.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.workoutStartDateTime").value(sameInstant(DEFAULT_WORKOUT_START_DATE_TIME)))
            .andExpect(jsonPath("$.workoutEndDateTime").value(sameInstant(DEFAULT_WORKOUT_END_DATE_TIME)));
    }

    @Test
    @Transactional
    void getNonExistingWorkout() throws Exception {
        // Get the workout
        restWorkoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkout() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();

        // Update the workout
        Workout updatedWorkout = workoutRepository.findById(workout.getId()).get();
        // Disconnect from session so that the updates on updatedWorkout are not directly saved in db
        em.detach(updatedWorkout);
        updatedWorkout
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .workoutStartDateTime(UPDATED_WORKOUT_START_DATE_TIME)
            .workoutEndDateTime(UPDATED_WORKOUT_END_DATE_TIME);

        restWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkout.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkout))
            )
            .andExpect(status().isOk());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkout.getWorkoutStartDateTime()).isEqualTo(UPDATED_WORKOUT_START_DATE_TIME);
        assertThat(testWorkout.getWorkoutEndDateTime()).isEqualTo(UPDATED_WORKOUT_END_DATE_TIME);

        // Validate the Workout in Elasticsearch
        verify(mockWorkoutSearchRepository).save(testWorkout);
    }

    @Test
    @Transactional
    void putNonExistingWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workout.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workout))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Workout in Elasticsearch
        verify(mockWorkoutSearchRepository, times(0)).save(workout);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workout))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Workout in Elasticsearch
        verify(mockWorkoutSearchRepository, times(0)).save(workout);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workout))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Workout in Elasticsearch
        verify(mockWorkoutSearchRepository, times(0)).save(workout);
    }

    @Test
    @Transactional
    void partialUpdateWorkoutWithPatch() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();

        // Update the workout using partial update
        Workout partialUpdatedWorkout = new Workout();
        partialUpdatedWorkout.setId(workout.getId());

        partialUpdatedWorkout.description(UPDATED_DESCRIPTION).workoutStartDateTime(UPDATED_WORKOUT_START_DATE_TIME);

        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkout.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkout))
            )
            .andExpect(status().isOk());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkout.getWorkoutStartDateTime()).isEqualTo(UPDATED_WORKOUT_START_DATE_TIME);
        assertThat(testWorkout.getWorkoutEndDateTime()).isEqualTo(DEFAULT_WORKOUT_END_DATE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateWorkoutWithPatch() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();

        // Update the workout using partial update
        Workout partialUpdatedWorkout = new Workout();
        partialUpdatedWorkout.setId(workout.getId());

        partialUpdatedWorkout
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .workoutStartDateTime(UPDATED_WORKOUT_START_DATE_TIME)
            .workoutEndDateTime(UPDATED_WORKOUT_END_DATE_TIME);

        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkout.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkout))
            )
            .andExpect(status().isOk());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkout.getWorkoutStartDateTime()).isEqualTo(UPDATED_WORKOUT_START_DATE_TIME);
        assertThat(testWorkout.getWorkoutEndDateTime()).isEqualTo(UPDATED_WORKOUT_END_DATE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workout.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workout))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Workout in Elasticsearch
        verify(mockWorkoutSearchRepository, times(0)).save(workout);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workout))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Workout in Elasticsearch
        verify(mockWorkoutSearchRepository, times(0)).save(workout);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workout))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Workout in Elasticsearch
        verify(mockWorkoutSearchRepository, times(0)).save(workout);
    }

    @Test
    @Transactional
    void deleteWorkout() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        int databaseSizeBeforeDelete = workoutRepository.findAll().size();

        // Delete the workout
        restWorkoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, workout.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Workout in Elasticsearch
        verify(mockWorkoutSearchRepository, times(1)).deleteById(workout.getId());
    }

    @Test
    @Transactional
    void searchWorkout() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workoutRepository.saveAndFlush(workout);
        when(mockWorkoutSearchRepository.search("id:" + workout.getId())).thenReturn(Stream.of(workout));

        // Search the workout
        restWorkoutMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workout.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].workoutStartDateTime").value(hasItem(sameInstant(DEFAULT_WORKOUT_START_DATE_TIME))))
            .andExpect(jsonPath("$.[*].workoutEndDateTime").value(hasItem(sameInstant(DEFAULT_WORKOUT_END_DATE_TIME))));
    }
}
