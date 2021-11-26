package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WorkoutRoutine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkoutRoutine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkoutRoutineRepository extends JpaRepository<WorkoutRoutine, Long> {}
