package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.WorkoutRoutineGroup;
import com.mycompany.myapp.repository.WorkoutRoutineGroupRepository;
import com.mycompany.myapp.repository.search.WorkoutRoutineGroupSearchRepository;
import com.mycompany.myapp.service.WorkoutRoutineGroupService;
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
 * Integration tests for the {@link WorkoutRoutineGroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkoutRoutineGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/workout-routine-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/workout-routine-groups";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutRoutineGroupRepository workoutRoutineGroupRepository;

    @Mock
    private WorkoutRoutineGroupRepository workoutRoutineGroupRepositoryMock;

    @Mock
    private WorkoutRoutineGroupService workoutRoutineGroupServiceMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.WorkoutRoutineGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkoutRoutineGroupSearchRepository mockWorkoutRoutineGroupSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkoutRoutineGroupMockMvc;

    private WorkoutRoutineGroup workoutRoutineGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutRoutineGroup createEntity(EntityManager em) {
        WorkoutRoutineGroup workoutRoutineGroup = new WorkoutRoutineGroup().name(DEFAULT_NAME);
        return workoutRoutineGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutRoutineGroup createUpdatedEntity(EntityManager em) {
        WorkoutRoutineGroup workoutRoutineGroup = new WorkoutRoutineGroup().name(UPDATED_NAME);
        return workoutRoutineGroup;
    }

    @BeforeEach
    public void initTest() {
        workoutRoutineGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkoutRoutineGroup() throws Exception {
        int databaseSizeBeforeCreate = workoutRoutineGroupRepository.findAll().size();
        // Create the WorkoutRoutineGroup
        restWorkoutRoutineGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineGroup))
            )
            .andExpect(status().isCreated());

        // Validate the WorkoutRoutineGroup in the database
        List<WorkoutRoutineGroup> workoutRoutineGroupList = workoutRoutineGroupRepository.findAll();
        assertThat(workoutRoutineGroupList).hasSize(databaseSizeBeforeCreate + 1);
        WorkoutRoutineGroup testWorkoutRoutineGroup = workoutRoutineGroupList.get(workoutRoutineGroupList.size() - 1);
        assertThat(testWorkoutRoutineGroup.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the WorkoutRoutineGroup in Elasticsearch
        verify(mockWorkoutRoutineGroupSearchRepository, times(1)).save(testWorkoutRoutineGroup);
    }

    @Test
    @Transactional
    void createWorkoutRoutineGroupWithExistingId() throws Exception {
        // Create the WorkoutRoutineGroup with an existing ID
        workoutRoutineGroup.setId(1L);

        int databaseSizeBeforeCreate = workoutRoutineGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutRoutineGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineGroup in the database
        List<WorkoutRoutineGroup> workoutRoutineGroupList = workoutRoutineGroupRepository.findAll();
        assertThat(workoutRoutineGroupList).hasSize(databaseSizeBeforeCreate);

        // Validate the WorkoutRoutineGroup in Elasticsearch
        verify(mockWorkoutRoutineGroupSearchRepository, times(0)).save(workoutRoutineGroup);
    }

    @Test
    @Transactional
    void getAllWorkoutRoutineGroups() throws Exception {
        // Initialize the database
        workoutRoutineGroupRepository.saveAndFlush(workoutRoutineGroup);

        // Get all the workoutRoutineGroupList
        restWorkoutRoutineGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutRoutineGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkoutRoutineGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(workoutRoutineGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkoutRoutineGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workoutRoutineGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkoutRoutineGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(workoutRoutineGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkoutRoutineGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workoutRoutineGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getWorkoutRoutineGroup() throws Exception {
        // Initialize the database
        workoutRoutineGroupRepository.saveAndFlush(workoutRoutineGroup);

        // Get the workoutRoutineGroup
        restWorkoutRoutineGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, workoutRoutineGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workoutRoutineGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingWorkoutRoutineGroup() throws Exception {
        // Get the workoutRoutineGroup
        restWorkoutRoutineGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkoutRoutineGroup() throws Exception {
        // Initialize the database
        workoutRoutineGroupRepository.saveAndFlush(workoutRoutineGroup);

        int databaseSizeBeforeUpdate = workoutRoutineGroupRepository.findAll().size();

        // Update the workoutRoutineGroup
        WorkoutRoutineGroup updatedWorkoutRoutineGroup = workoutRoutineGroupRepository.findById(workoutRoutineGroup.getId()).get();
        // Disconnect from session so that the updates on updatedWorkoutRoutineGroup are not directly saved in db
        em.detach(updatedWorkoutRoutineGroup);
        updatedWorkoutRoutineGroup.name(UPDATED_NAME);

        restWorkoutRoutineGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkoutRoutineGroup.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkoutRoutineGroup))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutRoutineGroup in the database
        List<WorkoutRoutineGroup> workoutRoutineGroupList = workoutRoutineGroupRepository.findAll();
        assertThat(workoutRoutineGroupList).hasSize(databaseSizeBeforeUpdate);
        WorkoutRoutineGroup testWorkoutRoutineGroup = workoutRoutineGroupList.get(workoutRoutineGroupList.size() - 1);
        assertThat(testWorkoutRoutineGroup.getName()).isEqualTo(UPDATED_NAME);

        // Validate the WorkoutRoutineGroup in Elasticsearch
        verify(mockWorkoutRoutineGroupSearchRepository).save(testWorkoutRoutineGroup);
    }

    @Test
    @Transactional
    void putNonExistingWorkoutRoutineGroup() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineGroupRepository.findAll().size();
        workoutRoutineGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutRoutineGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutRoutineGroup.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineGroup in the database
        List<WorkoutRoutineGroup> workoutRoutineGroupList = workoutRoutineGroupRepository.findAll();
        assertThat(workoutRoutineGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineGroup in Elasticsearch
        verify(mockWorkoutRoutineGroupSearchRepository, times(0)).save(workoutRoutineGroup);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkoutRoutineGroup() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineGroupRepository.findAll().size();
        workoutRoutineGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineGroup in the database
        List<WorkoutRoutineGroup> workoutRoutineGroupList = workoutRoutineGroupRepository.findAll();
        assertThat(workoutRoutineGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineGroup in Elasticsearch
        verify(mockWorkoutRoutineGroupSearchRepository, times(0)).save(workoutRoutineGroup);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkoutRoutineGroup() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineGroupRepository.findAll().size();
        workoutRoutineGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineGroupMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutRoutineGroup in the database
        List<WorkoutRoutineGroup> workoutRoutineGroupList = workoutRoutineGroupRepository.findAll();
        assertThat(workoutRoutineGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineGroup in Elasticsearch
        verify(mockWorkoutRoutineGroupSearchRepository, times(0)).save(workoutRoutineGroup);
    }

    @Test
    @Transactional
    void partialUpdateWorkoutRoutineGroupWithPatch() throws Exception {
        // Initialize the database
        workoutRoutineGroupRepository.saveAndFlush(workoutRoutineGroup);

        int databaseSizeBeforeUpdate = workoutRoutineGroupRepository.findAll().size();

        // Update the workoutRoutineGroup using partial update
        WorkoutRoutineGroup partialUpdatedWorkoutRoutineGroup = new WorkoutRoutineGroup();
        partialUpdatedWorkoutRoutineGroup.setId(workoutRoutineGroup.getId());

        partialUpdatedWorkoutRoutineGroup.name(UPDATED_NAME);

        restWorkoutRoutineGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutRoutineGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutRoutineGroup))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutRoutineGroup in the database
        List<WorkoutRoutineGroup> workoutRoutineGroupList = workoutRoutineGroupRepository.findAll();
        assertThat(workoutRoutineGroupList).hasSize(databaseSizeBeforeUpdate);
        WorkoutRoutineGroup testWorkoutRoutineGroup = workoutRoutineGroupList.get(workoutRoutineGroupList.size() - 1);
        assertThat(testWorkoutRoutineGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateWorkoutRoutineGroupWithPatch() throws Exception {
        // Initialize the database
        workoutRoutineGroupRepository.saveAndFlush(workoutRoutineGroup);

        int databaseSizeBeforeUpdate = workoutRoutineGroupRepository.findAll().size();

        // Update the workoutRoutineGroup using partial update
        WorkoutRoutineGroup partialUpdatedWorkoutRoutineGroup = new WorkoutRoutineGroup();
        partialUpdatedWorkoutRoutineGroup.setId(workoutRoutineGroup.getId());

        partialUpdatedWorkoutRoutineGroup.name(UPDATED_NAME);

        restWorkoutRoutineGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutRoutineGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutRoutineGroup))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutRoutineGroup in the database
        List<WorkoutRoutineGroup> workoutRoutineGroupList = workoutRoutineGroupRepository.findAll();
        assertThat(workoutRoutineGroupList).hasSize(databaseSizeBeforeUpdate);
        WorkoutRoutineGroup testWorkoutRoutineGroup = workoutRoutineGroupList.get(workoutRoutineGroupList.size() - 1);
        assertThat(testWorkoutRoutineGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingWorkoutRoutineGroup() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineGroupRepository.findAll().size();
        workoutRoutineGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutRoutineGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workoutRoutineGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineGroup in the database
        List<WorkoutRoutineGroup> workoutRoutineGroupList = workoutRoutineGroupRepository.findAll();
        assertThat(workoutRoutineGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineGroup in Elasticsearch
        verify(mockWorkoutRoutineGroupSearchRepository, times(0)).save(workoutRoutineGroup);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkoutRoutineGroup() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineGroupRepository.findAll().size();
        workoutRoutineGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutRoutineGroup in the database
        List<WorkoutRoutineGroup> workoutRoutineGroupList = workoutRoutineGroupRepository.findAll();
        assertThat(workoutRoutineGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineGroup in Elasticsearch
        verify(mockWorkoutRoutineGroupSearchRepository, times(0)).save(workoutRoutineGroup);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkoutRoutineGroup() throws Exception {
        int databaseSizeBeforeUpdate = workoutRoutineGroupRepository.findAll().size();
        workoutRoutineGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutRoutineGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutRoutineGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutRoutineGroup in the database
        List<WorkoutRoutineGroup> workoutRoutineGroupList = workoutRoutineGroupRepository.findAll();
        assertThat(workoutRoutineGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkoutRoutineGroup in Elasticsearch
        verify(mockWorkoutRoutineGroupSearchRepository, times(0)).save(workoutRoutineGroup);
    }

    @Test
    @Transactional
    void deleteWorkoutRoutineGroup() throws Exception {
        // Initialize the database
        workoutRoutineGroupRepository.saveAndFlush(workoutRoutineGroup);

        int databaseSizeBeforeDelete = workoutRoutineGroupRepository.findAll().size();

        // Delete the workoutRoutineGroup
        restWorkoutRoutineGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, workoutRoutineGroup.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkoutRoutineGroup> workoutRoutineGroupList = workoutRoutineGroupRepository.findAll();
        assertThat(workoutRoutineGroupList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WorkoutRoutineGroup in Elasticsearch
        verify(mockWorkoutRoutineGroupSearchRepository, times(1)).deleteById(workoutRoutineGroup.getId());
    }

    @Test
    @Transactional
    void searchWorkoutRoutineGroup() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workoutRoutineGroupRepository.saveAndFlush(workoutRoutineGroup);
        when(mockWorkoutRoutineGroupSearchRepository.search("id:" + workoutRoutineGroup.getId()))
            .thenReturn(Stream.of(workoutRoutineGroup));

        // Search the workoutRoutineGroup
        restWorkoutRoutineGroupMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workoutRoutineGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutRoutineGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
