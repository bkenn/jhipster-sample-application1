package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.ProgressPhoto;
import com.mycompany.myapp.repository.ProgressPhotoRepository;
import com.mycompany.myapp.service.ProgressPhotoService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ProgressPhoto}.
 */
@RestController
@RequestMapping("/api")
public class ProgressPhotoResource {

    private final Logger log = LoggerFactory.getLogger(ProgressPhotoResource.class);

    private static final String ENTITY_NAME = "progressPhoto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgressPhotoService progressPhotoService;

    private final ProgressPhotoRepository progressPhotoRepository;

    public ProgressPhotoResource(ProgressPhotoService progressPhotoService, ProgressPhotoRepository progressPhotoRepository) {
        this.progressPhotoService = progressPhotoService;
        this.progressPhotoRepository = progressPhotoRepository;
    }

    /**
     * {@code POST  /progress-photos} : Create a new progressPhoto.
     *
     * @param progressPhoto the progressPhoto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new progressPhoto, or with status {@code 400 (Bad Request)} if the progressPhoto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/progress-photos")
    public ResponseEntity<ProgressPhoto> createProgressPhoto(@RequestBody ProgressPhoto progressPhoto) throws URISyntaxException {
        log.debug("REST request to save ProgressPhoto : {}", progressPhoto);
        if (progressPhoto.getId() != null) {
            throw new BadRequestAlertException("A new progressPhoto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProgressPhoto result = progressPhotoService.save(progressPhoto);
        return ResponseEntity
            .created(new URI("/api/progress-photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /progress-photos/:id} : Updates an existing progressPhoto.
     *
     * @param id the id of the progressPhoto to save.
     * @param progressPhoto the progressPhoto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated progressPhoto,
     * or with status {@code 400 (Bad Request)} if the progressPhoto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the progressPhoto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/progress-photos/{id}")
    public ResponseEntity<ProgressPhoto> updateProgressPhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProgressPhoto progressPhoto
    ) throws URISyntaxException {
        log.debug("REST request to update ProgressPhoto : {}, {}", id, progressPhoto);
        if (progressPhoto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, progressPhoto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!progressPhotoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProgressPhoto result = progressPhotoService.save(progressPhoto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, progressPhoto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /progress-photos/:id} : Partial updates given fields of an existing progressPhoto, field will ignore if it is null
     *
     * @param id the id of the progressPhoto to save.
     * @param progressPhoto the progressPhoto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated progressPhoto,
     * or with status {@code 400 (Bad Request)} if the progressPhoto is not valid,
     * or with status {@code 404 (Not Found)} if the progressPhoto is not found,
     * or with status {@code 500 (Internal Server Error)} if the progressPhoto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/progress-photos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProgressPhoto> partialUpdateProgressPhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProgressPhoto progressPhoto
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProgressPhoto partially : {}, {}", id, progressPhoto);
        if (progressPhoto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, progressPhoto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!progressPhotoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProgressPhoto> result = progressPhotoService.partialUpdate(progressPhoto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, progressPhoto.getId().toString())
        );
    }

    /**
     * {@code GET  /progress-photos} : get all the progressPhotos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of progressPhotos in body.
     */
    @GetMapping("/progress-photos")
    public List<ProgressPhoto> getAllProgressPhotos() {
        log.debug("REST request to get all ProgressPhotos");
        return progressPhotoService.findAll();
    }

    /**
     * {@code GET  /progress-photos/:id} : get the "id" progressPhoto.
     *
     * @param id the id of the progressPhoto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the progressPhoto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/progress-photos/{id}")
    public ResponseEntity<ProgressPhoto> getProgressPhoto(@PathVariable Long id) {
        log.debug("REST request to get ProgressPhoto : {}", id);
        Optional<ProgressPhoto> progressPhoto = progressPhotoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(progressPhoto);
    }

    /**
     * {@code DELETE  /progress-photos/:id} : delete the "id" progressPhoto.
     *
     * @param id the id of the progressPhoto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/progress-photos/{id}")
    public ResponseEntity<Void> deleteProgressPhoto(@PathVariable Long id) {
        log.debug("REST request to delete ProgressPhoto : {}", id);
        progressPhotoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/progress-photos?query=:query} : search for the progressPhoto corresponding
     * to the query.
     *
     * @param query the query of the progressPhoto search.
     * @return the result of the search.
     */
    @GetMapping("/_search/progress-photos")
    public List<ProgressPhoto> searchProgressPhotos(@RequestParam String query) {
        log.debug("REST request to search ProgressPhotos for query {}", query);
        return progressPhotoService.search(query);
    }
}
