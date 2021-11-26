package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Duration;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkoutRoutineExerciseSet.
 */
@Entity
@Table(name = "workout_routine_exercise_set")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workoutroutineexerciseset")
public class WorkoutRoutineExerciseSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "reps")
    private Integer reps;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "time")
    private Duration time;

    @ManyToOne
    @JsonIgnoreProperties(value = { "workoutRoutineExerciseSets", "workoutExercises", "workoutRoutines" }, allowSetters = true)
    private WorkoutRoutineExercise workoutRoutineExercise;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkoutRoutineExerciseSet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReps() {
        return this.reps;
    }

    public WorkoutRoutineExerciseSet reps(Integer reps) {
        this.setReps(reps);
        return this;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public WorkoutRoutineExerciseSet weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Duration getTime() {
        return this.time;
    }

    public WorkoutRoutineExerciseSet time(Duration time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Duration time) {
        this.time = time;
    }

    public WorkoutRoutineExercise getWorkoutRoutineExercise() {
        return this.workoutRoutineExercise;
    }

    public void setWorkoutRoutineExercise(WorkoutRoutineExercise workoutRoutineExercise) {
        this.workoutRoutineExercise = workoutRoutineExercise;
    }

    public WorkoutRoutineExerciseSet workoutRoutineExercise(WorkoutRoutineExercise workoutRoutineExercise) {
        this.setWorkoutRoutineExercise(workoutRoutineExercise);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutRoutineExerciseSet)) {
            return false;
        }
        return id != null && id.equals(((WorkoutRoutineExerciseSet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutRoutineExerciseSet{" +
            "id=" + getId() +
            ", reps=" + getReps() +
            ", weight=" + getWeight() +
            ", time='" + getTime() + "'" +
            "}";
    }
}
