package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WorkoutExerciseSet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkoutExerciseSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkoutExerciseSetRepository extends JpaRepository<WorkoutExerciseSet, Long> {}
