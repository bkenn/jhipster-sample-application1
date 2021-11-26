package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ExerciseImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExerciseImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExerciseImageRepository extends JpaRepository<ExerciseImage, Long> {}
