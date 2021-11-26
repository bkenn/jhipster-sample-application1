package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Workout;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Workout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    @Query("select workout from Workout workout where workout.user.login = ?#{principal.preferredUsername}")
    List<Workout> findByUserIsCurrentUser();
}
