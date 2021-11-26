package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WorkoutRoutineExerciseSet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkoutRoutineExerciseSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkoutRoutineExerciseSetRepository extends JpaRepository<WorkoutRoutineExerciseSet, Long> {}
