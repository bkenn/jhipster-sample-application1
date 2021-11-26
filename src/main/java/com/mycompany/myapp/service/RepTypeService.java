package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.RepType;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link RepType}.
 */
public interface RepTypeService {
    /**
     * Save a repType.
     *
     * @param repType the entity to save.
     * @return the persisted entity.
     */
    RepType save(RepType repType);

    /**
     * Partially updates a repType.
     *
     * @param repType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RepType> partialUpdate(RepType repType);

    /**
     * Get all the repTypes.
     *
     * @return the list of entities.
     */
    List<RepType> findAll();

    /**
     * Get the "id" repType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RepType> findOne(Long id);

    /**
     * Delete the "id" repType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the repType corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<RepType> search(String query);
}
