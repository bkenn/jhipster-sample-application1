package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.ExerciseCategory;
import com.mycompany.myapp.repository.ExerciseCategoryRepository;
import com.mycompany.myapp.service.ExerciseCategoryService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ExerciseCategory}.
 */
@RestController
@RequestMapping("/api")
public class ExerciseCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseCategoryResource.class);

    private static final String ENTITY_NAME = "exerciseCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciseCategoryService exerciseCategoryService;

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    public ExerciseCategoryResource(
        ExerciseCategoryService exerciseCategoryService,
        ExerciseCategoryRepository exerciseCategoryRepository
    ) {
        this.exerciseCategoryService = exerciseCategoryService;
        this.exerciseCategoryRepository = exerciseCategoryRepository;
    }

    /**
     * {@code POST  /exercise-categories} : Create a new exerciseCategory.
     *
     * @param exerciseCategory the exerciseCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exerciseCategory, or with status {@code 400 (Bad Request)} if the exerciseCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exercise-categories")
    public ResponseEntity<ExerciseCategory> createExerciseCategory(@Valid @RequestBody ExerciseCategory exerciseCategory)
        throws URISyntaxException {
        log.debug("REST request to save ExerciseCategory : {}", exerciseCategory);
        if (exerciseCategory.getId() != null) {
            throw new BadRequestAlertException("A new exerciseCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExerciseCategory result = exerciseCategoryService.save(exerciseCategory);
        return ResponseEntity
            .created(new URI("/api/exercise-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercise-categories/:id} : Updates an existing exerciseCategory.
     *
     * @param id the id of the exerciseCategory to save.
     * @param exerciseCategory the exerciseCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseCategory,
     * or with status {@code 400 (Bad Request)} if the exerciseCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exerciseCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exercise-categories/{id}")
    public ResponseEntity<ExerciseCategory> updateExerciseCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExerciseCategory exerciseCategory
    ) throws URISyntaxException {
        log.debug("REST request to update ExerciseCategory : {}, {}", id, exerciseCategory);
        if (exerciseCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExerciseCategory result = exerciseCategoryService.save(exerciseCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciseCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exercise-categories/:id} : Partial updates given fields of an existing exerciseCategory, field will ignore if it is null
     *
     * @param id the id of the exerciseCategory to save.
     * @param exerciseCategory the exerciseCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseCategory,
     * or with status {@code 400 (Bad Request)} if the exerciseCategory is not valid,
     * or with status {@code 404 (Not Found)} if the exerciseCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the exerciseCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exercise-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExerciseCategory> partialUpdateExerciseCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExerciseCategory exerciseCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExerciseCategory partially : {}, {}", id, exerciseCategory);
        if (exerciseCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExerciseCategory> result = exerciseCategoryService.partialUpdate(exerciseCategory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciseCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /exercise-categories} : get all the exerciseCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exerciseCategories in body.
     */
    @GetMapping("/exercise-categories")
    public List<ExerciseCategory> getAllExerciseCategories() {
        log.debug("REST request to get all ExerciseCategories");
        return exerciseCategoryService.findAll();
    }

    /**
     * {@code GET  /exercise-categories/:id} : get the "id" exerciseCategory.
     *
     * @param id the id of the exerciseCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exerciseCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exercise-categories/{id}")
    public ResponseEntity<ExerciseCategory> getExerciseCategory(@PathVariable Long id) {
        log.debug("REST request to get ExerciseCategory : {}", id);
        Optional<ExerciseCategory> exerciseCategory = exerciseCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exerciseCategory);
    }

    /**
     * {@code DELETE  /exercise-categories/:id} : delete the "id" exerciseCategory.
     *
     * @param id the id of the exerciseCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exercise-categories/{id}")
    public ResponseEntity<Void> deleteExerciseCategory(@PathVariable Long id) {
        log.debug("REST request to delete ExerciseCategory : {}", id);
        exerciseCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/exercise-categories?query=:query} : search for the exerciseCategory corresponding
     * to the query.
     *
     * @param query the query of the exerciseCategory search.
     * @return the result of the search.
     */
    @GetMapping("/_search/exercise-categories")
    public List<ExerciseCategory> searchExerciseCategories(@RequestParam String query) {
        log.debug("REST request to search ExerciseCategories for query {}", query);
        return exerciseCategoryService.search(query);
    }
}
