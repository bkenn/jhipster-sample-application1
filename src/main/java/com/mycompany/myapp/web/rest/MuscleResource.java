package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Muscle;
import com.mycompany.myapp.repository.MuscleRepository;
import com.mycompany.myapp.service.MuscleService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Muscle}.
 */
@RestController
@RequestMapping("/api")
public class MuscleResource {

    private final Logger log = LoggerFactory.getLogger(MuscleResource.class);

    private static final String ENTITY_NAME = "muscle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MuscleService muscleService;

    private final MuscleRepository muscleRepository;

    public MuscleResource(MuscleService muscleService, MuscleRepository muscleRepository) {
        this.muscleService = muscleService;
        this.muscleRepository = muscleRepository;
    }

    /**
     * {@code POST  /muscles} : Create a new muscle.
     *
     * @param muscle the muscle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new muscle, or with status {@code 400 (Bad Request)} if the muscle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/muscles")
    public ResponseEntity<Muscle> createMuscle(@RequestBody Muscle muscle) throws URISyntaxException {
        log.debug("REST request to save Muscle : {}", muscle);
        if (muscle.getId() != null) {
            throw new BadRequestAlertException("A new muscle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Muscle result = muscleService.save(muscle);
        return ResponseEntity
            .created(new URI("/api/muscles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /muscles/:id} : Updates an existing muscle.
     *
     * @param id the id of the muscle to save.
     * @param muscle the muscle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated muscle,
     * or with status {@code 400 (Bad Request)} if the muscle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the muscle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/muscles/{id}")
    public ResponseEntity<Muscle> updateMuscle(@PathVariable(value = "id", required = false) final Long id, @RequestBody Muscle muscle)
        throws URISyntaxException {
        log.debug("REST request to update Muscle : {}, {}", id, muscle);
        if (muscle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, muscle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!muscleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Muscle result = muscleService.save(muscle);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, muscle.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /muscles/:id} : Partial updates given fields of an existing muscle, field will ignore if it is null
     *
     * @param id the id of the muscle to save.
     * @param muscle the muscle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated muscle,
     * or with status {@code 400 (Bad Request)} if the muscle is not valid,
     * or with status {@code 404 (Not Found)} if the muscle is not found,
     * or with status {@code 500 (Internal Server Error)} if the muscle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/muscles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Muscle> partialUpdateMuscle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Muscle muscle
    ) throws URISyntaxException {
        log.debug("REST request to partial update Muscle partially : {}, {}", id, muscle);
        if (muscle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, muscle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!muscleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Muscle> result = muscleService.partialUpdate(muscle);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, muscle.getId().toString())
        );
    }

    /**
     * {@code GET  /muscles} : get all the muscles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of muscles in body.
     */
    @GetMapping("/muscles")
    public List<Muscle> getAllMuscles() {
        log.debug("REST request to get all Muscles");
        return muscleService.findAll();
    }

    /**
     * {@code GET  /muscles/:id} : get the "id" muscle.
     *
     * @param id the id of the muscle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the muscle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/muscles/{id}")
    public ResponseEntity<Muscle> getMuscle(@PathVariable Long id) {
        log.debug("REST request to get Muscle : {}", id);
        Optional<Muscle> muscle = muscleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(muscle);
    }

    /**
     * {@code DELETE  /muscles/:id} : delete the "id" muscle.
     *
     * @param id the id of the muscle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/muscles/{id}")
    public ResponseEntity<Void> deleteMuscle(@PathVariable Long id) {
        log.debug("REST request to delete Muscle : {}", id);
        muscleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/muscles?query=:query} : search for the muscle corresponding
     * to the query.
     *
     * @param query the query of the muscle search.
     * @return the result of the search.
     */
    @GetMapping("/_search/muscles")
    public List<Muscle> searchMuscles(@RequestParam String query) {
        log.debug("REST request to search Muscles for query {}", query);
        return muscleService.search(query);
    }
}
