package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.WorkoutExerciseSet;
import com.mycompany.myapp.repository.WorkoutExerciseSetRepository;
import com.mycompany.myapp.service.WorkoutExerciseSetService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.WorkoutExerciseSet}.
 */
@RestController
@RequestMapping("/api")
public class WorkoutExerciseSetResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutExerciseSetResource.class);

    private static final String ENTITY_NAME = "workoutExerciseSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkoutExerciseSetService workoutExerciseSetService;

    private final WorkoutExerciseSetRepository workoutExerciseSetRepository;

    public WorkoutExerciseSetResource(
        WorkoutExerciseSetService workoutExerciseSetService,
        WorkoutExerciseSetRepository workoutExerciseSetRepository
    ) {
        this.workoutExerciseSetService = workoutExerciseSetService;
        this.workoutExerciseSetRepository = workoutExerciseSetRepository;
    }

    /**
     * {@code POST  /workout-exercise-sets} : Create a new workoutExerciseSet.
     *
     * @param workoutExerciseSet the workoutExerciseSet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workoutExerciseSet, or with status {@code 400 (Bad Request)} if the workoutExerciseSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workout-exercise-sets")
    public ResponseEntity<WorkoutExerciseSet> createWorkoutExerciseSet(@RequestBody WorkoutExerciseSet workoutExerciseSet)
        throws URISyntaxException {
        log.debug("REST request to save WorkoutExerciseSet : {}", workoutExerciseSet);
        if (workoutExerciseSet.getId() != null) {
            throw new BadRequestAlertException("A new workoutExerciseSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkoutExerciseSet result = workoutExerciseSetService.save(workoutExerciseSet);
        return ResponseEntity
            .created(new URI("/api/workout-exercise-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workout-exercise-sets/:id} : Updates an existing workoutExerciseSet.
     *
     * @param id the id of the workoutExerciseSet to save.
     * @param workoutExerciseSet the workoutExerciseSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutExerciseSet,
     * or with status {@code 400 (Bad Request)} if the workoutExerciseSet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workoutExerciseSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workout-exercise-sets/{id}")
    public ResponseEntity<WorkoutExerciseSet> updateWorkoutExerciseSet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutExerciseSet workoutExerciseSet
    ) throws URISyntaxException {
        log.debug("REST request to update WorkoutExerciseSet : {}, {}", id, workoutExerciseSet);
        if (workoutExerciseSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutExerciseSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutExerciseSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkoutExerciseSet result = workoutExerciseSetService.save(workoutExerciseSet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutExerciseSet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workout-exercise-sets/:id} : Partial updates given fields of an existing workoutExerciseSet, field will ignore if it is null
     *
     * @param id the id of the workoutExerciseSet to save.
     * @param workoutExerciseSet the workoutExerciseSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutExerciseSet,
     * or with status {@code 400 (Bad Request)} if the workoutExerciseSet is not valid,
     * or with status {@code 404 (Not Found)} if the workoutExerciseSet is not found,
     * or with status {@code 500 (Internal Server Error)} if the workoutExerciseSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workout-exercise-sets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkoutExerciseSet> partialUpdateWorkoutExerciseSet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutExerciseSet workoutExerciseSet
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkoutExerciseSet partially : {}, {}", id, workoutExerciseSet);
        if (workoutExerciseSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutExerciseSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutExerciseSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkoutExerciseSet> result = workoutExerciseSetService.partialUpdate(workoutExerciseSet);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutExerciseSet.getId().toString())
        );
    }

    /**
     * {@code GET  /workout-exercise-sets} : get all the workoutExerciseSets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workoutExerciseSets in body.
     */
    @GetMapping("/workout-exercise-sets")
    public List<WorkoutExerciseSet> getAllWorkoutExerciseSets() {
        log.debug("REST request to get all WorkoutExerciseSets");
        return workoutExerciseSetService.findAll();
    }

    /**
     * {@code GET  /workout-exercise-sets/:id} : get the "id" workoutExerciseSet.
     *
     * @param id the id of the workoutExerciseSet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workoutExerciseSet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workout-exercise-sets/{id}")
    public ResponseEntity<WorkoutExerciseSet> getWorkoutExerciseSet(@PathVariable Long id) {
        log.debug("REST request to get WorkoutExerciseSet : {}", id);
        Optional<WorkoutExerciseSet> workoutExerciseSet = workoutExerciseSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutExerciseSet);
    }

    /**
     * {@code DELETE  /workout-exercise-sets/:id} : delete the "id" workoutExerciseSet.
     *
     * @param id the id of the workoutExerciseSet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workout-exercise-sets/{id}")
    public ResponseEntity<Void> deleteWorkoutExerciseSet(@PathVariable Long id) {
        log.debug("REST request to delete WorkoutExerciseSet : {}", id);
        workoutExerciseSetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/workout-exercise-sets?query=:query} : search for the workoutExerciseSet corresponding
     * to the query.
     *
     * @param query the query of the workoutExerciseSet search.
     * @return the result of the search.
     */
    @GetMapping("/_search/workout-exercise-sets")
    public List<WorkoutExerciseSet> searchWorkoutExerciseSets(@RequestParam String query) {
        log.debug("REST request to search WorkoutExerciseSets for query {}", query);
        return workoutExerciseSetService.search(query);
    }
}
