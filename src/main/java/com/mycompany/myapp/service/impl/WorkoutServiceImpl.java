package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Workout;
import com.mycompany.myapp.repository.WorkoutRepository;
import com.mycompany.myapp.repository.search.WorkoutSearchRepository;
import com.mycompany.myapp.service.WorkoutService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Workout}.
 */
@Service
@Transactional
public class WorkoutServiceImpl implements WorkoutService {

    private final Logger log = LoggerFactory.getLogger(WorkoutServiceImpl.class);

    private final WorkoutRepository workoutRepository;

    private final WorkoutSearchRepository workoutSearchRepository;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository, WorkoutSearchRepository workoutSearchRepository) {
        this.workoutRepository = workoutRepository;
        this.workoutSearchRepository = workoutSearchRepository;
    }

    @Override
    public Workout save(Workout workout) {
        log.debug("Request to save Workout : {}", workout);
        Workout result = workoutRepository.save(workout);
        workoutSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Workout> partialUpdate(Workout workout) {
        log.debug("Request to partially update Workout : {}", workout);

        return workoutRepository
            .findById(workout.getId())
            .map(existingWorkout -> {
                if (workout.getTitle() != null) {
                    existingWorkout.setTitle(workout.getTitle());
                }
                if (workout.getDescription() != null) {
                    existingWorkout.setDescription(workout.getDescription());
                }
                if (workout.getWorkoutStartDateTime() != null) {
                    existingWorkout.setWorkoutStartDateTime(workout.getWorkoutStartDateTime());
                }
                if (workout.getWorkoutEndDateTime() != null) {
                    existingWorkout.setWorkoutEndDateTime(workout.getWorkoutEndDateTime());
                }

                return existingWorkout;
            })
            .map(workoutRepository::save)
            .map(savedWorkout -> {
                workoutSearchRepository.save(savedWorkout);

                return savedWorkout;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Workout> findAll() {
        log.debug("Request to get all Workouts");
        return workoutRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Workout> findOne(Long id) {
        log.debug("Request to get Workout : {}", id);
        return workoutRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Workout : {}", id);
        workoutRepository.deleteById(id);
        workoutSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Workout> search(String query) {
        log.debug("Request to search Workouts for query {}", query);
        return StreamSupport.stream(workoutSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
