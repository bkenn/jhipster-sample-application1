package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Workout;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Workout}.
 */
public interface WorkoutService {
    /**
     * Save a workout.
     *
     * @param workout the entity to save.
     * @return the persisted entity.
     */
    Workout save(Workout workout);

    /**
     * Partially updates a workout.
     *
     * @param workout the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Workout> partialUpdate(Workout workout);

    /**
     * Get all the workouts.
     *
     * @return the list of entities.
     */
    List<Workout> findAll();

    /**
     * Get the "id" workout.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Workout> findOne(Long id);

    /**
     * Delete the "id" workout.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workout corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<Workout> search(String query);
}
