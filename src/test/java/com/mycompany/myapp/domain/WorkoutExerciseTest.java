package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutExerciseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkoutExercise.class);
        WorkoutExercise workoutExercise1 = new WorkoutExercise();
        workoutExercise1.setId(1L);
        WorkoutExercise workoutExercise2 = new WorkoutExercise();
        workoutExercise2.setId(workoutExercise1.getId());
        assertThat(workoutExercise1).isEqualTo(workoutExercise2);
        workoutExercise2.setId(2L);
        assertThat(workoutExercise1).isNotEqualTo(workoutExercise2);
        workoutExercise1.setId(null);
        assertThat(workoutExercise1).isNotEqualTo(workoutExercise2);
    }
}
