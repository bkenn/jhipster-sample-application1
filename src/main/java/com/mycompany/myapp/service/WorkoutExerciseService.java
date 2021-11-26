package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.WorkoutExercise;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link WorkoutExercise}.
 */
public interface WorkoutExerciseService {
    /**
     * Save a workoutExercise.
     *
     * @param workoutExercise the entity to save.
     * @return the persisted entity.
     */
    WorkoutExercise save(WorkoutExercise workoutExercise);

    /**
     * Partially updates a workoutExercise.
     *
     * @param workoutExercise the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkoutExercise> partialUpdate(WorkoutExercise workoutExercise);

    /**
     * Get all the workoutExercises.
     *
     * @return the list of entities.
     */
    List<WorkoutExercise> findAll();

    /**
     * Get the "id" workoutExercise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkoutExercise> findOne(Long id);

    /**
     * Delete the "id" workoutExercise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workoutExercise corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<WorkoutExercise> search(String query);
}
