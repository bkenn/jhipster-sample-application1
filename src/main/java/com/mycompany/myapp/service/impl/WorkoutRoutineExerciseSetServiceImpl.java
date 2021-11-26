package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.WorkoutRoutineExerciseSet;
import com.mycompany.myapp.repository.WorkoutRoutineExerciseSetRepository;
import com.mycompany.myapp.repository.search.WorkoutRoutineExerciseSetSearchRepository;
import com.mycompany.myapp.service.WorkoutRoutineExerciseSetService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkoutRoutineExerciseSet}.
 */
@Service
@Transactional
public class WorkoutRoutineExerciseSetServiceImpl implements WorkoutRoutineExerciseSetService {

    private final Logger log = LoggerFactory.getLogger(WorkoutRoutineExerciseSetServiceImpl.class);

    private final WorkoutRoutineExerciseSetRepository workoutRoutineExerciseSetRepository;

    private final WorkoutRoutineExerciseSetSearchRepository workoutRoutineExerciseSetSearchRepository;

    public WorkoutRoutineExerciseSetServiceImpl(
        WorkoutRoutineExerciseSetRepository workoutRoutineExerciseSetRepository,
        WorkoutRoutineExerciseSetSearchRepository workoutRoutineExerciseSetSearchRepository
    ) {
        this.workoutRoutineExerciseSetRepository = workoutRoutineExerciseSetRepository;
        this.workoutRoutineExerciseSetSearchRepository = workoutRoutineExerciseSetSearchRepository;
    }

    @Override
    public WorkoutRoutineExerciseSet save(WorkoutRoutineExerciseSet workoutRoutineExerciseSet) {
        log.debug("Request to save WorkoutRoutineExerciseSet : {}", workoutRoutineExerciseSet);
        WorkoutRoutineExerciseSet result = workoutRoutineExerciseSetRepository.save(workoutRoutineExerciseSet);
        workoutRoutineExerciseSetSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<WorkoutRoutineExerciseSet> partialUpdate(WorkoutRoutineExerciseSet workoutRoutineExerciseSet) {
        log.debug("Request to partially update WorkoutRoutineExerciseSet : {}", workoutRoutineExerciseSet);

        return workoutRoutineExerciseSetRepository
            .findById(workoutRoutineExerciseSet.getId())
            .map(existingWorkoutRoutineExerciseSet -> {
                if (workoutRoutineExerciseSet.getReps() != null) {
                    existingWorkoutRoutineExerciseSet.setReps(workoutRoutineExerciseSet.getReps());
                }
                if (workoutRoutineExerciseSet.getWeight() != null) {
                    existingWorkoutRoutineExerciseSet.setWeight(workoutRoutineExerciseSet.getWeight());
                }
                if (workoutRoutineExerciseSet.getTime() != null) {
                    existingWorkoutRoutineExerciseSet.setTime(workoutRoutineExerciseSet.getTime());
                }

                return existingWorkoutRoutineExerciseSet;
            })
            .map(workoutRoutineExerciseSetRepository::save)
            .map(savedWorkoutRoutineExerciseSet -> {
                workoutRoutineExerciseSetSearchRepository.save(savedWorkoutRoutineExerciseSet);

                return savedWorkoutRoutineExerciseSet;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutRoutineExerciseSet> findAll() {
        log.debug("Request to get all WorkoutRoutineExerciseSets");
        return workoutRoutineExerciseSetRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutRoutineExerciseSet> findOne(Long id) {
        log.debug("Request to get WorkoutRoutineExerciseSet : {}", id);
        return workoutRoutineExerciseSetRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkoutRoutineExerciseSet : {}", id);
        workoutRoutineExerciseSetRepository.deleteById(id);
        workoutRoutineExerciseSetSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutRoutineExerciseSet> search(String query) {
        log.debug("Request to search WorkoutRoutineExerciseSets for query {}", query);
        return StreamSupport
            .stream(workoutRoutineExerciseSetSearchRepository.search(query).spliterator(), false)
            .collect(Collectors.toList());
    }
}
