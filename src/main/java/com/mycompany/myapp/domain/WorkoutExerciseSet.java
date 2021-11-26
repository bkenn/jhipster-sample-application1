package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Duration;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkoutExerciseSet.
 */
@Entity
@Table(name = "workout_exercise_set")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workoutexerciseset")
public class WorkoutExerciseSet implements Serializable {

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

    @Column(name = "complete")
    private Boolean complete;

    @Column(name = "complete_time")
    private Duration completeTime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "workoutExerciseSets", "exercise", "workoutRoutineExercise", "workout" }, allowSetters = true)
    private WorkoutExercise workoutExercise;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkoutExerciseSet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReps() {
        return this.reps;
    }

    public WorkoutExerciseSet reps(Integer reps) {
        this.setReps(reps);
        return this;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public WorkoutExerciseSet weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Duration getTime() {
        return this.time;
    }

    public WorkoutExerciseSet time(Duration time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Duration time) {
        this.time = time;
    }

    public Boolean getComplete() {
        return this.complete;
    }

    public WorkoutExerciseSet complete(Boolean complete) {
        this.setComplete(complete);
        return this;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public Duration getCompleteTime() {
        return this.completeTime;
    }

    public WorkoutExerciseSet completeTime(Duration completeTime) {
        this.setCompleteTime(completeTime);
        return this;
    }

    public void setCompleteTime(Duration completeTime) {
        this.completeTime = completeTime;
    }

    public WorkoutExercise getWorkoutExercise() {
        return this.workoutExercise;
    }

    public void setWorkoutExercise(WorkoutExercise workoutExercise) {
        this.workoutExercise = workoutExercise;
    }

    public WorkoutExerciseSet workoutExercise(WorkoutExercise workoutExercise) {
        this.setWorkoutExercise(workoutExercise);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutExerciseSet)) {
            return false;
        }
        return id != null && id.equals(((WorkoutExerciseSet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutExerciseSet{" +
            "id=" + getId() +
            ", reps=" + getReps() +
            ", weight=" + getWeight() +
            ", time='" + getTime() + "'" +
            ", complete='" + getComplete() + "'" +
            ", completeTime='" + getCompleteTime() + "'" +
            "}";
    }
}
