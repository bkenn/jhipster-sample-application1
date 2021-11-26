package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WorkoutExercise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkoutExercise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {}
