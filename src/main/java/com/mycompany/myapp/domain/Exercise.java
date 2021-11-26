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
 * A Exercise.
 */
@Entity
@Table(name = "exercise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "exercise")
public class Exercise implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "exercises" }, allowSetters = true)
    private RepType repType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "exercises" }, allowSetters = true)
    private ExerciseCategory exerciseCategory;

    @ManyToMany
    @JoinTable(
        name = "rel_exercise__exercise_image",
        joinColumns = @JoinColumn(name = "exercise_id"),
        inverseJoinColumns = @JoinColumn(name = "exercise_image_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "exercises" }, allowSetters = true)
    private Set<ExerciseImage> exerciseImages = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_exercise__muscle",
        joinColumns = @JoinColumn(name = "exercise_id"),
        inverseJoinColumns = @JoinColumn(name = "muscle_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "exercises" }, allowSetters = true)
    private Set<Muscle> muscles = new HashSet<>();

    @OneToMany(mappedBy = "exercise")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workoutExerciseSets", "exercise", "workoutRoutineExercise", "workout" }, allowSetters = true)
    private Set<WorkoutExercise> workoutExercises = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Exercise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Exercise name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Exercise description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RepType getRepType() {
        return this.repType;
    }

    public void setRepType(RepType repType) {
        this.repType = repType;
    }

    public Exercise repType(RepType repType) {
        this.setRepType(repType);
        return this;
    }

    public ExerciseCategory getExerciseCategory() {
        return this.exerciseCategory;
    }

    public void setExerciseCategory(ExerciseCategory exerciseCategory) {
        this.exerciseCategory = exerciseCategory;
    }

    public Exercise exerciseCategory(ExerciseCategory exerciseCategory) {
        this.setExerciseCategory(exerciseCategory);
        return this;
    }

    public Set<ExerciseImage> getExerciseImages() {
        return this.exerciseImages;
    }

    public void setExerciseImages(Set<ExerciseImage> exerciseImages) {
        this.exerciseImages = exerciseImages;
    }

    public Exercise exerciseImages(Set<ExerciseImage> exerciseImages) {
        this.setExerciseImages(exerciseImages);
        return this;
    }

    public Exercise addExerciseImage(ExerciseImage exerciseImage) {
        this.exerciseImages.add(exerciseImage);
        exerciseImage.getExercises().add(this);
        return this;
    }

    public Exercise removeExerciseImage(ExerciseImage exerciseImage) {
        this.exerciseImages.remove(exerciseImage);
        exerciseImage.getExercises().remove(this);
        return this;
    }

    public Set<Muscle> getMuscles() {
        return this.muscles;
    }

    public void setMuscles(Set<Muscle> muscles) {
        this.muscles = muscles;
    }

    public Exercise muscles(Set<Muscle> muscles) {
        this.setMuscles(muscles);
        return this;
    }

    public Exercise addMuscle(Muscle muscle) {
        this.muscles.add(muscle);
        muscle.getExercises().add(this);
        return this;
    }

    public Exercise removeMuscle(Muscle muscle) {
        this.muscles.remove(muscle);
        muscle.getExercises().remove(this);
        return this;
    }

    public Set<WorkoutExercise> getWorkoutExercises() {
        return this.workoutExercises;
    }

    public void setWorkoutExercises(Set<WorkoutExercise> workoutExercises) {
        if (this.workoutExercises != null) {
            this.workoutExercises.forEach(i -> i.setExercise(null));
        }
        if (workoutExercises != null) {
            workoutExercises.forEach(i -> i.setExercise(this));
        }
        this.workoutExercises = workoutExercises;
    }

    public Exercise workoutExercises(Set<WorkoutExercise> workoutExercises) {
        this.setWorkoutExercises(workoutExercises);
        return this;
    }

    public Exercise addWorkoutExercise(WorkoutExercise workoutExercise) {
        this.workoutExercises.add(workoutExercise);
        workoutExercise.setExercise(this);
        return this;
    }

    public Exercise removeWorkoutExercise(WorkoutExercise workoutExercise) {
        this.workoutExercises.remove(workoutExercise);
        workoutExercise.setExercise(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exercise)) {
            return false;
        }
        return id != null && id.equals(((Exercise) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exercise{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
