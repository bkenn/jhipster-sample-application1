package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.WorkoutRoutineGroup;
import com.mycompany.myapp.repository.WorkoutRoutineGroupRepository;
import com.mycompany.myapp.repository.search.WorkoutRoutineGroupSearchRepository;
import com.mycompany.myapp.service.WorkoutRoutineGroupService;
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
 * Service Implementation for managing {@link WorkoutRoutineGroup}.
 */
@Service
@Transactional
public class WorkoutRoutineGroupServiceImpl implements WorkoutRoutineGroupService {

    private final Logger log = LoggerFactory.getLogger(WorkoutRoutineGroupServiceImpl.class);

    private final WorkoutRoutineGroupRepository workoutRoutineGroupRepository;

    private final WorkoutRoutineGroupSearchRepository workoutRoutineGroupSearchRepository;

    public WorkoutRoutineGroupServiceImpl(
        WorkoutRoutineGroupRepository workoutRoutineGroupRepository,
        WorkoutRoutineGroupSearchRepository workoutRoutineGroupSearchRepository
    ) {
        this.workoutRoutineGroupRepository = workoutRoutineGroupRepository;
        this.workoutRoutineGroupSearchRepository = workoutRoutineGroupSearchRepository;
    }

    @Override
    public WorkoutRoutineGroup save(WorkoutRoutineGroup workoutRoutineGroup) {
        log.debug("Request to save WorkoutRoutineGroup : {}", workoutRoutineGroup);
        WorkoutRoutineGroup result = workoutRoutineGroupRepository.save(workoutRoutineGroup);
        workoutRoutineGroupSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<WorkoutRoutineGroup> partialUpdate(WorkoutRoutineGroup workoutRoutineGroup) {
        log.debug("Request to partially update WorkoutRoutineGroup : {}", workoutRoutineGroup);

        return workoutRoutineGroupRepository
            .findById(workoutRoutineGroup.getId())
            .map(existingWorkoutRoutineGroup -> {
                if (workoutRoutineGroup.getName() != null) {
                    existingWorkoutRoutineGroup.setName(workoutRoutineGroup.getName());
                }

                return existingWorkoutRoutineGroup;
            })
            .map(workoutRoutineGroupRepository::save)
            .map(savedWorkoutRoutineGroup -> {
                workoutRoutineGroupSearchRepository.save(savedWorkoutRoutineGroup);

                return savedWorkoutRoutineGroup;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutRoutineGroup> findAll() {
        log.debug("Request to get all WorkoutRoutineGroups");
        return workoutRoutineGroupRepository.findAllWithEagerRelationships();
    }

    public Page<WorkoutRoutineGroup> findAllWithEagerRelationships(Pageable pageable) {
        return workoutRoutineGroupRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutRoutineGroup> findOne(Long id) {
        log.debug("Request to get WorkoutRoutineGroup : {}", id);
        return workoutRoutineGroupRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkoutRoutineGroup : {}", id);
        workoutRoutineGroupRepository.deleteById(id);
        workoutRoutineGroupSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutRoutineGroup> search(String query) {
        log.debug("Request to search WorkoutRoutineGroups for query {}", query);
        return StreamSupport.stream(workoutRoutineGroupSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
