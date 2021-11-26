package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BodyMeasurementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BodyMeasurement.class);
        BodyMeasurement bodyMeasurement1 = new BodyMeasurement();
        bodyMeasurement1.setId(1L);
        BodyMeasurement bodyMeasurement2 = new BodyMeasurement();
        bodyMeasurement2.setId(bodyMeasurement1.getId());
        assertThat(bodyMeasurement1).isEqualTo(bodyMeasurement2);
        bodyMeasurement2.setId(2L);
        assertThat(bodyMeasurement1).isNotEqualTo(bodyMeasurement2);
        bodyMeasurement1.setId(null);
        assertThat(bodyMeasurement1).isNotEqualTo(bodyMeasurement2);
    }
}
