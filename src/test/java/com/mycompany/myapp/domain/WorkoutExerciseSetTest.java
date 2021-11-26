package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutExerciseSetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkoutExerciseSet.class);
        WorkoutExerciseSet workoutExerciseSet1 = new WorkoutExerciseSet();
        workoutExerciseSet1.setId(1L);
        WorkoutExerciseSet workoutExerciseSet2 = new WorkoutExerciseSet();
        workoutExerciseSet2.setId(workoutExerciseSet1.getId());
        assertThat(workoutExerciseSet1).isEqualTo(workoutExerciseSet2);
        workoutExerciseSet2.setId(2L);
        assertThat(workoutExerciseSet1).isNotEqualTo(workoutExerciseSet2);
        workoutExerciseSet1.setId(null);
        assertThat(workoutExerciseSet1).isNotEqualTo(workoutExerciseSet2);
    }
}
