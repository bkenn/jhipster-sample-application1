package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.WorkoutRoutineExercise;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link WorkoutRoutineExercise}.
 */
public interface WorkoutRoutineExerciseService {
    /**
     * Save a workoutRoutineExercise.
     *
     * @param workoutRoutineExercise the entity to save.
     * @return the persisted entity.
     */
    WorkoutRoutineExercise save(WorkoutRoutineExercise workoutRoutineExercise);

    /**
     * Partially updates a workoutRoutineExercise.
     *
     * @param workoutRoutineExercise the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkoutRoutineExercise> partialUpdate(WorkoutRoutineExercise workoutRoutineExercise);

    /**
     * Get all the workoutRoutineExercises.
     *
     * @return the list of entities.
     */
    List<WorkoutRoutineExercise> findAll();

    /**
     * Get the "id" workoutRoutineExercise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkoutRoutineExercise> findOne(Long id);

    /**
     * Delete the "id" workoutRoutineExercise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workoutRoutineExercise corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<WorkoutRoutineExercise> search(String query);
}
