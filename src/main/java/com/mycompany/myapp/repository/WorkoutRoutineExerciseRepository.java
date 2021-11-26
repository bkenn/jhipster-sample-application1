package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WorkoutRoutineExercise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkoutRoutineExercise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkoutRoutineExerciseRepository extends JpaRepository<WorkoutRoutineExercise, Long> {}
