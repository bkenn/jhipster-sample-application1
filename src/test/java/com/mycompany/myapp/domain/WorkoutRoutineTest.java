package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutRoutineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkoutRoutine.class);
        WorkoutRoutine workoutRoutine1 = new WorkoutRoutine();
        workoutRoutine1.setId(1L);
        WorkoutRoutine workoutRoutine2 = new WorkoutRoutine();
        workoutRoutine2.setId(workoutRoutine1.getId());
        assertThat(workoutRoutine1).isEqualTo(workoutRoutine2);
        workoutRoutine2.setId(2L);
        assertThat(workoutRoutine1).isNotEqualTo(workoutRoutine2);
        workoutRoutine1.setId(null);
        assertThat(workoutRoutine1).isNotEqualTo(workoutRoutine2);
    }
}
