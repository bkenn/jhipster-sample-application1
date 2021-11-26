package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.WorkoutExerciseSet;
import com.mycompany.myapp.repository.WorkoutExerciseSetRepository;
import com.mycompany.myapp.repository.search.WorkoutExerciseSetSearchRepository;
import com.mycompany.myapp.service.WorkoutExerciseSetService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkoutExerciseSet}.
 */
@Service
@Transactional
public class WorkoutExerciseSetServiceImpl implements WorkoutExerciseSetService {

    private final Logger log = LoggerFactory.getLogger(WorkoutExerciseSetServiceImpl.class);

    private final WorkoutExerciseSetRepository workoutExerciseSetRepository;

    private final WorkoutExerciseSetSearchRepository workoutExerciseSetSearchRepository;

    public WorkoutExerciseSetServiceImpl(
        WorkoutExerciseSetRepository workoutExerciseSetRepository,
        WorkoutExerciseSetSearchRepository workoutExerciseSetSearchRepository
    ) {
        this.workoutExerciseSetRepository = workoutExerciseSetRepository;
        this.workoutExerciseSetSearchRepository = workoutExerciseSetSearchRepository;
    }

    @Override
    public WorkoutExerciseSet save(WorkoutExerciseSet workoutExerciseSet) {
        log.debug("Request to save WorkoutExerciseSet : {}", workoutExerciseSet);
        WorkoutExerciseSet result = workoutExerciseSetRepository.save(workoutExerciseSet);
        workoutExerciseSetSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<WorkoutExerciseSet> partialUpdate(WorkoutExerciseSet workoutExerciseSet) {
        log.debug("Request to partially update WorkoutExerciseSet : {}", workoutExerciseSet);

        return workoutExerciseSetRepository
            .findById(workoutExerciseSet.getId())
            .map(existingWorkoutExerciseSet -> {
                if (workoutExerciseSet.getReps() != null) {
                    existingWorkoutExerciseSet.setReps(workoutExerciseSet.getReps());
                }
                if (workoutExerciseSet.getWeight() != null) {
                    existingWorkoutExerciseSet.setWeight(workoutExerciseSet.getWeight());
                }
                if (workoutExerciseSet.getTime() != null) {
                    existingWorkoutExerciseSet.setTime(workoutExerciseSet.getTime());
                }
                if (workoutExerciseSet.getComplete() != null) {
                    existingWorkoutExerciseSet.setComplete(workoutExerciseSet.getComplete());
                }
                if (workoutExerciseSet.getCompleteTime() != null) {
                    existingWorkoutExerciseSet.setCompleteTime(workoutExerciseSet.getCompleteTime());
                }

                return existingWorkoutExerciseSet;
            })
            .map(workoutExerciseSetRepository::save)
            .map(savedWorkoutExerciseSet -> {
                workoutExerciseSetSearchRepository.save(savedWorkoutExerciseSet);

                return savedWorkoutExerciseSet;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutExerciseSet> findAll() {
        log.debug("Request to get all WorkoutExerciseSets");
        return workoutExerciseSetRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutExerciseSet> findOne(Long id) {
        log.debug("Request to get WorkoutExerciseSet : {}", id);
        return workoutExerciseSetRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkoutExerciseSet : {}", id);
        workoutExerciseSetRepository.deleteById(id);
        workoutExerciseSetSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutExerciseSet> search(String query) {
        log.debug("Request to search WorkoutExerciseSets for query {}", query);
        return StreamSupport.stream(workoutExerciseSetSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
