package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.WorkoutRoutineExerciseSet;
import com.mycompany.myapp.repository.WorkoutRoutineExerciseSetRepository;
import com.mycompany.myapp.service.WorkoutRoutineExerciseSetService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.WorkoutRoutineExerciseSet}.
 */
@RestController
@RequestMapping("/api")
public class WorkoutRoutineExerciseSetResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutRoutineExerciseSetResource.class);

    private static final String ENTITY_NAME = "workoutRoutineExerciseSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkoutRoutineExerciseSetService workoutRoutineExerciseSetService;

    private final WorkoutRoutineExerciseSetRepository workoutRoutineExerciseSetRepository;

    public WorkoutRoutineExerciseSetResource(
        WorkoutRoutineExerciseSetService workoutRoutineExerciseSetService,
        WorkoutRoutineExerciseSetRepository workoutRoutineExerciseSetRepository
    ) {
        this.workoutRoutineExerciseSetService = workoutRoutineExerciseSetService;
        this.workoutRoutineExerciseSetRepository = workoutRoutineExerciseSetRepository;
    }

    /**
     * {@code POST  /workout-routine-exercise-sets} : Create a new workoutRoutineExerciseSet.
     *
     * @param workoutRoutineExerciseSet the workoutRoutineExerciseSet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workoutRoutineExerciseSet, or with status {@code 400 (Bad Request)} if the workoutRoutineExerciseSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workout-routine-exercise-sets")
    public ResponseEntity<WorkoutRoutineExerciseSet> createWorkoutRoutineExerciseSet(
        @RequestBody WorkoutRoutineExerciseSet workoutRoutineExerciseSet
    ) throws URISyntaxException {
        log.debug("REST request to save WorkoutRoutineExerciseSet : {}", workoutRoutineExerciseSet);
        if (workoutRoutineExerciseSet.getId() != null) {
            throw new BadRequestAlertException("A new workoutRoutineExerciseSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkoutRoutineExerciseSet result = workoutRoutineExerciseSetService.save(workoutRoutineExerciseSet);
        return ResponseEntity
            .created(new URI("/api/workout-routine-exercise-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workout-routine-exercise-sets/:id} : Updates an existing workoutRoutineExerciseSet.
     *
     * @param id the id of the workoutRoutineExerciseSet to save.
     * @param workoutRoutineExerciseSet the workoutRoutineExerciseSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutRoutineExerciseSet,
     * or with status {@code 400 (Bad Request)} if the workoutRoutineExerciseSet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workoutRoutineExerciseSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workout-routine-exercise-sets/{id}")
    public ResponseEntity<WorkoutRoutineExerciseSet> updateWorkoutRoutineExerciseSet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutRoutineExerciseSet workoutRoutineExerciseSet
    ) throws URISyntaxException {
        log.debug("REST request to update WorkoutRoutineExerciseSet : {}, {}", id, workoutRoutineExerciseSet);
        if (workoutRoutineExerciseSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutRoutineExerciseSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRoutineExerciseSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkoutRoutineExerciseSet result = workoutRoutineExerciseSetService.save(workoutRoutineExerciseSet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutRoutineExerciseSet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workout-routine-exercise-sets/:id} : Partial updates given fields of an existing workoutRoutineExerciseSet, field will ignore if it is null
     *
     * @param id the id of the workoutRoutineExerciseSet to save.
     * @param workoutRoutineExerciseSet the workoutRoutineExerciseSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutRoutineExerciseSet,
     * or with status {@code 400 (Bad Request)} if the workoutRoutineExerciseSet is not valid,
     * or with status {@code 404 (Not Found)} if the workoutRoutineExerciseSet is not found,
     * or with status {@code 500 (Internal Server Error)} if the workoutRoutineExerciseSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workout-routine-exercise-sets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkoutRoutineExerciseSet> partialUpdateWorkoutRoutineExerciseSet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutRoutineExerciseSet workoutRoutineExerciseSet
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkoutRoutineExerciseSet partially : {}, {}", id, workoutRoutineExerciseSet);
        if (workoutRoutineExerciseSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutRoutineExerciseSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRoutineExerciseSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkoutRoutineExerciseSet> result = workoutRoutineExerciseSetService.partialUpdate(workoutRoutineExerciseSet);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutRoutineExerciseSet.getId().toString())
        );
    }

    /**
     * {@code GET  /workout-routine-exercise-sets} : get all the workoutRoutineExerciseSets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workoutRoutineExerciseSets in body.
     */
    @GetMapping("/workout-routine-exercise-sets")
    public List<WorkoutRoutineExerciseSet> getAllWorkoutRoutineExerciseSets() {
        log.debug("REST request to get all WorkoutRoutineExerciseSets");
        return workoutRoutineExerciseSetService.findAll();
    }

    /**
     * {@code GET  /workout-routine-exercise-sets/:id} : get the "id" workoutRoutineExerciseSet.
     *
     * @param id the id of the workoutRoutineExerciseSet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workoutRoutineExerciseSet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workout-routine-exercise-sets/{id}")
    public ResponseEntity<WorkoutRoutineExerciseSet> getWorkoutRoutineExerciseSet(@PathVariable Long id) {
        log.debug("REST request to get WorkoutRoutineExerciseSet : {}", id);
        Optional<WorkoutRoutineExerciseSet> workoutRoutineExerciseSet = workoutRoutineExerciseSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutRoutineExerciseSet);
    }

    /**
     * {@code DELETE  /workout-routine-exercise-sets/:id} : delete the "id" workoutRoutineExerciseSet.
     *
     * @param id the id of the workoutRoutineExerciseSet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workout-routine-exercise-sets/{id}")
    public ResponseEntity<Void> deleteWorkoutRoutineExerciseSet(@PathVariable Long id) {
        log.debug("REST request to delete WorkoutRoutineExerciseSet : {}", id);
        workoutRoutineExerciseSetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/workout-routine-exercise-sets?query=:query} : search for the workoutRoutineExerciseSet corresponding
     * to the query.
     *
     * @param query the query of the workoutRoutineExerciseSet search.
     * @return the result of the search.
     */
    @GetMapping("/_search/workout-routine-exercise-sets")
    public List<WorkoutRoutineExerciseSet> searchWorkoutRoutineExerciseSets(@RequestParam String query) {
        log.debug("REST request to search WorkoutRoutineExerciseSets for query {}", query);
        return workoutRoutineExerciseSetService.search(query);
    }
}
