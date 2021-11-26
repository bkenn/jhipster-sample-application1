package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Workout;
import com.mycompany.myapp.repository.WorkoutRepository;
import com.mycompany.myapp.service.WorkoutService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Workout}.
 */
@RestController
@RequestMapping("/api")
public class WorkoutResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutResource.class);

    private static final String ENTITY_NAME = "workout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkoutService workoutService;

    private final WorkoutRepository workoutRepository;

    public WorkoutResource(WorkoutService workoutService, WorkoutRepository workoutRepository) {
        this.workoutService = workoutService;
        this.workoutRepository = workoutRepository;
    }

    /**
     * {@code POST  /workouts} : Create a new workout.
     *
     * @param workout the workout to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workout, or with status {@code 400 (Bad Request)} if the workout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workouts")
    public ResponseEntity<Workout> createWorkout(@RequestBody Workout workout) throws URISyntaxException {
        log.debug("REST request to save Workout : {}", workout);
        if (workout.getId() != null) {
            throw new BadRequestAlertException("A new workout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Workout result = workoutService.save(workout);
        return ResponseEntity
            .created(new URI("/api/workouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workouts/:id} : Updates an existing workout.
     *
     * @param id the id of the workout to save.
     * @param workout the workout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workout,
     * or with status {@code 400 (Bad Request)} if the workout is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workouts/{id}")
    public ResponseEntity<Workout> updateWorkout(@PathVariable(value = "id", required = false) final Long id, @RequestBody Workout workout)
        throws URISyntaxException {
        log.debug("REST request to update Workout : {}, {}", id, workout);
        if (workout.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workout.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Workout result = workoutService.save(workout);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workout.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workouts/:id} : Partial updates given fields of an existing workout, field will ignore if it is null
     *
     * @param id the id of the workout to save.
     * @param workout the workout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workout,
     * or with status {@code 400 (Bad Request)} if the workout is not valid,
     * or with status {@code 404 (Not Found)} if the workout is not found,
     * or with status {@code 500 (Internal Server Error)} if the workout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workouts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Workout> partialUpdateWorkout(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Workout workout
    ) throws URISyntaxException {
        log.debug("REST request to partial update Workout partially : {}, {}", id, workout);
        if (workout.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workout.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Workout> result = workoutService.partialUpdate(workout);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workout.getId().toString())
        );
    }

    /**
     * {@code GET  /workouts} : get all the workouts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workouts in body.
     */
    @GetMapping("/workouts")
    public List<Workout> getAllWorkouts() {
        log.debug("REST request to get all Workouts");
        return workoutService.findAll();
    }

    /**
     * {@code GET  /workouts/:id} : get the "id" workout.
     *
     * @param id the id of the workout to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workout, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workouts/{id}")
    public ResponseEntity<Workout> getWorkout(@PathVariable Long id) {
        log.debug("REST request to get Workout : {}", id);
        Optional<Workout> workout = workoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workout);
    }

    /**
     * {@code DELETE  /workouts/:id} : delete the "id" workout.
     *
     * @param id the id of the workout to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workouts/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        log.debug("REST request to delete Workout : {}", id);
        workoutService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/workouts?query=:query} : search for the workout corresponding
     * to the query.
     *
     * @param query the query of the workout search.
     * @return the result of the search.
     */
    @GetMapping("/_search/workouts")
    public List<Workout> searchWorkouts(@RequestParam String query) {
        log.debug("REST request to search Workouts for query {}", query);
        return workoutService.search(query);
    }
}
