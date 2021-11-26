package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.WorkoutExercise;
import com.mycompany.myapp.repository.WorkoutExerciseRepository;
import com.mycompany.myapp.repository.search.WorkoutExerciseSearchRepository;
import com.mycompany.myapp.service.WorkoutExerciseService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkoutExercise}.
 */
@Service
@Transactional
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final Logger log = LoggerFactory.getLogger(WorkoutExerciseServiceImpl.class);

    private final WorkoutExerciseRepository workoutExerciseRepository;

    private final WorkoutExerciseSearchRepository workoutExerciseSearchRepository;

    public WorkoutExerciseServiceImpl(
        WorkoutExerciseRepository workoutExerciseRepository,
        WorkoutExerciseSearchRepository workoutExerciseSearchRepository
    ) {
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.workoutExerciseSearchRepository = workoutExerciseSearchRepository;
    }

    @Override
    public WorkoutExercise save(WorkoutExercise workoutExercise) {
        log.debug("Request to save WorkoutExercise : {}", workoutExercise);
        WorkoutExercise result = workoutExerciseRepository.save(workoutExercise);
        workoutExerciseSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<WorkoutExercise> partialUpdate(WorkoutExercise workoutExercise) {
        log.debug("Request to partially update WorkoutExercise : {}", workoutExercise);

        return workoutExerciseRepository
            .findById(workoutExercise.getId())
            .map(existingWorkoutExercise -> {
                if (workoutExercise.getNote() != null) {
                    existingWorkoutExercise.setNote(workoutExercise.getNote());
                }
                if (workoutExercise.getTimer() != null) {
                    existingWorkoutExercise.setTimer(workoutExercise.getTimer());
                }

                return existingWorkoutExercise;
            })
            .map(workoutExerciseRepository::save)
            .map(savedWorkoutExercise -> {
                workoutExerciseSearchRepository.save(savedWorkoutExercise);

                return savedWorkoutExercise;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutExercise> findAll() {
        log.debug("Request to get all WorkoutExercises");
        return workoutExerciseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutExercise> findOne(Long id) {
        log.debug("Request to get WorkoutExercise : {}", id);
        return workoutExerciseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkoutExercise : {}", id);
        workoutExerciseRepository.deleteById(id);
        workoutExerciseSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutExercise> search(String query) {
        log.debug("Request to search WorkoutExercises for query {}", query);
        return StreamSupport.stream(workoutExerciseSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
