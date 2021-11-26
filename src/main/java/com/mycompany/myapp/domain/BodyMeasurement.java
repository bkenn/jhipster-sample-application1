package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BodyMeasurement.
 */
@Entity
@Table(name = "body_measurement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "bodymeasurement")
public class BodyMeasurement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "body_measurement_date_time")
    private ZonedDateTime bodyMeasurementDateTime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "bodyMeasurements", "weights" }, allowSetters = true)
    private MeasurementType measurementType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BodyMeasurement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return this.value;
    }

    public BodyMeasurement value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public ZonedDateTime getBodyMeasurementDateTime() {
        return this.bodyMeasurementDateTime;
    }

    public BodyMeasurement bodyMeasurementDateTime(ZonedDateTime bodyMeasurementDateTime) {
        this.setBodyMeasurementDateTime(bodyMeasurementDateTime);
        return this;
    }

    public void setBodyMeasurementDateTime(ZonedDateTime bodyMeasurementDateTime) {
        this.bodyMeasurementDateTime = bodyMeasurementDateTime;
    }

    public MeasurementType getMeasurementType() {
        return this.measurementType;
    }

    public void setMeasurementType(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    public BodyMeasurement measurementType(MeasurementType measurementType) {
        this.setMeasurementType(measurementType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BodyMeasurement)) {
            return false;
        }
        return id != null && id.equals(((BodyMeasurement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BodyMeasurement{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", bodyMeasurementDateTime='" + getBodyMeasurementDateTime() + "'" +
            "}";
    }
}
