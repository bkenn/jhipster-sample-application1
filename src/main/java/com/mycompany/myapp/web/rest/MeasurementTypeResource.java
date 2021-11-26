package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.MeasurementType;
import com.mycompany.myapp.repository.MeasurementTypeRepository;
import com.mycompany.myapp.service.MeasurementTypeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MeasurementType}.
 */
@RestController
@RequestMapping("/api")
public class MeasurementTypeResource {

    private final Logger log = LoggerFactory.getLogger(MeasurementTypeResource.class);

    private static final String ENTITY_NAME = "measurementType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeasurementTypeService measurementTypeService;

    private final MeasurementTypeRepository measurementTypeRepository;

    public MeasurementTypeResource(MeasurementTypeService measurementTypeService, MeasurementTypeRepository measurementTypeRepository) {
        this.measurementTypeService = measurementTypeService;
        this.measurementTypeRepository = measurementTypeRepository;
    }

    /**
     * {@code POST  /measurement-types} : Create a new measurementType.
     *
     * @param measurementType the measurementType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new measurementType, or with status {@code 400 (Bad Request)} if the measurementType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/measurement-types")
    public ResponseEntity<MeasurementType> createMeasurementType(@Valid @RequestBody MeasurementType measurementType)
        throws URISyntaxException {
        log.debug("REST request to save MeasurementType : {}", measurementType);
        if (measurementType.getId() != null) {
            throw new BadRequestAlertException("A new measurementType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MeasurementType result = measurementTypeService.save(measurementType);
        return ResponseEntity
            .created(new URI("/api/measurement-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /measurement-types/:id} : Updates an existing measurementType.
     *
     * @param id the id of the measurementType to save.
     * @param measurementType the measurementType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measurementType,
     * or with status {@code 400 (Bad Request)} if the measurementType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the measurementType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/measurement-types/{id}")
    public ResponseEntity<MeasurementType> updateMeasurementType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MeasurementType measurementType
    ) throws URISyntaxException {
        log.debug("REST request to update MeasurementType : {}, {}", id, measurementType);
        if (measurementType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, measurementType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!measurementTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MeasurementType result = measurementTypeService.save(measurementType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, measurementType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /measurement-types/:id} : Partial updates given fields of an existing measurementType, field will ignore if it is null
     *
     * @param id the id of the measurementType to save.
     * @param measurementType the measurementType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measurementType,
     * or with status {@code 400 (Bad Request)} if the measurementType is not valid,
     * or with status {@code 404 (Not Found)} if the measurementType is not found,
     * or with status {@code 500 (Internal Server Error)} if the measurementType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/measurement-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MeasurementType> partialUpdateMeasurementType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MeasurementType measurementType
    ) throws URISyntaxException {
        log.debug("REST request to partial update MeasurementType partially : {}, {}", id, measurementType);
        if (measurementType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, measurementType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!measurementTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MeasurementType> result = measurementTypeService.partialUpdate(measurementType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, measurementType.getId().toString())
        );
    }

    /**
     * {@code GET  /measurement-types} : get all the measurementTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of measurementTypes in body.
     */
    @GetMapping("/measurement-types")
    public List<MeasurementType> getAllMeasurementTypes() {
        log.debug("REST request to get all MeasurementTypes");
        return measurementTypeService.findAll();
    }

    /**
     * {@code GET  /measurement-types/:id} : get the "id" measurementType.
     *
     * @param id the id of the measurementType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the measurementType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/measurement-types/{id}")
    public ResponseEntity<MeasurementType> getMeasurementType(@PathVariable Long id) {
        log.debug("REST request to get MeasurementType : {}", id);
        Optional<MeasurementType> measurementType = measurementTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(measurementType);
    }

    /**
     * {@code DELETE  /measurement-types/:id} : delete the "id" measurementType.
     *
     * @param id the id of the measurementType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/measurement-types/{id}")
    public ResponseEntity<Void> deleteMeasurementType(@PathVariable Long id) {
        log.debug("REST request to delete MeasurementType : {}", id);
        measurementTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/measurement-types?query=:query} : search for the measurementType corresponding
     * to the query.
     *
     * @param query the query of the measurementType search.
     * @return the result of the search.
     */
    @GetMapping("/_search/measurement-types")
    public List<MeasurementType> searchMeasurementTypes(@RequestParam String query) {
        log.debug("REST request to search MeasurementTypes for query {}", query);
        return measurementTypeService.search(query);
    }
}
