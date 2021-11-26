package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.WorkoutRoutineGroup;
import com.mycompany.myapp.repository.WorkoutRoutineGroupRepository;
import com.mycompany.myapp.service.WorkoutRoutineGroupService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.WorkoutRoutineGroup}.
 */
@RestController
@RequestMapping("/api")
public class WorkoutRoutineGroupResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutRoutineGroupResource.class);

    private static final String ENTITY_NAME = "workoutRoutineGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkoutRoutineGroupService workoutRoutineGroupService;

    private final WorkoutRoutineGroupRepository workoutRoutineGroupRepository;

    public WorkoutRoutineGroupResource(
        WorkoutRoutineGroupService workoutRoutineGroupService,
        WorkoutRoutineGroupRepository workoutRoutineGroupRepository
    ) {
        this.workoutRoutineGroupService = workoutRoutineGroupService;
        this.workoutRoutineGroupRepository = workoutRoutineGroupRepository;
    }

    /**
     * {@code POST  /workout-routine-groups} : Create a new workoutRoutineGroup.
     *
     * @param workoutRoutineGroup the workoutRoutineGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workoutRoutineGroup, or with status {@code 400 (Bad Request)} if the workoutRoutineGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workout-routine-groups")
    public ResponseEntity<WorkoutRoutineGroup> createWorkoutRoutineGroup(@RequestBody WorkoutRoutineGroup workoutRoutineGroup)
        throws URISyntaxException {
        log.debug("REST request to save WorkoutRoutineGroup : {}", workoutRoutineGroup);
        if (workoutRoutineGroup.getId() != null) {
            throw new BadRequestAlertException("A new workoutRoutineGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkoutRoutineGroup result = workoutRoutineGroupService.save(workoutRoutineGroup);
        return ResponseEntity
            .created(new URI("/api/workout-routine-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workout-routine-groups/:id} : Updates an existing workoutRoutineGroup.
     *
     * @param id the id of the workoutRoutineGroup to save.
     * @param workoutRoutineGroup the workoutRoutineGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutRoutineGroup,
     * or with status {@code 400 (Bad Request)} if the workoutRoutineGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workoutRoutineGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workout-routine-groups/{id}")
    public ResponseEntity<WorkoutRoutineGroup> updateWorkoutRoutineGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutRoutineGroup workoutRoutineGroup
    ) throws URISyntaxException {
        log.debug("REST request to update WorkoutRoutineGroup : {}, {}", id, workoutRoutineGroup);
        if (workoutRoutineGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutRoutineGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRoutineGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkoutRoutineGroup result = workoutRoutineGroupService.save(workoutRoutineGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutRoutineGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workout-routine-groups/:id} : Partial updates given fields of an existing workoutRoutineGroup, field will ignore if it is null
     *
     * @param id the id of the workoutRoutineGroup to save.
     * @param workoutRoutineGroup the workoutRoutineGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutRoutineGroup,
     * or with status {@code 400 (Bad Request)} if the workoutRoutineGroup is not valid,
     * or with status {@code 404 (Not Found)} if the workoutRoutineGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the workoutRoutineGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workout-routine-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkoutRoutineGroup> partialUpdateWorkoutRoutineGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutRoutineGroup workoutRoutineGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkoutRoutineGroup partially : {}, {}", id, workoutRoutineGroup);
        if (workoutRoutineGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutRoutineGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRoutineGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkoutRoutineGroup> result = workoutRoutineGroupService.partialUpdate(workoutRoutineGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutRoutineGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /workout-routine-groups} : get all the workoutRoutineGroups.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workoutRoutineGroups in body.
     */
    @GetMapping("/workout-routine-groups")
    public List<WorkoutRoutineGroup> getAllWorkoutRoutineGroups(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all WorkoutRoutineGroups");
        return workoutRoutineGroupService.findAll();
    }

    /**
     * {@code GET  /workout-routine-groups/:id} : get the "id" workoutRoutineGroup.
     *
     * @param id the id of the workoutRoutineGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workoutRoutineGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workout-routine-groups/{id}")
    public ResponseEntity<WorkoutRoutineGroup> getWorkoutRoutineGroup(@PathVariable Long id) {
        log.debug("REST request to get WorkoutRoutineGroup : {}", id);
        Optional<WorkoutRoutineGroup> workoutRoutineGroup = workoutRoutineGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutRoutineGroup);
    }

    /**
     * {@code DELETE  /workout-routine-groups/:id} : delete the "id" workoutRoutineGroup.
     *
     * @param id the id of the workoutRoutineGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workout-routine-groups/{id}")
    public ResponseEntity<Void> deleteWorkoutRoutineGroup(@PathVariable Long id) {
        log.debug("REST request to delete WorkoutRoutineGroup : {}", id);
        workoutRoutineGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/workout-routine-groups?query=:query} : search for the workoutRoutineGroup corresponding
     * to the query.
     *
     * @param query the query of the workoutRoutineGroup search.
     * @return the result of the search.
     */
    @GetMapping("/_search/workout-routine-groups")
    public List<WorkoutRoutineGroup> searchWorkoutRoutineGroups(@RequestParam String query) {
        log.debug("REST request to search WorkoutRoutineGroups for query {}", query);
        return workoutRoutineGroupService.search(query);
    }
}
