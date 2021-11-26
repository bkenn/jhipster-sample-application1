package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.WorkoutExercise;
import com.mycompany.myapp.repository.WorkoutExerciseRepository;
import com.mycompany.myapp.service.WorkoutExerciseService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.WorkoutExercise}.
 */
@RestController
@RequestMapping("/api")
public class WorkoutExerciseResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutExerciseResource.class);

    private static final String ENTITY_NAME = "workoutExercise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkoutExerciseService workoutExerciseService;

    private final WorkoutExerciseRepository workoutExerciseRepository;

    public WorkoutExerciseResource(WorkoutExerciseService workoutExerciseService, WorkoutExerciseRepository workoutExerciseRepository) {
        this.workoutExerciseService = workoutExerciseService;
        this.workoutExerciseRepository = workoutExerciseRepository;
    }

    /**
     * {@code POST  /workout-exercises} : Create a new workoutExercise.
     *
     * @param workoutExercise the workoutExercise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workoutExercise, or with status {@code 400 (Bad Request)} if the workoutExercise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workout-exercises")
    public ResponseEntity<WorkoutExercise> createWorkoutExercise(@RequestBody WorkoutExercise workoutExercise) throws URISyntaxException {
        log.debug("REST request to save WorkoutExercise : {}", workoutExercise);
        if (workoutExercise.getId() != null) {
            throw new BadRequestAlertException("A new workoutExercise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkoutExercise result = workoutExerciseService.save(workoutExercise);
        return ResponseEntity
            .created(new URI("/api/workout-exercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workout-exercises/:id} : Updates an existing workoutExercise.
     *
     * @param id the id of the workoutExercise to save.
     * @param workoutExercise the workoutExercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutExercise,
     * or with status {@code 400 (Bad Request)} if the workoutExercise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workoutExercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workout-exercises/{id}")
    public ResponseEntity<WorkoutExercise> updateWorkoutExercise(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutExercise workoutExercise
    ) throws URISyntaxException {
        log.debug("REST request to update WorkoutExercise : {}, {}", id, workoutExercise);
        if (workoutExercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutExercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutExerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkoutExercise result = workoutExerciseService.save(workoutExercise);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutExercise.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workout-exercises/:id} : Partial updates given fields of an existing workoutExercise, field will ignore if it is null
     *
     * @param id the id of the workoutExercise to save.
     * @param workoutExercise the workoutExercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutExercise,
     * or with status {@code 400 (Bad Request)} if the workoutExercise is not valid,
     * or with status {@code 404 (Not Found)} if the workoutExercise is not found,
     * or with status {@code 500 (Internal Server Error)} if the workoutExercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workout-exercises/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkoutExercise> partialUpdateWorkoutExercise(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutExercise workoutExercise
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkoutExercise partially : {}, {}", id, workoutExercise);
        if (workoutExercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutExercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutExerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkoutExercise> result = workoutExerciseService.partialUpdate(workoutExercise);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutExercise.getId().toString())
        );
    }

    /**
     * {@code GET  /workout-exercises} : get all the workoutExercises.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workoutExercises in body.
     */
    @GetMapping("/workout-exercises")
    public List<WorkoutExercise> getAllWorkoutExercises() {
        log.debug("REST request to get all WorkoutExercises");
        return workoutExerciseService.findAll();
    }

    /**
     * {@code GET  /workout-exercises/:id} : get the "id" workoutExercise.
     *
     * @param id the id of the workoutExercise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workoutExercise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workout-exercises/{id}")
    public ResponseEntity<WorkoutExercise> getWorkoutExercise(@PathVariable Long id) {
        log.debug("REST request to get WorkoutExercise : {}", id);
        Optional<WorkoutExercise> workoutExercise = workoutExerciseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutExercise);
    }

    /**
     * {@code DELETE  /workout-exercises/:id} : delete the "id" workoutExercise.
     *
     * @param id the id of the workoutExercise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workout-exercises/{id}")
    public ResponseEntity<Void> deleteWorkoutExercise(@PathVariable Long id) {
        log.debug("REST request to delete WorkoutExercise : {}", id);
        workoutExerciseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/workout-exercises?query=:query} : search for the workoutExercise corresponding
     * to the query.
     *
     * @param query the query of the workoutExercise search.
     * @return the result of the search.
     */
    @GetMapping("/_search/workout-exercises")
    public List<WorkoutExercise> searchWorkoutExercises(@RequestParam String query) {
        log.debug("REST request to search WorkoutExercises for query {}", query);
        return workoutExerciseService.search(query);
    }
}
