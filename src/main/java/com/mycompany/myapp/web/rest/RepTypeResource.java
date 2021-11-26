package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.RepType;
import com.mycompany.myapp.repository.RepTypeRepository;
import com.mycompany.myapp.service.RepTypeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.RepType}.
 */
@RestController
@RequestMapping("/api")
public class RepTypeResource {

    private final Logger log = LoggerFactory.getLogger(RepTypeResource.class);

    private static final String ENTITY_NAME = "repType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RepTypeService repTypeService;

    private final RepTypeRepository repTypeRepository;

    public RepTypeResource(RepTypeService repTypeService, RepTypeRepository repTypeRepository) {
        this.repTypeService = repTypeService;
        this.repTypeRepository = repTypeRepository;
    }

    /**
     * {@code POST  /rep-types} : Create a new repType.
     *
     * @param repType the repType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new repType, or with status {@code 400 (Bad Request)} if the repType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rep-types")
    public ResponseEntity<RepType> createRepType(@RequestBody RepType repType) throws URISyntaxException {
        log.debug("REST request to save RepType : {}", repType);
        if (repType.getId() != null) {
            throw new BadRequestAlertException("A new repType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RepType result = repTypeService.save(repType);
        return ResponseEntity
            .created(new URI("/api/rep-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rep-types/:id} : Updates an existing repType.
     *
     * @param id the id of the repType to save.
     * @param repType the repType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repType,
     * or with status {@code 400 (Bad Request)} if the repType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the repType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rep-types/{id}")
    public ResponseEntity<RepType> updateRepType(@PathVariable(value = "id", required = false) final Long id, @RequestBody RepType repType)
        throws URISyntaxException {
        log.debug("REST request to update RepType : {}, {}", id, repType);
        if (repType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RepType result = repTypeService.save(repType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rep-types/:id} : Partial updates given fields of an existing repType, field will ignore if it is null
     *
     * @param id the id of the repType to save.
     * @param repType the repType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repType,
     * or with status {@code 400 (Bad Request)} if the repType is not valid,
     * or with status {@code 404 (Not Found)} if the repType is not found,
     * or with status {@code 500 (Internal Server Error)} if the repType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rep-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RepType> partialUpdateRepType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RepType repType
    ) throws URISyntaxException {
        log.debug("REST request to partial update RepType partially : {}, {}", id, repType);
        if (repType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RepType> result = repTypeService.partialUpdate(repType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repType.getId().toString())
        );
    }

    /**
     * {@code GET  /rep-types} : get all the repTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of repTypes in body.
     */
    @GetMapping("/rep-types")
    public List<RepType> getAllRepTypes() {
        log.debug("REST request to get all RepTypes");
        return repTypeService.findAll();
    }

    /**
     * {@code GET  /rep-types/:id} : get the "id" repType.
     *
     * @param id the id of the repType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the repType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rep-types/{id}")
    public ResponseEntity<RepType> getRepType(@PathVariable Long id) {
        log.debug("REST request to get RepType : {}", id);
        Optional<RepType> repType = repTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(repType);
    }

    /**
     * {@code DELETE  /rep-types/:id} : delete the "id" repType.
     *
     * @param id the id of the repType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rep-types/{id}")
    public ResponseEntity<Void> deleteRepType(@PathVariable Long id) {
        log.debug("REST request to delete RepType : {}", id);
        repTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/rep-types?query=:query} : search for the repType corresponding
     * to the query.
     *
     * @param query the query of the repType search.
     * @return the result of the search.
     */
    @GetMapping("/_search/rep-types")
    public List<RepType> searchRepTypes(@RequestParam String query) {
        log.debug("REST request to search RepTypes for query {}", query);
        return repTypeService.search(query);
    }
}
