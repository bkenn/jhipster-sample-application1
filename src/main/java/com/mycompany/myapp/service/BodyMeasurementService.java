package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BodyMeasurement;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link BodyMeasurement}.
 */
public interface BodyMeasurementService {
    /**
     * Save a bodyMeasurement.
     *
     * @param bodyMeasurement the entity to save.
     * @return the persisted entity.
     */
    BodyMeasurement save(BodyMeasurement bodyMeasurement);

    /**
     * Partially updates a bodyMeasurement.
     *
     * @param bodyMeasurement the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BodyMeasurement> partialUpdate(BodyMeasurement bodyMeasurement);

    /**
     * Get all the bodyMeasurements.
     *
     * @return the list of entities.
     */
    List<BodyMeasurement> findAll();

    /**
     * Get the "id" bodyMeasurement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BodyMeasurement> findOne(Long id);

    /**
     * Delete the "id" bodyMeasurement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the bodyMeasurement corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<BodyMeasurement> search(String query);
}
