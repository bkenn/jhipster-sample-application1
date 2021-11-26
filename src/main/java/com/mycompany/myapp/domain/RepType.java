package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RepType.
 */
@Entity
@Table(name = "rep_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "reptype")
public class RepType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "display")
    private String display;

    @OneToMany(mappedBy = "repType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "repType", "exerciseCategory", "exerciseImages", "muscles", "workoutExercises" }, allowSetters = true)
    private Set<Exercise> exercises = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RepType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public RepType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay() {
        return this.display;
    }

    public RepType display(String display) {
        this.setDisplay(display);
        return this;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Set<Exercise> getExercises() {
        return this.exercises;
    }

    public void setExercises(Set<Exercise> exercises) {
        if (this.exercises != null) {
            this.exercises.forEach(i -> i.setRepType(null));
        }
        if (exercises != null) {
            exercises.forEach(i -> i.setRepType(this));
        }
        this.exercises = exercises;
    }

    public RepType exercises(Set<Exercise> exercises) {
        this.setExercises(exercises);
        return this;
    }

    public RepType addExercise(Exercise exercise) {
        this.exercises.add(exercise);
        exercise.setRepType(this);
        return this;
    }

    public RepType removeExercise(Exercise exercise) {
        this.exercises.remove(exercise);
        exercise.setRepType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepType)) {
            return false;
        }
        return id != null && id.equals(((RepType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RepType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", display='" + getDisplay() + "'" +
            "}";
    }
}
