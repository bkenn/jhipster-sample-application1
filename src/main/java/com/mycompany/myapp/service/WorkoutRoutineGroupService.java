package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.WorkoutRoutineGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link WorkoutRoutineGroup}.
 */
public interface WorkoutRoutineGroupService {
    /**
     * Save a workoutRoutineGroup.
     *
     * @param workoutRoutineGroup the entity to save.
     * @return the persisted entity.
     */
    WorkoutRoutineGroup save(WorkoutRoutineGroup workoutRoutineGroup);

    /**
     * Partially updates a workoutRoutineGroup.
     *
     * @param workoutRoutineGroup the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkoutRoutineGroup> partialUpdate(WorkoutRoutineGroup workoutRoutineGroup);

    /**
     * Get all the workoutRoutineGroups.
     *
     * @return the list of entities.
     */
    List<WorkoutRoutineGroup> findAll();

    /**
     * Get all the workoutRoutineGroups with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkoutRoutineGroup> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" workoutRoutineGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkoutRoutineGroup> findOne(Long id);

    /**
     * Delete the "id" workoutRoutineGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workoutRoutineGroup corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<WorkoutRoutineGroup> search(String query);
}
