package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Weight.
 */
@Entity
@Table(name = "weight")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "weight")
public class Weight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private Double value;

    @Column(name = "weight_date_time")
    private ZonedDateTime weightDateTime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "bodyMeasurements", "weights" }, allowSetters = true)
    private MeasurementType measurementType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Weight id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return this.value;
    }

    public Weight value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public ZonedDateTime getWeightDateTime() {
        return this.weightDateTime;
    }

    public Weight weightDateTime(ZonedDateTime weightDateTime) {
        this.setWeightDateTime(weightDateTime);
        return this;
    }

    public void setWeightDateTime(ZonedDateTime weightDateTime) {
        this.weightDateTime = weightDateTime;
    }

    public MeasurementType getMeasurementType() {
        return this.measurementType;
    }

    public void setMeasurementType(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    public Weight measurementType(MeasurementType measurementType) {
        this.setMeasurementType(measurementType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Weight)) {
            return false;
        }
        return id != null && id.equals(((Weight) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Weight{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", weightDateTime='" + getWeightDateTime() + "'" +
            "}";
    }
}
