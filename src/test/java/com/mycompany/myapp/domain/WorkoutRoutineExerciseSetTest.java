package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutRoutineExerciseSetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkoutRoutineExerciseSet.class);
        WorkoutRoutineExerciseSet workoutRoutineExerciseSet1 = new WorkoutRoutineExerciseSet();
        workoutRoutineExerciseSet1.setId(1L);
        WorkoutRoutineExerciseSet workoutRoutineExerciseSet2 = new WorkoutRoutineExerciseSet();
        workoutRoutineExerciseSet2.setId(workoutRoutineExerciseSet1.getId());
        assertThat(workoutRoutineExerciseSet1).isEqualTo(workoutRoutineExerciseSet2);
        workoutRoutineExerciseSet2.setId(2L);
        assertThat(workoutRoutineExerciseSet1).isNotEqualTo(workoutRoutineExerciseSet2);
        workoutRoutineExerciseSet1.setId(null);
        assertThat(workoutRoutineExerciseSet1).isNotEqualTo(workoutRoutineExerciseSet2);
    }
}
