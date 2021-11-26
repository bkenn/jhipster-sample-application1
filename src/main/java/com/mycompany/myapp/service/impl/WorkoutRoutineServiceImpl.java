package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.WorkoutRoutine;
import com.mycompany.myapp.repository.WorkoutRoutineRepository;
import com.mycompany.myapp.repository.search.WorkoutRoutineSearchRepository;
import com.mycompany.myapp.service.WorkoutRoutineService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkoutRoutine}.
 */
@Service
@Transactional
public class WorkoutRoutineServiceImpl implements WorkoutRoutineService {

    private final Logger log = LoggerFactory.getLogger(WorkoutRoutineServiceImpl.class);

    private final WorkoutRoutineRepository workoutRoutineRepository;

    private final WorkoutRoutineSearchRepository workoutRoutineSearchRepository;

    public WorkoutRoutineServiceImpl(
        WorkoutRoutineRepository workoutRoutineRepository,
        WorkoutRoutineSearchRepository workoutRoutineSearchRepository
    ) {
        this.workoutRoutineRepository = workoutRoutineRepository;
        this.workoutRoutineSearchRepository = workoutRoutineSearchRepository;
    }

    @Override
    public WorkoutRoutine save(WorkoutRoutine workoutRoutine) {
        log.debug("Request to save WorkoutRoutine : {}", workoutRoutine);
        WorkoutRoutine result = workoutRoutineRepository.save(workoutRoutine);
        workoutRoutineSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<WorkoutRoutine> partialUpdate(WorkoutRoutine workoutRoutine) {
        log.debug("Request to partially update WorkoutRoutine : {}", workoutRoutine);

        return workoutRoutineRepository
            .findById(workoutRoutine.getId())
            .map(existingWorkoutRoutine -> {
                if (workoutRoutine.getTitle() != null) {
                    existingWorkoutRoutine.setTitle(workoutRoutine.getTitle());
                }
                if (workoutRoutine.getDescription() != null) {
                    existingWorkoutRoutine.setDescription(workoutRoutine.getDescription());
                }

                return existingWorkoutRoutine;
            })
            .map(workoutRoutineRepository::save)
            .map(savedWorkoutRoutine -> {
                workoutRoutineSearchRepository.save(savedWorkoutRoutine);

                return savedWorkoutRoutine;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutRoutine> findAll() {
        log.debug("Request to get all WorkoutRoutines");
        return workoutRoutineRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutRoutine> findOne(Long id) {
        log.debug("Request to get WorkoutRoutine : {}", id);
        return workoutRoutineRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkoutRoutine : {}", id);
        workoutRoutineRepository.deleteById(id);
        workoutRoutineSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutRoutine> search(String query) {
        log.debug("Request to search WorkoutRoutines for query {}", query);
        return StreamSupport.stream(workoutRoutineSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
