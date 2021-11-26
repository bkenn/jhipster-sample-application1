package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProgressPhotoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgressPhoto.class);
        ProgressPhoto progressPhoto1 = new ProgressPhoto();
        progressPhoto1.setId(1L);
        ProgressPhoto progressPhoto2 = new ProgressPhoto();
        progressPhoto2.setId(progressPhoto1.getId());
        assertThat(progressPhoto1).isEqualTo(progressPhoto2);
        progressPhoto2.setId(2L);
        assertThat(progressPhoto1).isNotEqualTo(progressPhoto2);
        progressPhoto1.setId(null);
        assertThat(progressPhoto1).isNotEqualTo(progressPhoto2);
    }
}
