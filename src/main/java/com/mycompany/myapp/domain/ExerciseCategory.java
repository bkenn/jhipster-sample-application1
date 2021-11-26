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
 * A ExerciseCategory.
 */
@Entity
@Table(name = "exercise_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "exercisecategory")
public class ExerciseCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category_order")
    private Integer categoryOrder;

    @OneToMany(mappedBy = "exerciseCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "repType", "exerciseCategory", "exerciseImages", "muscles", "workoutExercises" }, allowSetters = true)
    private Set<Exercise> exercises = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExerciseCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ExerciseCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryOrder() {
        return this.categoryOrder;
    }

    public ExerciseCategory categoryOrder(Integer categoryOrder) {
        this.setCategoryOrder(categoryOrder);
        return this;
    }

    public void setCategoryOrder(Integer categoryOrder) {
        this.categoryOrder = categoryOrder;
    }

    public Set<Exercise> getExercises() {
        return this.exercises;
    }

    public void setExercises(Set<Exercise> exercises) {
        if (this.exercises != null) {
            this.exercises.forEach(i -> i.setExerciseCategory(null));
        }
        if (exercises != null) {
            exercises.forEach(i -> i.setExerciseCategory(this));
        }
        this.exercises = exercises;
    }

    public ExerciseCategory exercises(Set<Exercise> exercises) {
        this.setExercises(exercises);
        return this;
    }

    public ExerciseCategory addExercise(Exercise exercise) {
        this.exercises.add(exercise);
        exercise.setExerciseCategory(this);
        return this;
    }

    public ExerciseCategory removeExercise(Exercise exercise) {
        this.exercises.remove(exercise);
        exercise.setExerciseCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExerciseCategory)) {
            return false;
        }
        return id != null && id.equals(((ExerciseCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciseCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", categoryOrder=" + getCategoryOrder() +
            "}";
    }
}
