package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExerciseImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseImage.class);
        ExerciseImage exerciseImage1 = new ExerciseImage();
        exerciseImage1.setId(1L);
        ExerciseImage exerciseImage2 = new ExerciseImage();
        exerciseImage2.setId(exerciseImage1.getId());
        assertThat(exerciseImage1).isEqualTo(exerciseImage2);
        exerciseImage2.setId(2L);
        assertThat(exerciseImage1).isNotEqualTo(exerciseImage2);
        exerciseImage1.setId(null);
        assertThat(exerciseImage1).isNotEqualTo(exerciseImage2);
    }
}
