package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Muscle.
 */
@Entity
@Table(name = "muscle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "muscle")
public class Muscle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "muscle_order")
    private Integer muscleOrder;

    @Column(name = "image_url_main")
    private String imageUrlMain;

    @Column(name = "image_url_secondary")
    private String imageUrlSecondary;

    @Column(name = "front")
    private Boolean front;

    @ManyToMany(mappedBy = "muscles")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "repType", "exerciseCategory", "exerciseImages", "muscles", "workoutExercises" }, allowSetters = true)
    private Set<Exercise> exercises = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Muscle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Muscle name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Muscle description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMuscleOrder() {
        return this.muscleOrder;
    }

    public Muscle muscleOrder(Integer muscleOrder) {
        this.setMuscleOrder(muscleOrder);
        return this;
    }

    public void setMuscleOrder(Integer muscleOrder) {
        this.muscleOrder = muscleOrder;
    }

    public String getImageUrlMain() {
        return this.imageUrlMain;
    }

    public Muscle imageUrlMain(String imageUrlMain) {
        this.setImageUrlMain(imageUrlMain);
        return this;
    }

    public void setImageUrlMain(String imageUrlMain) {
        this.imageUrlMain = imageUrlMain;
    }

    public String getImageUrlSecondary() {
        return this.imageUrlSecondary;
    }

    public Muscle imageUrlSecondary(String imageUrlSecondary) {
        this.setImageUrlSecondary(imageUrlSecondary);
        return this;
    }

    public void setImageUrlSecondary(String imageUrlSecondary) {
        this.imageUrlSecondary = imageUrlSecondary;
    }

    public Boolean getFront() {
        return this.front;
    }

    public Muscle front(Boolean front) {
        this.setFront(front);
        return this;
    }

    public void setFront(Boolean front) {
        this.front = front;
    }

    public Set<Exercise> getExercises() {
        return this.exercises;
    }

    public void setExercises(Set<Exercise> exercises) {
        if (this.exercises != null) {
            this.exercises.forEach(i -> i.removeMuscle(this));
        }
        if (exercises != null) {
            exercises.forEach(i -> i.addMuscle(this));
        }
        this.exercises = exercises;
    }

    public Muscle exercises(Set<Exercise> exercises) {
        this.setExercises(exercises);
        return this;
    }

    public Muscle addExercise(Exercise exercise) {
        this.exercises.add(exercise);
        exercise.getMuscles().add(this);
        return this;
    }

    public Muscle removeExercise(Exercise exercise) {
        this.exercises.remove(exercise);
        exercise.getMuscles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Muscle)) {
            return false;
        }
        return id != null && id.equals(((Muscle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Muscle{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", muscleOrder=" + getMuscleOrder() +
            ", imageUrlMain='" + getImageUrlMain() + "'" +
            ", imageUrlSecondary='" + getImageUrlSecondary() + "'" +
            ", front='" + getFront() + "'" +
            "}";
    }
}
