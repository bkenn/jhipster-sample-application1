package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ExerciseImage;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ExerciseImage}.
 */
public interface ExerciseImageService {
    /**
     * Save a exerciseImage.
     *
     * @param exerciseImage the entity to save.
     * @return the persisted entity.
     */
    ExerciseImage save(ExerciseImage exerciseImage);

    /**
     * Partially updates a exerciseImage.
     *
     * @param exerciseImage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExerciseImage> partialUpdate(ExerciseImage exerciseImage);

    /**
     * Get all the exerciseImages.
     *
     * @return the list of entities.
     */
    List<ExerciseImage> findAll();

    /**
     * Get the "id" exerciseImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseImage> findOne(Long id);

    /**
     * Delete the "id" exerciseImage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the exerciseImage corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<ExerciseImage> search(String query);
}
