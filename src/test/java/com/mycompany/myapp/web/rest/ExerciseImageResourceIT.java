package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ExerciseImage;
import com.mycompany.myapp.repository.ExerciseImageRepository;
import com.mycompany.myapp.repository.search.ExerciseImageSearchRepository;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ExerciseImageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExerciseImageResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_MAIN = false;
    private static final Boolean UPDATED_MAIN = true;

    private static final String ENTITY_API_URL = "/api/exercise-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/exercise-images";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExerciseImageRepository exerciseImageRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.ExerciseImageSearchRepositoryMockConfiguration
     */
    @Autowired
    private ExerciseImageSearchRepository mockExerciseImageSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExerciseImageMockMvc;

    private ExerciseImage exerciseImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseImage createEntity(EntityManager em) {
        ExerciseImage exerciseImage = new ExerciseImage()
            .uuid(DEFAULT_UUID)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .main(DEFAULT_MAIN);
        return exerciseImage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseImage createUpdatedEntity(EntityManager em) {
        ExerciseImage exerciseImage = new ExerciseImage()
            .uuid(UPDATED_UUID)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .main(UPDATED_MAIN);
        return exerciseImage;
    }

    @BeforeEach
    public void initTest() {
        exerciseImage = createEntity(em);
    }

    @Test
    @Transactional
    void createExerciseImage() throws Exception {
        int databaseSizeBeforeCreate = exerciseImageRepository.findAll().size();
        // Create the ExerciseImage
        restExerciseImageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseImage))
            )
            .andExpect(status().isCreated());

        // Validate the ExerciseImage in the database
        List<ExerciseImage> exerciseImageList = exerciseImageRepository.findAll();
        assertThat(exerciseImageList).hasSize(databaseSizeBeforeCreate + 1);
        ExerciseImage testExerciseImage = exerciseImageList.get(exerciseImageList.size() - 1);
        assertThat(testExerciseImage.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testExerciseImage.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testExerciseImage.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testExerciseImage.getMain()).isEqualTo(DEFAULT_MAIN);

        // Validate the ExerciseImage in Elasticsearch
        verify(mockExerciseImageSearchRepository, times(1)).save(testExerciseImage);
    }

    @Test
    @Transactional
    void createExerciseImageWithExistingId() throws Exception {
        // Create the ExerciseImage with an existing ID
        exerciseImage.setId(1L);

        int databaseSizeBeforeCreate = exerciseImageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseImageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseImage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseImage in the database
        List<ExerciseImage> exerciseImageList = exerciseImageRepository.findAll();
        assertThat(exerciseImageList).hasSize(databaseSizeBeforeCreate);

        // Validate the ExerciseImage in Elasticsearch
        verify(mockExerciseImageSearchRepository, times(0)).save(exerciseImage);
    }

    @Test
    @Transactional
    void getAllExerciseImages() throws Exception {
        // Initialize the database
        exerciseImageRepository.saveAndFlush(exerciseImage);

        // Get all the exerciseImageList
        restExerciseImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].main").value(hasItem(DEFAULT_MAIN.booleanValue())));
    }

    @Test
    @Transactional
    void getExerciseImage() throws Exception {
        // Initialize the database
        exerciseImageRepository.saveAndFlush(exerciseImage);

        // Get the exerciseImage
        restExerciseImageMockMvc
            .perform(get(ENTITY_API_URL_ID, exerciseImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exerciseImage.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.main").value(DEFAULT_MAIN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingExerciseImage() throws Exception {
        // Get the exerciseImage
        restExerciseImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExerciseImage() throws Exception {
        // Initialize the database
        exerciseImageRepository.saveAndFlush(exerciseImage);

        int databaseSizeBeforeUpdate = exerciseImageRepository.findAll().size();

        // Update the exerciseImage
        ExerciseImage updatedExerciseImage = exerciseImageRepository.findById(exerciseImage.getId()).get();
        // Disconnect from session so that the updates on updatedExerciseImage are not directly saved in db
        em.detach(updatedExerciseImage);
        updatedExerciseImage.uuid(UPDATED_UUID).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE).main(UPDATED_MAIN);

        restExerciseImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExerciseImage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExerciseImage))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseImage in the database
        List<ExerciseImage> exerciseImageList = exerciseImageRepository.findAll();
        assertThat(exerciseImageList).hasSize(databaseSizeBeforeUpdate);
        ExerciseImage testExerciseImage = exerciseImageList.get(exerciseImageList.size() - 1);
        assertThat(testExerciseImage.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testExerciseImage.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExerciseImage.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExerciseImage.getMain()).isEqualTo(UPDATED_MAIN);

        // Validate the ExerciseImage in Elasticsearch
        verify(mockExerciseImageSearchRepository).save(testExerciseImage);
    }

    @Test
    @Transactional
    void putNonExistingExerciseImage() throws Exception {
        int databaseSizeBeforeUpdate = exerciseImageRepository.findAll().size();
        exerciseImage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseImage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseImage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseImage in the database
        List<ExerciseImage> exerciseImageList = exerciseImageRepository.findAll();
        assertThat(exerciseImageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExerciseImage in Elasticsearch
        verify(mockExerciseImageSearchRepository, times(0)).save(exerciseImage);
    }

    @Test
    @Transactional
    void putWithIdMismatchExerciseImage() throws Exception {
        int databaseSizeBeforeUpdate = exerciseImageRepository.findAll().size();
        exerciseImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseImage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseImage in the database
        List<ExerciseImage> exerciseImageList = exerciseImageRepository.findAll();
        assertThat(exerciseImageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExerciseImage in Elasticsearch
        verify(mockExerciseImageSearchRepository, times(0)).save(exerciseImage);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExerciseImage() throws Exception {
        int databaseSizeBeforeUpdate = exerciseImageRepository.findAll().size();
        exerciseImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseImageMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseImage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseImage in the database
        List<ExerciseImage> exerciseImageList = exerciseImageRepository.findAll();
        assertThat(exerciseImageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExerciseImage in Elasticsearch
        verify(mockExerciseImageSearchRepository, times(0)).save(exerciseImage);
    }

    @Test
    @Transactional
    void partialUpdateExerciseImageWithPatch() throws Exception {
        // Initialize the database
        exerciseImageRepository.saveAndFlush(exerciseImage);

        int databaseSizeBeforeUpdate = exerciseImageRepository.findAll().size();

        // Update the exerciseImage using partial update
        ExerciseImage partialUpdatedExerciseImage = new ExerciseImage();
        partialUpdatedExerciseImage.setId(exerciseImage.getId());

        restExerciseImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseImage))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseImage in the database
        List<ExerciseImage> exerciseImageList = exerciseImageRepository.findAll();
        assertThat(exerciseImageList).hasSize(databaseSizeBeforeUpdate);
        ExerciseImage testExerciseImage = exerciseImageList.get(exerciseImageList.size() - 1);
        assertThat(testExerciseImage.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testExerciseImage.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testExerciseImage.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testExerciseImage.getMain()).isEqualTo(DEFAULT_MAIN);
    }

    @Test
    @Transactional
    void fullUpdateExerciseImageWithPatch() throws Exception {
        // Initialize the database
        exerciseImageRepository.saveAndFlush(exerciseImage);

        int databaseSizeBeforeUpdate = exerciseImageRepository.findAll().size();

        // Update the exerciseImage using partial update
        ExerciseImage partialUpdatedExerciseImage = new ExerciseImage();
        partialUpdatedExerciseImage.setId(exerciseImage.getId());

        partialUpdatedExerciseImage.uuid(UPDATED_UUID).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE).main(UPDATED_MAIN);

        restExerciseImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseImage))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseImage in the database
        List<ExerciseImage> exerciseImageList = exerciseImageRepository.findAll();
        assertThat(exerciseImageList).hasSize(databaseSizeBeforeUpdate);
        ExerciseImage testExerciseImage = exerciseImageList.get(exerciseImageList.size() - 1);
        assertThat(testExerciseImage.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testExerciseImage.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExerciseImage.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExerciseImage.getMain()).isEqualTo(UPDATED_MAIN);
    }

    @Test
    @Transactional
    void patchNonExistingExerciseImage() throws Exception {
        int databaseSizeBeforeUpdate = exerciseImageRepository.findAll().size();
        exerciseImage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exerciseImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseImage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseImage in the database
        List<ExerciseImage> exerciseImageList = exerciseImageRepository.findAll();
        assertThat(exerciseImageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExerciseImage in Elasticsearch
        verify(mockExerciseImageSearchRepository, times(0)).save(exerciseImage);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExerciseImage() throws Exception {
        int databaseSizeBeforeUpdate = exerciseImageRepository.findAll().size();
        exerciseImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseImage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseImage in the database
        List<ExerciseImage> exerciseImageList = exerciseImageRepository.findAll();
        assertThat(exerciseImageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExerciseImage in Elasticsearch
        verify(mockExerciseImageSearchRepository, times(0)).save(exerciseImage);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExerciseImage() throws Exception {
        int databaseSizeBeforeUpdate = exerciseImageRepository.findAll().size();
        exerciseImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseImageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseImage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseImage in the database
        List<ExerciseImage> exerciseImageList = exerciseImageRepository.findAll();
        assertThat(exerciseImageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExerciseImage in Elasticsearch
        verify(mockExerciseImageSearchRepository, times(0)).save(exerciseImage);
    }

    @Test
    @Transactional
    void deleteExerciseImage() throws Exception {
        // Initialize the database
        exerciseImageRepository.saveAndFlush(exerciseImage);

        int databaseSizeBeforeDelete = exerciseImageRepository.findAll().size();

        // Delete the exerciseImage
        restExerciseImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, exerciseImage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExerciseImage> exerciseImageList = exerciseImageRepository.findAll();
        assertThat(exerciseImageList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ExerciseImage in Elasticsearch
        verify(mockExerciseImageSearchRepository, times(1)).deleteById(exerciseImage.getId());
    }

    @Test
    @Transactional
    void searchExerciseImage() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        exerciseImageRepository.saveAndFlush(exerciseImage);
        when(mockExerciseImageSearchRepository.search("id:" + exerciseImage.getId())).thenReturn(Stream.of(exerciseImage));

        // Search the exerciseImage
        restExerciseImageMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + exerciseImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].main").value(hasItem(DEFAULT_MAIN.booleanValue())));
    }
}
