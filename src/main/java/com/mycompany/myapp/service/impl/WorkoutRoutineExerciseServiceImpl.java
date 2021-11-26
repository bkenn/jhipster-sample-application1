package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.WorkoutRoutineExercise;
import com.mycompany.myapp.repository.WorkoutRoutineExerciseRepository;
import com.mycompany.myapp.repository.search.WorkoutRoutineExerciseSearchRepository;
import com.mycompany.myapp.service.WorkoutRoutineExerciseService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkoutRoutineExercise}.
 */
@Service
@Transactional
public class WorkoutRoutineExerciseServiceImpl implements WorkoutRoutineExerciseService {

    private final Logger log = LoggerFactory.getLogger(WorkoutRoutineExerciseServiceImpl.class);

    private final WorkoutRoutineExerciseRepository workoutRoutineExerciseRepository;

    private final WorkoutRoutineExerciseSearchRepository workoutRoutineExerciseSearchRepository;

    public WorkoutRoutineExerciseServiceImpl(
        WorkoutRoutineExerciseRepository workoutRoutineExerciseRepository,
        WorkoutRoutineExerciseSearchRepository workoutRoutineExerciseSearchRepository
    ) {
        this.workoutRoutineExerciseRepository = workoutRoutineExerciseRepository;
        this.workoutRoutineExerciseSearchRepository = workoutRoutineExerciseSearchRepository;
    }

    @Override
    public WorkoutRoutineExercise save(WorkoutRoutineExercise workoutRoutineExercise) {
        log.debug("Request to save WorkoutRoutineExercise : {}", workoutRoutineExercise);
        WorkoutRoutineExercise result = workoutRoutineExerciseRepository.save(workoutRoutineExercise);
        workoutRoutineExerciseSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<WorkoutRoutineExercise> partialUpdate(WorkoutRoutineExercise workoutRoutineExercise) {
        log.debug("Request to partially update WorkoutRoutineExercise : {}", workoutRoutineExercise);

        return workoutRoutineExerciseRepository
            .findById(workoutRoutineExercise.getId())
            .map(existingWorkoutRoutineExercise -> {
                if (workoutRoutineExercise.getNote() != null) {
                    existingWorkoutRoutineExercise.setNote(workoutRoutineExercise.getNote());
                }
                if (workoutRoutineExercise.getTimer() != null) {
                    existingWorkoutRoutineExercise.setTimer(workoutRoutineExercise.getTimer());
                }

                return existingWorkoutRoutineExercise;
            })
            .map(workoutRoutineExerciseRepository::save)
            .map(savedWorkoutRoutineExercise -> {
                workoutRoutineExerciseSearchRepository.save(savedWorkoutRoutineExercise);

                return savedWorkoutRoutineExercise;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutRoutineExercise> findAll() {
        log.debug("Request to get all WorkoutRoutineExercises");
        return workoutRoutineExerciseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutRoutineExercise> findOne(Long id) {
        log.debug("Request to get WorkoutRoutineExercise : {}", id);
        return workoutRoutineExerciseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkoutRoutineExercise : {}", id);
        workoutRoutineExerciseRepository.deleteById(id);
        workoutRoutineExerciseSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutRoutineExercise> search(String query) {
        log.debug("Request to search WorkoutRoutineExercises for query {}", query);
        return StreamSupport.stream(workoutRoutineExerciseSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
