package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProgressPhoto;
import com.mycompany.myapp.repository.ProgressPhotoRepository;
import com.mycompany.myapp.repository.search.ProgressPhotoSearchRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ProgressPhotoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProgressPhotoResourceIT {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_WEIGHT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_WEIGHT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/progress-photos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/progress-photos";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProgressPhotoRepository progressPhotoRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.ProgressPhotoSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProgressPhotoSearchRepository mockProgressPhotoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgressPhotoMockMvc;

    private ProgressPhoto progressPhoto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgressPhoto createEntity(EntityManager em) {
        ProgressPhoto progressPhoto = new ProgressPhoto()
            .note(DEFAULT_NOTE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .weightDate(DEFAULT_WEIGHT_DATE);
        return progressPhoto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgressPhoto createUpdatedEntity(EntityManager em) {
        ProgressPhoto progressPhoto = new ProgressPhoto()
            .note(UPDATED_NOTE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .weightDate(UPDATED_WEIGHT_DATE);
        return progressPhoto;
    }

    @BeforeEach
    public void initTest() {
        progressPhoto = createEntity(em);
    }

    @Test
    @Transactional
    void createProgressPhoto() throws Exception {
        int databaseSizeBeforeCreate = progressPhotoRepository.findAll().size();
        // Create the ProgressPhoto
        restProgressPhotoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(progressPhoto))
            )
            .andExpect(status().isCreated());

        // Validate the ProgressPhoto in the database
        List<ProgressPhoto> progressPhotoList = progressPhotoRepository.findAll();
        assertThat(progressPhotoList).hasSize(databaseSizeBeforeCreate + 1);
        ProgressPhoto testProgressPhoto = progressPhotoList.get(progressPhotoList.size() - 1);
        assertThat(testProgressPhoto.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testProgressPhoto.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProgressPhoto.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testProgressPhoto.getWeightDate()).isEqualTo(DEFAULT_WEIGHT_DATE);

        // Validate the ProgressPhoto in Elasticsearch
        verify(mockProgressPhotoSearchRepository, times(1)).save(testProgressPhoto);
    }

    @Test
    @Transactional
    void createProgressPhotoWithExistingId() throws Exception {
        // Create the ProgressPhoto with an existing ID
        progressPhoto.setId(1L);

        int databaseSizeBeforeCreate = progressPhotoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgressPhotoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(progressPhoto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgressPhoto in the database
        List<ProgressPhoto> progressPhotoList = progressPhotoRepository.findAll();
        assertThat(progressPhotoList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProgressPhoto in Elasticsearch
        verify(mockProgressPhotoSearchRepository, times(0)).save(progressPhoto);
    }

    @Test
    @Transactional
    void getAllProgressPhotos() throws Exception {
        // Initialize the database
        progressPhotoRepository.saveAndFlush(progressPhoto);

        // Get all the progressPhotoList
        restProgressPhotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(progressPhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].weightDate").value(hasItem(sameInstant(DEFAULT_WEIGHT_DATE))));
    }

    @Test
    @Transactional
    void getProgressPhoto() throws Exception {
        // Initialize the database
        progressPhotoRepository.saveAndFlush(progressPhoto);

        // Get the progressPhoto
        restProgressPhotoMockMvc
            .perform(get(ENTITY_API_URL_ID, progressPhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(progressPhoto.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.weightDate").value(sameInstant(DEFAULT_WEIGHT_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingProgressPhoto() throws Exception {
        // Get the progressPhoto
        restProgressPhotoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProgressPhoto() throws Exception {
        // Initialize the database
        progressPhotoRepository.saveAndFlush(progressPhoto);

        int databaseSizeBeforeUpdate = progressPhotoRepository.findAll().size();

        // Update the progressPhoto
        ProgressPhoto updatedProgressPhoto = progressPhotoRepository.findById(progressPhoto.getId()).get();
        // Disconnect from session so that the updates on updatedProgressPhoto are not directly saved in db
        em.detach(updatedProgressPhoto);
        updatedProgressPhoto
            .note(UPDATED_NOTE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .weightDate(UPDATED_WEIGHT_DATE);

        restProgressPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProgressPhoto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProgressPhoto))
            )
            .andExpect(status().isOk());

        // Validate the ProgressPhoto in the database
        List<ProgressPhoto> progressPhotoList = progressPhotoRepository.findAll();
        assertThat(progressPhotoList).hasSize(databaseSizeBeforeUpdate);
        ProgressPhoto testProgressPhoto = progressPhotoList.get(progressPhotoList.size() - 1);
        assertThat(testProgressPhoto.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testProgressPhoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProgressPhoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProgressPhoto.getWeightDate()).isEqualTo(UPDATED_WEIGHT_DATE);

        // Validate the ProgressPhoto in Elasticsearch
        verify(mockProgressPhotoSearchRepository).save(testProgressPhoto);
    }

    @Test
    @Transactional
    void putNonExistingProgressPhoto() throws Exception {
        int databaseSizeBeforeUpdate = progressPhotoRepository.findAll().size();
        progressPhoto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgressPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, progressPhoto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(progressPhoto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgressPhoto in the database
        List<ProgressPhoto> progressPhotoList = progressPhotoRepository.findAll();
        assertThat(progressPhotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProgressPhoto in Elasticsearch
        verify(mockProgressPhotoSearchRepository, times(0)).save(progressPhoto);
    }

    @Test
    @Transactional
    void putWithIdMismatchProgressPhoto() throws Exception {
        int databaseSizeBeforeUpdate = progressPhotoRepository.findAll().size();
        progressPhoto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgressPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(progressPhoto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgressPhoto in the database
        List<ProgressPhoto> progressPhotoList = progressPhotoRepository.findAll();
        assertThat(progressPhotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProgressPhoto in Elasticsearch
        verify(mockProgressPhotoSearchRepository, times(0)).save(progressPhoto);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProgressPhoto() throws Exception {
        int databaseSizeBeforeUpdate = progressPhotoRepository.findAll().size();
        progressPhoto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgressPhotoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(progressPhoto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgressPhoto in the database
        List<ProgressPhoto> progressPhotoList = progressPhotoRepository.findAll();
        assertThat(progressPhotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProgressPhoto in Elasticsearch
        verify(mockProgressPhotoSearchRepository, times(0)).save(progressPhoto);
    }

    @Test
    @Transactional
    void partialUpdateProgressPhotoWithPatch() throws Exception {
        // Initialize the database
        progressPhotoRepository.saveAndFlush(progressPhoto);

        int databaseSizeBeforeUpdate = progressPhotoRepository.findAll().size();

        // Update the progressPhoto using partial update
        ProgressPhoto partialUpdatedProgressPhoto = new ProgressPhoto();
        partialUpdatedProgressPhoto.setId(progressPhoto.getId());

        partialUpdatedProgressPhoto.image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE).weightDate(UPDATED_WEIGHT_DATE);

        restProgressPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgressPhoto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgressPhoto))
            )
            .andExpect(status().isOk());

        // Validate the ProgressPhoto in the database
        List<ProgressPhoto> progressPhotoList = progressPhotoRepository.findAll();
        assertThat(progressPhotoList).hasSize(databaseSizeBeforeUpdate);
        ProgressPhoto testProgressPhoto = progressPhotoList.get(progressPhotoList.size() - 1);
        assertThat(testProgressPhoto.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testProgressPhoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProgressPhoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProgressPhoto.getWeightDate()).isEqualTo(UPDATED_WEIGHT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateProgressPhotoWithPatch() throws Exception {
        // Initialize the database
        progressPhotoRepository.saveAndFlush(progressPhoto);

        int databaseSizeBeforeUpdate = progressPhotoRepository.findAll().size();

        // Update the progressPhoto using partial update
        ProgressPhoto partialUpdatedProgressPhoto = new ProgressPhoto();
        partialUpdatedProgressPhoto.setId(progressPhoto.getId());

        partialUpdatedProgressPhoto
            .note(UPDATED_NOTE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .weightDate(UPDATED_WEIGHT_DATE);

        restProgressPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgressPhoto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgressPhoto))
            )
            .andExpect(status().isOk());

        // Validate the ProgressPhoto in the database
        List<ProgressPhoto> progressPhotoList = progressPhotoRepository.findAll();
        assertThat(progressPhotoList).hasSize(databaseSizeBeforeUpdate);
        ProgressPhoto testProgressPhoto = progressPhotoList.get(progressPhotoList.size() - 1);
        assertThat(testProgressPhoto.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testProgressPhoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProgressPhoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProgressPhoto.getWeightDate()).isEqualTo(UPDATED_WEIGHT_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingProgressPhoto() throws Exception {
        int databaseSizeBeforeUpdate = progressPhotoRepository.findAll().size();
        progressPhoto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgressPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, progressPhoto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(progressPhoto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgressPhoto in the database
        List<ProgressPhoto> progressPhotoList = progressPhotoRepository.findAll();
        assertThat(progressPhotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProgressPhoto in Elasticsearch
        verify(mockProgressPhotoSearchRepository, times(0)).save(progressPhoto);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProgressPhoto() throws Exception {
        int databaseSizeBeforeUpdate = progressPhotoRepository.findAll().size();
        progressPhoto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgressPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(progressPhoto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgressPhoto in the database
        List<ProgressPhoto> progressPhotoList = progressPhotoRepository.findAll();
        assertThat(progressPhotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProgressPhoto in Elasticsearch
        verify(mockProgressPhotoSearchRepository, times(0)).save(progressPhoto);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProgressPhoto() throws Exception {
        int databaseSizeBeforeUpdate = progressPhotoRepository.findAll().size();
        progressPhoto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgressPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(progressPhoto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgressPhoto in the database
        List<ProgressPhoto> progressPhotoList = progressPhotoRepository.findAll();
        assertThat(progressPhotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProgressPhoto in Elasticsearch
        verify(mockProgressPhotoSearchRepository, times(0)).save(progressPhoto);
    }

    @Test
    @Transactional
    void deleteProgressPhoto() throws Exception {
        // Initialize the database
        progressPhotoRepository.saveAndFlush(progressPhoto);

        int databaseSizeBeforeDelete = progressPhotoRepository.findAll().size();

        // Delete the progressPhoto
        restProgressPhotoMockMvc
            .perform(delete(ENTITY_API_URL_ID, progressPhoto.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProgressPhoto> progressPhotoList = progressPhotoRepository.findAll();
        assertThat(progressPhotoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProgressPhoto in Elasticsearch
        verify(mockProgressPhotoSearchRepository, times(1)).deleteById(progressPhoto.getId());
    }

    @Test
    @Transactional
    void searchProgressPhoto() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        progressPhotoRepository.saveAndFlush(progressPhoto);
        when(mockProgressPhotoSearchRepository.search("id:" + progressPhoto.getId())).thenReturn(Stream.of(progressPhoto));

        // Search the progressPhoto
        restProgressPhotoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + progressPhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(progressPhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].weightDate").value(hasItem(sameInstant(DEFAULT_WEIGHT_DATE))));
    }
}
