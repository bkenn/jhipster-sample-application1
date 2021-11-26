package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ExerciseCategory;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ExerciseCategory}.
 */
public interface ExerciseCategoryService {
    /**
     * Save a exerciseCategory.
     *
     * @param exerciseCategory the entity to save.
     * @return the persisted entity.
     */
    ExerciseCategory save(ExerciseCategory exerciseCategory);

    /**
     * Partially updates a exerciseCategory.
     *
     * @param exerciseCategory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExerciseCategory> partialUpdate(ExerciseCategory exerciseCategory);

    /**
     * Get all the exerciseCategories.
     *
     * @return the list of entities.
     */
    List<ExerciseCategory> findAll();

    /**
     * Get the "id" exerciseCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseCategory> findOne(Long id);

    /**
     * Delete the "id" exerciseCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the exerciseCategory corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<ExerciseCategory> search(String query);
}
