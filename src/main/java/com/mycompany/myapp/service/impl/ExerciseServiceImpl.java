package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Exercise;
import com.mycompany.myapp.repository.ExerciseRepository;
import com.mycompany.myapp.repository.search.ExerciseSearchRepository;
import com.mycompany.myapp.service.ExerciseService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Exercise}.
 */
@Service
@Transactional
public class ExerciseServiceImpl implements ExerciseService {

    private final Logger log = LoggerFactory.getLogger(ExerciseServiceImpl.class);

    private final ExerciseRepository exerciseRepository;

    private final ExerciseSearchRepository exerciseSearchRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ExerciseSearchRepository exerciseSearchRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseSearchRepository = exerciseSearchRepository;
    }

    @Override
    public Exercise save(Exercise exercise) {
        log.debug("Request to save Exercise : {}", exercise);
        Exercise result = exerciseRepository.save(exercise);
        exerciseSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Exercise> partialUpdate(Exercise exercise) {
        log.debug("Request to partially update Exercise : {}", exercise);

        return exerciseRepository
            .findById(exercise.getId())
            .map(existingExercise -> {
                if (exercise.getName() != null) {
                    existingExercise.setName(exercise.getName());
                }
                if (exercise.getDescription() != null) {
                    existingExercise.setDescription(exercise.getDescription());
                }

                return existingExercise;
            })
            .map(exerciseRepository::save)
            .map(savedExercise -> {
                exerciseSearchRepository.save(savedExercise);

                return savedExercise;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Exercise> findAll() {
        log.debug("Request to get all Exercises");
        return exerciseRepository.findAllWithEagerRelationships();
    }

    public Page<Exercise> findAllWithEagerRelationships(Pageable pageable) {
        return exerciseRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Exercise> findOne(Long id) {
        log.debug("Request to get Exercise : {}", id);
        return exerciseRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Exercise : {}", id);
        exerciseRepository.deleteById(id);
        exerciseSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Exercise> search(String query) {
        log.debug("Request to search Exercises for query {}", query);
        return StreamSupport.stream(exerciseSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
