package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.WorkoutExerciseSet;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link WorkoutExerciseSet}.
 */
public interface WorkoutExerciseSetService {
    /**
     * Save a workoutExerciseSet.
     *
     * @param workoutExerciseSet the entity to save.
     * @return the persisted entity.
     */
    WorkoutExerciseSet save(WorkoutExerciseSet workoutExerciseSet);

    /**
     * Partially updates a workoutExerciseSet.
     *
     * @param workoutExerciseSet the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkoutExerciseSet> partialUpdate(WorkoutExerciseSet workoutExerciseSet);

    /**
     * Get all the workoutExerciseSets.
     *
     * @return the list of entities.
     */
    List<WorkoutExerciseSet> findAll();

    /**
     * Get the "id" workoutExerciseSet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkoutExerciseSet> findOne(Long id);

    /**
     * Delete the "id" workoutExerciseSet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workoutExerciseSet corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<WorkoutExerciseSet> search(String query);
}
