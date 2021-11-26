package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.MeasurementType;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link MeasurementType}.
 */
public interface MeasurementTypeService {
    /**
     * Save a measurementType.
     *
     * @param measurementType the entity to save.
     * @return the persisted entity.
     */
    MeasurementType save(MeasurementType measurementType);

    /**
     * Partially updates a measurementType.
     *
     * @param measurementType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MeasurementType> partialUpdate(MeasurementType measurementType);

    /**
     * Get all the measurementTypes.
     *
     * @return the list of entities.
     */
    List<MeasurementType> findAll();

    /**
     * Get the "id" measurementType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MeasurementType> findOne(Long id);

    /**
     * Delete the "id" measurementType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the measurementType corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<MeasurementType> search(String query);
}
