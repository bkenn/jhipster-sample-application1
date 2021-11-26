package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.WorkoutRoutineExerciseSet;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link WorkoutRoutineExerciseSet}.
 */
public interface WorkoutRoutineExerciseSetService {
    /**
     * Save a workoutRoutineExerciseSet.
     *
     * @param workoutRoutineExerciseSet the entity to save.
     * @return the persisted entity.
     */
    WorkoutRoutineExerciseSet save(WorkoutRoutineExerciseSet workoutRoutineExerciseSet);

    /**
     * Partially updates a workoutRoutineExerciseSet.
     *
     * @param workoutRoutineExerciseSet the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkoutRoutineExerciseSet> partialUpdate(WorkoutRoutineExerciseSet workoutRoutineExerciseSet);

    /**
     * Get all the workoutRoutineExerciseSets.
     *
     * @return the list of entities.
     */
    List<WorkoutRoutineExerciseSet> findAll();

    /**
     * Get the "id" workoutRoutineExerciseSet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkoutRoutineExerciseSet> findOne(Long id);

    /**
     * Delete the "id" workoutRoutineExerciseSet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workoutRoutineExerciseSet corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<WorkoutRoutineExerciseSet> search(String query);
}
