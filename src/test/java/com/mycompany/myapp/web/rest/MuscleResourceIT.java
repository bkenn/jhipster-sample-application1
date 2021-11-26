package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Muscle;
import com.mycompany.myapp.repository.MuscleRepository;
import com.mycompany.myapp.repository.search.MuscleSearchRepository;
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
 * Integration tests for the {@link MuscleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MuscleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MUSCLE_ORDER = 1;
    private static final Integer UPDATED_MUSCLE_ORDER = 2;

    private static final String DEFAULT_IMAGE_URL_MAIN = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL_MAIN = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL_SECONDARY = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL_SECONDARY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FRONT = false;
    private static final Boolean UPDATED_FRONT = true;

    private static final String ENTITY_API_URL = "/api/muscles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/muscles";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MuscleRepository muscleRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.MuscleSearchRepositoryMockConfiguration
     */
    @Autowired
    private MuscleSearchRepository mockMuscleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMuscleMockMvc;

    private Muscle muscle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Muscle createEntity(EntityManager em) {
        Muscle muscle = new Muscle()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .muscleOrder(DEFAULT_MUSCLE_ORDER)
            .imageUrlMain(DEFAULT_IMAGE_URL_MAIN)
            .imageUrlSecondary(DEFAULT_IMAGE_URL_SECONDARY)
            .front(DEFAULT_FRONT);
        return muscle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Muscle createUpdatedEntity(EntityManager em) {
        Muscle muscle = new Muscle()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .muscleOrder(UPDATED_MUSCLE_ORDER)
            .imageUrlMain(UPDATED_IMAGE_URL_MAIN)
            .imageUrlSecondary(UPDATED_IMAGE_URL_SECONDARY)
            .front(UPDATED_FRONT);
        return muscle;
    }

    @BeforeEach
    public void initTest() {
        muscle = createEntity(em);
    }

    @Test
    @Transactional
    void createMuscle() throws Exception {
        int databaseSizeBeforeCreate = muscleRepository.findAll().size();
        // Create the Muscle
        restMuscleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(muscle))
            )
            .andExpect(status().isCreated());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeCreate + 1);
        Muscle testMuscle = muscleList.get(muscleList.size() - 1);
        assertThat(testMuscle.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMuscle.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMuscle.getMuscleOrder()).isEqualTo(DEFAULT_MUSCLE_ORDER);
        assertThat(testMuscle.getImageUrlMain()).isEqualTo(DEFAULT_IMAGE_URL_MAIN);
        assertThat(testMuscle.getImageUrlSecondary()).isEqualTo(DEFAULT_IMAGE_URL_SECONDARY);
        assertThat(testMuscle.getFront()).isEqualTo(DEFAULT_FRONT);

        // Validate the Muscle in Elasticsearch
        verify(mockMuscleSearchRepository, times(1)).save(testMuscle);
    }

    @Test
    @Transactional
    void createMuscleWithExistingId() throws Exception {
        // Create the Muscle with an existing ID
        muscle.setId(1L);

        int databaseSizeBeforeCreate = muscleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMuscleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(muscle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeCreate);

        // Validate the Muscle in Elasticsearch
        verify(mockMuscleSearchRepository, times(0)).save(muscle);
    }

    @Test
    @Transactional
    void getAllMuscles() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList
        restMuscleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(muscle.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].muscleOrder").value(hasItem(DEFAULT_MUSCLE_ORDER)))
            .andExpect(jsonPath("$.[*].imageUrlMain").value(hasItem(DEFAULT_IMAGE_URL_MAIN)))
            .andExpect(jsonPath("$.[*].imageUrlSecondary").value(hasItem(DEFAULT_IMAGE_URL_SECONDARY)))
            .andExpect(jsonPath("$.[*].front").value(hasItem(DEFAULT_FRONT.booleanValue())));
    }

    @Test
    @Transactional
    void getMuscle() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get the muscle
        restMuscleMockMvc
            .perform(get(ENTITY_API_URL_ID, muscle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(muscle.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.muscleOrder").value(DEFAULT_MUSCLE_ORDER))
            .andExpect(jsonPath("$.imageUrlMain").value(DEFAULT_IMAGE_URL_MAIN))
            .andExpect(jsonPath("$.imageUrlSecondary").value(DEFAULT_IMAGE_URL_SECONDARY))
            .andExpect(jsonPath("$.front").value(DEFAULT_FRONT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMuscle() throws Exception {
        // Get the muscle
        restMuscleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMuscle() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();

        // Update the muscle
        Muscle updatedMuscle = muscleRepository.findById(muscle.getId()).get();
        // Disconnect from session so that the updates on updatedMuscle are not directly saved in db
        em.detach(updatedMuscle);
        updatedMuscle
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .muscleOrder(UPDATED_MUSCLE_ORDER)
            .imageUrlMain(UPDATED_IMAGE_URL_MAIN)
            .imageUrlSecondary(UPDATED_IMAGE_URL_SECONDARY)
            .front(UPDATED_FRONT);

        restMuscleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMuscle.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMuscle))
            )
            .andExpect(status().isOk());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);
        Muscle testMuscle = muscleList.get(muscleList.size() - 1);
        assertThat(testMuscle.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMuscle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMuscle.getMuscleOrder()).isEqualTo(UPDATED_MUSCLE_ORDER);
        assertThat(testMuscle.getImageUrlMain()).isEqualTo(UPDATED_IMAGE_URL_MAIN);
        assertThat(testMuscle.getImageUrlSecondary()).isEqualTo(UPDATED_IMAGE_URL_SECONDARY);
        assertThat(testMuscle.getFront()).isEqualTo(UPDATED_FRONT);

        // Validate the Muscle in Elasticsearch
        verify(mockMuscleSearchRepository).save(testMuscle);
    }

    @Test
    @Transactional
    void putNonExistingMuscle() throws Exception {
        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();
        muscle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMuscleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, muscle.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(muscle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Muscle in Elasticsearch
        verify(mockMuscleSearchRepository, times(0)).save(muscle);
    }

    @Test
    @Transactional
    void putWithIdMismatchMuscle() throws Exception {
        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();
        muscle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMuscleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(muscle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Muscle in Elasticsearch
        verify(mockMuscleSearchRepository, times(0)).save(muscle);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMuscle() throws Exception {
        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();
        muscle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMuscleMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(muscle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Muscle in Elasticsearch
        verify(mockMuscleSearchRepository, times(0)).save(muscle);
    }

    @Test
    @Transactional
    void partialUpdateMuscleWithPatch() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();

        // Update the muscle using partial update
        Muscle partialUpdatedMuscle = new Muscle();
        partialUpdatedMuscle.setId(muscle.getId());

        partialUpdatedMuscle.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).imageUrlSecondary(UPDATED_IMAGE_URL_SECONDARY);

        restMuscleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMuscle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMuscle))
            )
            .andExpect(status().isOk());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);
        Muscle testMuscle = muscleList.get(muscleList.size() - 1);
        assertThat(testMuscle.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMuscle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMuscle.getMuscleOrder()).isEqualTo(DEFAULT_MUSCLE_ORDER);
        assertThat(testMuscle.getImageUrlMain()).isEqualTo(DEFAULT_IMAGE_URL_MAIN);
        assertThat(testMuscle.getImageUrlSecondary()).isEqualTo(UPDATED_IMAGE_URL_SECONDARY);
        assertThat(testMuscle.getFront()).isEqualTo(DEFAULT_FRONT);
    }

    @Test
    @Transactional
    void fullUpdateMuscleWithPatch() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();

        // Update the muscle using partial update
        Muscle partialUpdatedMuscle = new Muscle();
        partialUpdatedMuscle.setId(muscle.getId());

        partialUpdatedMuscle
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .muscleOrder(UPDATED_MUSCLE_ORDER)
            .imageUrlMain(UPDATED_IMAGE_URL_MAIN)
            .imageUrlSecondary(UPDATED_IMAGE_URL_SECONDARY)
            .front(UPDATED_FRONT);

        restMuscleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMuscle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMuscle))
            )
            .andExpect(status().isOk());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);
        Muscle testMuscle = muscleList.get(muscleList.size() - 1);
        assertThat(testMuscle.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMuscle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMuscle.getMuscleOrder()).isEqualTo(UPDATED_MUSCLE_ORDER);
        assertThat(testMuscle.getImageUrlMain()).isEqualTo(UPDATED_IMAGE_URL_MAIN);
        assertThat(testMuscle.getImageUrlSecondary()).isEqualTo(UPDATED_IMAGE_URL_SECONDARY);
        assertThat(testMuscle.getFront()).isEqualTo(UPDATED_FRONT);
    }

    @Test
    @Transactional
    void patchNonExistingMuscle() throws Exception {
        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();
        muscle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMuscleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, muscle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(muscle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Muscle in Elasticsearch
        verify(mockMuscleSearchRepository, times(0)).save(muscle);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMuscle() throws Exception {
        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();
        muscle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMuscleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(muscle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Muscle in Elasticsearch
        verify(mockMuscleSearchRepository, times(0)).save(muscle);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMuscle() throws Exception {
        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();
        muscle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMuscleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(muscle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Muscle in Elasticsearch
        verify(mockMuscleSearchRepository, times(0)).save(muscle);
    }

    @Test
    @Transactional
    void deleteMuscle() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        int databaseSizeBeforeDelete = muscleRepository.findAll().size();

        // Delete the muscle
        restMuscleMockMvc
            .perform(delete(ENTITY_API_URL_ID, muscle.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Muscle in Elasticsearch
        verify(mockMuscleSearchRepository, times(1)).deleteById(muscle.getId());
    }

    @Test
    @Transactional
    void searchMuscle() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);
        when(mockMuscleSearchRepository.search("id:" + muscle.getId())).thenReturn(Stream.of(muscle));

        // Search the muscle
        restMuscleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + muscle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(muscle.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].muscleOrder").value(hasItem(DEFAULT_MUSCLE_ORDER)))
            .andExpect(jsonPath("$.[*].imageUrlMain").value(hasItem(DEFAULT_IMAGE_URL_MAIN)))
            .andExpect(jsonPath("$.[*].imageUrlSecondary").value(hasItem(DEFAULT_IMAGE_URL_SECONDARY)))
            .andExpect(jsonPath("$.[*].front").value(hasItem(DEFAULT_FRONT.booleanValue())));
    }
}
