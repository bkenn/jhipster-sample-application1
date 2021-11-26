package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.BodyMeasurement;
import com.mycompany.myapp.repository.BodyMeasurementRepository;
import com.mycompany.myapp.service.BodyMeasurementService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BodyMeasurement}.
 */
@RestController
@RequestMapping("/api")
public class BodyMeasurementResource {

    private final Logger log = LoggerFactory.getLogger(BodyMeasurementResource.class);

    private static final String ENTITY_NAME = "bodyMeasurement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BodyMeasurementService bodyMeasurementService;

    private final BodyMeasurementRepository bodyMeasurementRepository;

    public BodyMeasurementResource(BodyMeasurementService bodyMeasurementService, BodyMeasurementRepository bodyMeasurementRepository) {
        this.bodyMeasurementService = bodyMeasurementService;
        this.bodyMeasurementRepository = bodyMeasurementRepository;
    }

    /**
     * {@code POST  /body-measurements} : Create a new bodyMeasurement.
     *
     * @param bodyMeasurement the bodyMeasurement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bodyMeasurement, or with status {@code 400 (Bad Request)} if the bodyMeasurement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/body-measurements")
    public ResponseEntity<BodyMeasurement> createBodyMeasurement(@Valid @RequestBody BodyMeasurement bodyMeasurement)
        throws URISyntaxException {
        log.debug("REST request to save BodyMeasurement : {}", bodyMeasurement);
        if (bodyMeasurement.getId() != null) {
            throw new BadRequestAlertException("A new bodyMeasurement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BodyMeasurement result = bodyMeasurementService.save(bodyMeasurement);
        return ResponseEntity
            .created(new URI("/api/body-measurements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /body-measurements/:id} : Updates an existing bodyMeasurement.
     *
     * @param id the id of the bodyMeasurement to save.
     * @param bodyMeasurement the bodyMeasurement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bodyMeasurement,
     * or with status {@code 400 (Bad Request)} if the bodyMeasurement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bodyMeasurement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/body-measurements/{id}")
    public ResponseEntity<BodyMeasurement> updateBodyMeasurement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BodyMeasurement bodyMeasurement
    ) throws URISyntaxException {
        log.debug("REST request to update BodyMeasurement : {}, {}", id, bodyMeasurement);
        if (bodyMeasurement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodyMeasurement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodyMeasurementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BodyMeasurement result = bodyMeasurementService.save(bodyMeasurement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bodyMeasurement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /body-measurements/:id} : Partial updates given fields of an existing bodyMeasurement, field will ignore if it is null
     *
     * @param id the id of the bodyMeasurement to save.
     * @param bodyMeasurement the bodyMeasurement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bodyMeasurement,
     * or with status {@code 400 (Bad Request)} if the bodyMeasurement is not valid,
     * or with status {@code 404 (Not Found)} if the bodyMeasurement is not found,
     * or with status {@code 500 (Internal Server Error)} if the bodyMeasurement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/body-measurements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BodyMeasurement> partialUpdateBodyMeasurement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BodyMeasurement bodyMeasurement
    ) throws URISyntaxException {
        log.debug("REST request to partial update BodyMeasurement partially : {}, {}", id, bodyMeasurement);
        if (bodyMeasurement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodyMeasurement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodyMeasurementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BodyMeasurement> result = bodyMeasurementService.partialUpdate(bodyMeasurement);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bodyMeasurement.getId().toString())
        );
    }

    /**
     * {@code GET  /body-measurements} : get all the bodyMeasurements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bodyMeasurements in body.
     */
    @GetMapping("/body-measurements")
    public List<BodyMeasurement> getAllBodyMeasurements() {
        log.debug("REST request to get all BodyMeasurements");
        return bodyMeasurementService.findAll();
    }

    /**
     * {@code GET  /body-measurements/:id} : get the "id" bodyMeasurement.
     *
     * @param id the id of the bodyMeasurement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bodyMeasurement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/body-measurements/{id}")
    public ResponseEntity<BodyMeasurement> getBodyMeasurement(@PathVariable Long id) {
        log.debug("REST request to get BodyMeasurement : {}", id);
        Optional<BodyMeasurement> bodyMeasurement = bodyMeasurementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bodyMeasurement);
    }

    /**
     * {@code DELETE  /body-measurements/:id} : delete the "id" bodyMeasurement.
     *
     * @param id the id of the bodyMeasurement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/body-measurements/{id}")
    public ResponseEntity<Void> deleteBodyMeasurement(@PathVariable Long id) {
        log.debug("REST request to delete BodyMeasurement : {}", id);
        bodyMeasurementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/body-measurements?query=:query} : search for the bodyMeasurement corresponding
     * to the query.
     *
     * @param query the query of the bodyMeasurement search.
     * @return the result of the search.
     */
    @GetMapping("/_search/body-measurements")
    public List<BodyMeasurement> searchBodyMeasurements(@RequestParam String query) {
        log.debug("REST request to search BodyMeasurements for query {}", query);
        return bodyMeasurementService.search(query);
    }
}
