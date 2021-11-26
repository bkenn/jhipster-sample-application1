package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WorkoutRoutineGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkoutRoutineGroup entity.
 */
@Repository
public interface WorkoutRoutineGroupRepository extends JpaRepository<WorkoutRoutineGroup, Long> {
    @Query(
        value = "select distinct workoutRoutineGroup from WorkoutRoutineGroup workoutRoutineGroup left join fetch workoutRoutineGroup.workoutRoutines",
        countQuery = "select count(distinct workoutRoutineGroup) from WorkoutRoutineGroup workoutRoutineGroup"
    )
    Page<WorkoutRoutineGroup> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct workoutRoutineGroup from WorkoutRoutineGroup workoutRoutineGroup left join fetch workoutRoutineGroup.workoutRoutines"
    )
    List<WorkoutRoutineGroup> findAllWithEagerRelationships();

    @Query(
        "select workoutRoutineGroup from WorkoutRoutineGroup workoutRoutineGroup left join fetch workoutRoutineGroup.workoutRoutines where workoutRoutineGroup.id =:id"
    )
    Optional<WorkoutRoutineGroup> findOneWithEagerRelationships(@Param("id") Long id);
}
