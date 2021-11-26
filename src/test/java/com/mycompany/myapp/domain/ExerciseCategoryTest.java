package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExerciseCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseCategory.class);
        ExerciseCategory exerciseCategory1 = new ExerciseCategory();
        exerciseCategory1.setId(1L);
        ExerciseCategory exerciseCategory2 = new ExerciseCategory();
        exerciseCategory2.setId(exerciseCategory1.getId());
        assertThat(exerciseCategory1).isEqualTo(exerciseCategory2);
        exerciseCategory2.setId(2L);
        assertThat(exerciseCategory1).isNotEqualTo(exerciseCategory2);
        exerciseCategory1.setId(null);
        assertThat(exerciseCategory1).isNotEqualTo(exerciseCategory2);
    }
}
