package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.WorkoutRoutine;
import com.mycompany.myapp.repository.WorkoutRoutineRepository;
import com.mycompany.myapp.service.WorkoutRoutineService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.WorkoutRoutine}.
 */
@RestController
@RequestMapping("/api")
public class WorkoutRoutineResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutRoutineResource.class);

    private static final String ENTITY_NAME = "workoutRoutine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkoutRoutineService workoutRoutineService;

    private final WorkoutRoutineRepository workoutRoutineRepository;

    public WorkoutRoutineResource(WorkoutRoutineService workoutRoutineService, WorkoutRoutineRepository workoutRoutineRepository) {
        this.workoutRoutineService = workoutRoutineService;
        this.workoutRoutineRepository = workoutRoutineRepository;
    }

    /**
     * {@code POST  /workout-routines} : Create a new workoutRoutine.
     *
     * @param workoutRoutine the workoutRoutine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workoutRoutine, or with status {@code 400 (Bad Request)} if the workoutRoutine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workout-routines")
    public ResponseEntity<WorkoutRoutine> createWorkoutRoutine(@RequestBody WorkoutRoutine workoutRoutine) throws URISyntaxException {
        log.debug("REST request to save WorkoutRoutine : {}", workoutRoutine);
        if (workoutRoutine.getId() != null) {
            throw new BadRequestAlertException("A new workoutRoutine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkoutRoutine result = workoutRoutineService.save(workoutRoutine);
        return ResponseEntity
            .created(new URI("/api/workout-routines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workout-routines/:id} : Updates an existing workoutRoutine.
     *
     * @param id the id of the workoutRoutine to save.
     * @param workoutRoutine the workoutRoutine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutRoutine,
     * or with status {@code 400 (Bad Request)} if the workoutRoutine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workoutRoutine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workout-routines/{id}")
    public ResponseEntity<WorkoutRoutine> updateWorkoutRoutine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutRoutine workoutRoutine
    ) throws URISyntaxException {
        log.debug("REST request to update WorkoutRoutine : {}, {}", id, workoutRoutine);
        if (workoutRoutine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutRoutine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRoutineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkoutRoutine result = workoutRoutineService.save(workoutRoutine);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutRoutine.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workout-routines/:id} : Partial updates given fields of an existing workoutRoutine, field will ignore if it is null
     *
     * @param id the id of the workoutRoutine to save.
     * @param workoutRoutine the workoutRoutine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutRoutine,
     * or with status {@code 400 (Bad Request)} if the workoutRoutine is not valid,
     * or with status {@code 404 (Not Found)} if the workoutRoutine is not found,
     * or with status {@code 500 (Internal Server Error)} if the workoutRoutine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workout-routines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkoutRoutine> partialUpdateWorkoutRoutine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutRoutine workoutRoutine
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkoutRoutine partially : {}, {}", id, workoutRoutine);
        if (workoutRoutine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutRoutine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRoutineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkoutRoutine> result = workoutRoutineService.partialUpdate(workoutRoutine);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutRoutine.getId().toString())
        );
    }

    /**
     * {@code GET  /workout-routines} : get all the workoutRoutines.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workoutRoutines in body.
     */
    @GetMapping("/workout-routines")
    public List<WorkoutRoutine> getAllWorkoutRoutines() {
        log.debug("REST request to get all WorkoutRoutines");
        return workoutRoutineService.findAll();
    }

    /**
     * {@code GET  /workout-routines/:id} : get the "id" workoutRoutine.
     *
     * @param id the id of the workoutRoutine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workoutRoutine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workout-routines/{id}")
    public ResponseEntity<WorkoutRoutine> getWorkoutRoutine(@PathVariable Long id) {
        log.debug("REST request to get WorkoutRoutine : {}", id);
        Optional<WorkoutRoutine> workoutRoutine = workoutRoutineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutRoutine);
    }

    /**
     * {@code DELETE  /workout-routines/:id} : delete the "id" workoutRoutine.
     *
     * @param id the id of the workoutRoutine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workout-routines/{id}")
    public ResponseEntity<Void> deleteWorkoutRoutine(@PathVariable Long id) {
        log.debug("REST request to delete WorkoutRoutine : {}", id);
        workoutRoutineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/workout-routines?query=:query} : search for the workoutRoutine corresponding
     * to the query.
     *
     * @param query the query of the workoutRoutine search.
     * @return the result of the search.
     */
    @GetMapping("/_search/workout-routines")
    public List<WorkoutRoutine> searchWorkoutRoutines(@RequestParam String query) {
        log.debug("REST request to search WorkoutRoutines for query {}", query);
        return workoutRoutineService.search(query);
    }
}
