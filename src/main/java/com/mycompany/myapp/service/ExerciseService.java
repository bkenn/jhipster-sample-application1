package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Exercise;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Exercise}.
 */
public interface ExerciseService {
    /**
     * Save a exercise.
     *
     * @param exercise the entity to save.
     * @return the persisted entity.
     */
    Exercise save(Exercise exercise);

    /**
     * Partially updates a exercise.
     *
     * @param exercise the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Exercise> partialUpdate(Exercise exercise);

    /**
     * Get all the exercises.
     *
     * @return the list of entities.
     */
    List<Exercise> findAll();

    /**
     * Get all the exercises with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Exercise> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" exercise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Exercise> findOne(Long id);

    /**
     * Delete the "id" exercise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the exercise corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<Exercise> search(String query);
}
