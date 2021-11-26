package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.WorkoutRoutine;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link WorkoutRoutine}.
 */
public interface WorkoutRoutineService {
    /**
     * Save a workoutRoutine.
     *
     * @param workoutRoutine the entity to save.
     * @return the persisted entity.
     */
    WorkoutRoutine save(WorkoutRoutine workoutRoutine);

    /**
     * Partially updates a workoutRoutine.
     *
     * @param workoutRoutine the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkoutRoutine> partialUpdate(WorkoutRoutine workoutRoutine);

    /**
     * Get all the workoutRoutines.
     *
     * @return the list of entities.
     */
    List<WorkoutRoutine> findAll();

    /**
     * Get the "id" workoutRoutine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkoutRoutine> findOne(Long id);

    /**
     * Delete the "id" workoutRoutine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workoutRoutine corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<WorkoutRoutine> search(String query);
}
