package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutRoutineExerciseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkoutRoutineExercise.class);
        WorkoutRoutineExercise workoutRoutineExercise1 = new WorkoutRoutineExercise();
        workoutRoutineExercise1.setId(1L);
        WorkoutRoutineExercise workoutRoutineExercise2 = new WorkoutRoutineExercise();
        workoutRoutineExercise2.setId(workoutRoutineExercise1.getId());
        assertThat(workoutRoutineExercise1).isEqualTo(workoutRoutineExercise2);
        workoutRoutineExercise2.setId(2L);
        assertThat(workoutRoutineExercise1).isNotEqualTo(workoutRoutineExercise2);
        workoutRoutineExercise1.setId(null);
        assertThat(workoutRoutineExercise1).isNotEqualTo(workoutRoutineExercise2);
    }
}
