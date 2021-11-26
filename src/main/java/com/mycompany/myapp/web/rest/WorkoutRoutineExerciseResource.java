package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.WorkoutRoutineExercise;
import com.mycompany.myapp.repository.WorkoutRoutineExerciseRepository;
import com.mycompany.myapp.service.WorkoutRoutineExerciseService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.WorkoutRoutineExercise}.
 */
@RestController
@RequestMapping("/api")
public class WorkoutRoutineExerciseResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutRoutineExerciseResource.class);

    private static final String ENTITY_NAME = "workoutRoutineExercise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkoutRoutineExerciseService workoutRoutineExerciseService;

    private final WorkoutRoutineExerciseRepository workoutRoutineExerciseRepository;

    public WorkoutRoutineExerciseResource(
        WorkoutRoutineExerciseService workoutRoutineExerciseService,
        WorkoutRoutineExerciseRepository workoutRoutineExerciseRepository
    ) {
        this.workoutRoutineExerciseService = workoutRoutineExerciseService;
        this.workoutRoutineExerciseRepository = workoutRoutineExerciseRepository;
    }

    /**
     * {@code POST  /workout-routine-exercises} : Create a new workoutRoutineExercise.
     *
     * @param workoutRoutineExercise the workoutRoutineExercise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workoutRoutineExercise, or with status {@code 400 (Bad Request)} if the workoutRoutineExercise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workout-routine-exercises")
    public ResponseEntity<WorkoutRoutineExercise> createWorkoutRoutineExercise(@RequestBody WorkoutRoutineExercise workoutRoutineExercise)
        throws URISyntaxException {
        log.debug("REST request to save WorkoutRoutineExercise : {}", workoutRoutineExercise);
        if (workoutRoutineExercise.getId() != null) {
            throw new BadRequestAlertException("A new workoutRoutineExercise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkoutRoutineExercise result = workoutRoutineExerciseService.save(workoutRoutineExercise);
        return ResponseEntity
            .created(new URI("/api/workout-routine-exercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workout-routine-exercises/:id} : Updates an existing workoutRoutineExercise.
     *
     * @param id the id of the workoutRoutineExercise to save.
     * @param workoutRoutineExercise the workoutRoutineExercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutRoutineExercise,
     * or with status {@code 400 (Bad Request)} if the workoutRoutineExercise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workoutRoutineExercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workout-routine-exercises/{id}")
    public ResponseEntity<WorkoutRoutineExercise> updateWorkoutRoutineExercise(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutRoutineExercise workoutRoutineExercise
    ) throws URISyntaxException {
        log.debug("REST request to update WorkoutRoutineExercise : {}, {}", id, workoutRoutineExercise);
        if (workoutRoutineExercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutRoutineExercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRoutineExerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkoutRoutineExercise result = workoutRoutineExerciseService.save(workoutRoutineExercise);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutRoutineExercise.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workout-routine-exercises/:id} : Partial updates given fields of an existing workoutRoutineExercise, field will ignore if it is null
     *
     * @param id the id of the workoutRoutineExercise to save.
     * @param workoutRoutineExercise the workoutRoutineExercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutRoutineExercise,
     * or with status {@code 400 (Bad Request)} if the workoutRoutineExercise is not valid,
     * or with status {@code 404 (Not Found)} if the workoutRoutineExercise is not found,
     * or with status {@code 500 (Internal Server Error)} if the workoutRoutineExercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workout-routine-exercises/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkoutRoutineExercise> partialUpdateWorkoutRoutineExercise(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutRoutineExercise workoutRoutineExercise
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkoutRoutineExercise partially : {}, {}", id, workoutRoutineExercise);
        if (workoutRoutineExercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutRoutineExercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRoutineExerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkoutRoutineExercise> result = workoutRoutineExerciseService.partialUpdate(workoutRoutineExercise);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutRoutineExercise.getId().toString())
        );
    }

    /**
     * {@code GET  /workout-routine-exercises} : get all the workoutRoutineExercises.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workoutRoutineExercises in body.
     */
    @GetMapping("/workout-routine-exercises")
    public List<WorkoutRoutineExercise> getAllWorkoutRoutineExercises() {
        log.debug("REST request to get all WorkoutRoutineExercises");
        return workoutRoutineExerciseService.findAll();
    }

    /**
     * {@code GET  /workout-routine-exercises/:id} : get the "id" workoutRoutineExercise.
     *
     * @param id the id of the workoutRoutineExercise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workoutRoutineExercise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workout-routine-exercises/{id}")
    public ResponseEntity<WorkoutRoutineExercise> getWorkoutRoutineExercise(@PathVariable Long id) {
        log.debug("REST request to get WorkoutRoutineExercise : {}", id);
        Optional<WorkoutRoutineExercise> workoutRoutineExercise = workoutRoutineExerciseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutRoutineExercise);
    }

    /**
     * {@code DELETE  /workout-routine-exercises/:id} : delete the "id" workoutRoutineExercise.
     *
     * @param id the id of the workoutRoutineExercise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workout-routine-exercises/{id}")
    public ResponseEntity<Void> deleteWorkoutRoutineExercise(@PathVariable Long id) {
        log.debug("REST request to delete WorkoutRoutineExercise : {}", id);
        workoutRoutineExerciseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/workout-routine-exercises?query=:query} : search for the workoutRoutineExercise corresponding
     * to the query.
     *
     * @param query the query of the workoutRoutineExercise search.
     * @return the result of the search.
     */
    @GetMapping("/_search/workout-routine-exercises")
    public List<WorkoutRoutineExercise> searchWorkoutRoutineExercises(@RequestParam String query) {
        log.debug("REST request to search WorkoutRoutineExercises for query {}", query);
        return workoutRoutineExerciseService.search(query);
    }
}
