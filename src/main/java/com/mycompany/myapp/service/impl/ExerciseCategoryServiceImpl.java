package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.ExerciseCategory;
import com.mycompany.myapp.repository.ExerciseCategoryRepository;
import com.mycompany.myapp.repository.search.ExerciseCategorySearchRepository;
import com.mycompany.myapp.service.ExerciseCategoryService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExerciseCategory}.
 */
@Service
@Transactional
public class ExerciseCategoryServiceImpl implements ExerciseCategoryService {

    private final Logger log = LoggerFactory.getLogger(ExerciseCategoryServiceImpl.class);

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    private final ExerciseCategorySearchRepository exerciseCategorySearchRepository;

    public ExerciseCategoryServiceImpl(
        ExerciseCategoryRepository exerciseCategoryRepository,
        ExerciseCategorySearchRepository exerciseCategorySearchRepository
    ) {
        this.exerciseCategoryRepository = exerciseCategoryRepository;
        this.exerciseCategorySearchRepository = exerciseCategorySearchRepository;
    }

    @Override
    public ExerciseCategory save(ExerciseCategory exerciseCategory) {
        log.debug("Request to save ExerciseCategory : {}", exerciseCategory);
        ExerciseCategory result = exerciseCategoryRepository.save(exerciseCategory);
        exerciseCategorySearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<ExerciseCategory> partialUpdate(ExerciseCategory exerciseCategory) {
        log.debug("Request to partially update ExerciseCategory : {}", exerciseCategory);

        return exerciseCategoryRepository
            .findById(exerciseCategory.getId())
            .map(existingExerciseCategory -> {
                if (exerciseCategory.getName() != null) {
                    existingExerciseCategory.setName(exerciseCategory.getName());
                }
                if (exerciseCategory.getCategoryOrder() != null) {
                    existingExerciseCategory.setCategoryOrder(exerciseCategory.getCategoryOrder());
                }

                return existingExerciseCategory;
            })
            .map(exerciseCategoryRepository::save)
            .map(savedExerciseCategory -> {
                exerciseCategorySearchRepository.save(savedExerciseCategory);

                return savedExerciseCategory;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExerciseCategory> findAll() {
        log.debug("Request to get all ExerciseCategories");
        return exerciseCategoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseCategory> findOne(Long id) {
        log.debug("Request to get ExerciseCategory : {}", id);
        return exerciseCategoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExerciseCategory : {}", id);
        exerciseCategoryRepository.deleteById(id);
        exerciseCategorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExerciseCategory> search(String query) {
        log.debug("Request to search ExerciseCategories for query {}", query);
        return StreamSupport.stream(exerciseCategorySearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
