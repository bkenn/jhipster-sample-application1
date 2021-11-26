package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MeasurementType.
 */
@Entity
@Table(name = "measurement_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "measurementtype")
public class MeasurementType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "measurement_order")
    private Integer measurementOrder;

    @Column(name = "measurement_unit")
    private String measurementUnit;

    @OneToMany(mappedBy = "measurementType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "measurementType" }, allowSetters = true)
    private Set<BodyMeasurement> bodyMeasurements = new HashSet<>();

    @OneToMany(mappedBy = "measurementType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "measurementType" }, allowSetters = true)
    private Set<Weight> weights = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MeasurementType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MeasurementType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public MeasurementType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMeasurementOrder() {
        return this.measurementOrder;
    }

    public MeasurementType measurementOrder(Integer measurementOrder) {
        this.setMeasurementOrder(measurementOrder);
        return this;
    }

    public void setMeasurementOrder(Integer measurementOrder) {
        this.measurementOrder = measurementOrder;
    }

    public String getMeasurementUnit() {
        return this.measurementUnit;
    }

    public MeasurementType measurementUnit(String measurementUnit) {
        this.setMeasurementUnit(measurementUnit);
        return this;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public Set<BodyMeasurement> getBodyMeasurements() {
        return this.bodyMeasurements;
    }

    public void setBodyMeasurements(Set<BodyMeasurement> bodyMeasurements) {
        if (this.bodyMeasurements != null) {
            this.bodyMeasurements.forEach(i -> i.setMeasurementType(null));
        }
        if (bodyMeasurements != null) {
            bodyMeasurements.forEach(i -> i.setMeasurementType(this));
        }
        this.bodyMeasurements = bodyMeasurements;
    }

    public MeasurementType bodyMeasurements(Set<BodyMeasurement> bodyMeasurements) {
        this.setBodyMeasurements(bodyMeasurements);
        return this;
    }

    public MeasurementType addBodyMeasurement(BodyMeasurement bodyMeasurement) {
        this.bodyMeasurements.add(bodyMeasurement);
        bodyMeasurement.setMeasurementType(this);
        return this;
    }

    public MeasurementType removeBodyMeasurement(BodyMeasurement bodyMeasurement) {
        this.bodyMeasurements.remove(bodyMeasurement);
        bodyMeasurement.setMeasurementType(null);
        return this;
    }

    public Set<Weight> getWeights() {
        return this.weights;
    }

    public void setWeights(Set<Weight> weights) {
        if (this.weights != null) {
            this.weights.forEach(i -> i.setMeasurementType(null));
        }
        if (weights != null) {
            weights.forEach(i -> i.setMeasurementType(this));
        }
        this.weights = weights;
    }

    public MeasurementType weights(Set<Weight> weights) {
        this.setWeights(weights);
        return this;
    }

    public MeasurementType addWeight(Weight weight) {
        this.weights.add(weight);
        weight.setMeasurementType(this);
        return this;
    }

    public MeasurementType removeWeight(Weight weight) {
        this.weights.remove(weight);
        weight.setMeasurementType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MeasurementType)) {
            return false;
        }
        return id != null && id.equals(((MeasurementType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MeasurementType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", measurementOrder=" + getMeasurementOrder() +
            ", measurementUnit='" + getMeasurementUnit() + "'" +
            "}";
    }
}
