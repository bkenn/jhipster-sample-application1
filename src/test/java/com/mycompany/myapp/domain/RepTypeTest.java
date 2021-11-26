package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RepTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RepType.class);
        RepType repType1 = new RepType();
        repType1.setId(1L);
        RepType repType2 = new RepType();
        repType2.setId(repType1.getId());
        assertThat(repType1).isEqualTo(repType2);
        repType2.setId(2L);
        assertThat(repType1).isNotEqualTo(repType2);
        repType1.setId(null);
        assertThat(repType1).isNotEqualTo(repType2);
    }
}
