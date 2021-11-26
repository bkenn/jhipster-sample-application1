package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.ExerciseImage;
import com.mycompany.myapp.repository.ExerciseImageRepository;
import com.mycompany.myapp.service.ExerciseImageService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ExerciseImage}.
 */
@RestController
@RequestMapping("/api")
public class ExerciseImageResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseImageResource.class);

    private static final String ENTITY_NAME = "exerciseImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciseImageService exerciseImageService;

    private final ExerciseImageRepository exerciseImageRepository;

    public ExerciseImageResource(ExerciseImageService exerciseImageService, ExerciseImageRepository exerciseImageRepository) {
        this.exerciseImageService = exerciseImageService;
        this.exerciseImageRepository = exerciseImageRepository;
    }

    /**
     * {@code POST  /exercise-images} : Create a new exerciseImage.
     *
     * @param exerciseImage the exerciseImage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exerciseImage, or with status {@code 400 (Bad Request)} if the exerciseImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exercise-images")
    public ResponseEntity<ExerciseImage> createExerciseImage(@RequestBody ExerciseImage exerciseImage) throws URISyntaxException {
        log.debug("REST request to save ExerciseImage : {}", exerciseImage);
        if (exerciseImage.getId() != null) {
            throw new BadRequestAlertException("A new exerciseImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExerciseImage result = exerciseImageService.save(exerciseImage);
        return ResponseEntity
            .created(new URI("/api/exercise-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercise-images/:id} : Updates an existing exerciseImage.
     *
     * @param id the id of the exerciseImage to save.
     * @param exerciseImage the exerciseImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseImage,
     * or with status {@code 400 (Bad Request)} if the exerciseImage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exerciseImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exercise-images/{id}")
    public ResponseEntity<ExerciseImage> updateExerciseImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExerciseImage exerciseImage
    ) throws URISyntaxException {
        log.debug("REST request to update ExerciseImage : {}, {}", id, exerciseImage);
        if (exerciseImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExerciseImage result = exerciseImageService.save(exerciseImage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciseImage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exercise-images/:id} : Partial updates given fields of an existing exerciseImage, field will ignore if it is null
     *
     * @param id the id of the exerciseImage to save.
     * @param exerciseImage the exerciseImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseImage,
     * or with status {@code 400 (Bad Request)} if the exerciseImage is not valid,
     * or with status {@code 404 (Not Found)} if the exerciseImage is not found,
     * or with status {@code 500 (Internal Server Error)} if the exerciseImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exercise-images/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExerciseImage> partialUpdateExerciseImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExerciseImage exerciseImage
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExerciseImage partially : {}, {}", id, exerciseImage);
        if (exerciseImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExerciseImage> result = exerciseImageService.partialUpdate(exerciseImage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciseImage.getId().toString())
        );
    }

    /**
     * {@code GET  /exercise-images} : get all the exerciseImages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exerciseImages in body.
     */
    @GetMapping("/exercise-images")
    public List<ExerciseImage> getAllExerciseImages() {
        log.debug("REST request to get all ExerciseImages");
        return exerciseImageService.findAll();
    }

    /**
     * {@code GET  /exercise-images/:id} : get the "id" exerciseImage.
     *
     * @param id the id of the exerciseImage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exerciseImage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exercise-images/{id}")
    public ResponseEntity<ExerciseImage> getExerciseImage(@PathVariable Long id) {
        log.debug("REST request to get ExerciseImage : {}", id);
        Optional<ExerciseImage> exerciseImage = exerciseImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exerciseImage);
    }

    /**
     * {@code DELETE  /exercise-images/:id} : delete the "id" exerciseImage.
     *
     * @param id the id of the exerciseImage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exercise-images/{id}")
    public ResponseEntity<Void> deleteExerciseImage(@PathVariable Long id) {
        log.debug("REST request to delete ExerciseImage : {}", id);
        exerciseImageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/exercise-images?query=:query} : search for the exerciseImage corresponding
     * to the query.
     *
     * @param query the query of the exerciseImage search.
     * @return the result of the search.
     */
    @GetMapping("/_search/exercise-images")
    public List<ExerciseImage> searchExerciseImages(@RequestParam String query) {
        log.debug("REST request to search ExerciseImages for query {}", query);
        return exerciseImageService.search(query);
    }
}
