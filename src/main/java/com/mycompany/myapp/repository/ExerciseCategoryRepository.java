package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ExerciseCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExerciseCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExerciseCategoryRepository extends JpaRepository<ExerciseCategory, Long> {}
