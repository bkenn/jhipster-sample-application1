package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MeasurementTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeasurementType.class);
        MeasurementType measurementType1 = new MeasurementType();
        measurementType1.setId(1L);
        MeasurementType measurementType2 = new MeasurementType();
        measurementType2.setId(measurementType1.getId());
        assertThat(measurementType1).isEqualTo(measurementType2);
        measurementType2.setId(2L);
        assertThat(measurementType1).isNotEqualTo(measurementType2);
        measurementType1.setId(null);
        assertThat(measurementType1).isNotEqualTo(measurementType2);
    }
}
