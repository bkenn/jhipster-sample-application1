package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutRoutineGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkoutRoutineGroup.class);
        WorkoutRoutineGroup workoutRoutineGroup1 = new WorkoutRoutineGroup();
        workoutRoutineGroup1.setId(1L);
        WorkoutRoutineGroup workoutRoutineGroup2 = new WorkoutRoutineGroup();
        workoutRoutineGroup2.setId(workoutRoutineGroup1.getId());
        assertThat(workoutRoutineGroup1).isEqualTo(workoutRoutineGroup2);
        workoutRoutineGroup2.setId(2L);
        assertThat(workoutRoutineGroup1).isNotEqualTo(workoutRoutineGroup2);
        workoutRoutineGroup1.setId(null);
        assertThat(workoutRoutineGroup1).isNotEqualTo(workoutRoutineGroup2);
    }
}
