package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Muscle;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Muscle}.
 */
public interface MuscleService {
    /**
     * Save a muscle.
     *
     * @param muscle the entity to save.
     * @return the persisted entity.
     */
    Muscle save(Muscle muscle);

    /**
     * Partially updates a muscle.
     *
     * @param muscle the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Muscle> partialUpdate(Muscle muscle);

    /**
     * Get all the muscles.
     *
     * @return the list of entities.
     */
    List<Muscle> findAll();

    /**
     * Get the "id" muscle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Muscle> findOne(Long id);

    /**
     * Delete the "id" muscle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the muscle corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<Muscle> search(String query);
}
