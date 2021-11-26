package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ProgressPhoto;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ProgressPhoto}.
 */
public interface ProgressPhotoService {
    /**
     * Save a progressPhoto.
     *
     * @param progressPhoto the entity to save.
     * @return the persisted entity.
     */
    ProgressPhoto save(ProgressPhoto progressPhoto);

    /**
     * Partially updates a progressPhoto.
     *
     * @param progressPhoto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProgressPhoto> partialUpdate(ProgressPhoto progressPhoto);

    /**
     * Get all the progressPhotos.
     *
     * @return the list of entities.
     */
    List<ProgressPhoto> findAll();

    /**
     * Get the "id" progressPhoto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProgressPhoto> findOne(Long id);

    /**
     * Delete the "id" progressPhoto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the progressPhoto corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<ProgressPhoto> search(String query);
}
