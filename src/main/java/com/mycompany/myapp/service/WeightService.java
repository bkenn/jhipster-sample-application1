package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Weight;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Weight}.
 */
public interface WeightService {
    /**
     * Save a weight.
     *
     * @param weight the entity to save.
     * @return the persisted entity.
     */
    Weight save(Weight weight);

    /**
     * Partially updates a weight.
     *
     * @param weight the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Weight> partialUpdate(Weight weight);

    /**
     * Get all the weights.
     *
     * @return the list of entities.
     */
    List<Weight> findAll();

    /**
     * Get the "id" weight.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Weight> findOne(Long id);

    /**
     * Delete the "id" weight.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the weight corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<Weight> search(String query);
}
