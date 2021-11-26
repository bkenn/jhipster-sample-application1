package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkoutExercise.
 */
@Entity
@Table(name = "workout_exercise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workoutexercise")
public class WorkoutExercise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "note")
    private String note;

    @Column(name = "timer")
    private Duration timer;

    @OneToMany(mappedBy = "workoutExercise")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workoutExercise" }, allowSetters = true)
    private Set<WorkoutExerciseSet> workoutExerciseSets = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "repType", "exerciseCategory", "exerciseImages", "muscles", "workoutExercises" }, allowSetters = true)
    private Exercise exercise;

    @ManyToOne
    @JsonIgnoreProperties(value = { "workoutRoutineExerciseSets", "workoutExercises", "workoutRoutines" }, allowSetters = true)
    private WorkoutRoutineExercise workoutRoutineExercise;

    @ManyToOne
    @JsonIgnoreProperties(value = { "workoutExercises", "workoutRoutine", "user" }, allowSetters = true)
    private Workout workout;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkoutExercise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return this.note;
    }

    public WorkoutExercise note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Duration getTimer() {
        return this.timer;
    }

    public WorkoutExercise timer(Duration timer) {
        this.setTimer(timer);
        return this;
    }

    public void setTimer(Duration timer) {
        this.timer = timer;
    }

    public Set<WorkoutExerciseSet> getWorkoutExerciseSets() {
        return this.workoutExerciseSets;
    }

    public void setWorkoutExerciseSets(Set<WorkoutExerciseSet> workoutExerciseSets) {
        if (this.workoutExerciseSets != null) {
            this.workoutExerciseSets.forEach(i -> i.setWorkoutExercise(null));
        }
        if (workoutExerciseSets != null) {
            workoutExerciseSets.forEach(i -> i.setWorkoutExercise(this));
        }
        this.workoutExerciseSets = workoutExerciseSets;
    }

    public WorkoutExercise workoutExerciseSets(Set<WorkoutExerciseSet> workoutExerciseSets) {
        this.setWorkoutExerciseSets(workoutExerciseSets);
        return this;
    }

    public WorkoutExercise addWorkoutExerciseSet(WorkoutExerciseSet workoutExerciseSet) {
        this.workoutExerciseSets.add(workoutExerciseSet);
        workoutExerciseSet.setWorkoutExercise(this);
        return this;
    }

    public WorkoutExercise removeWorkoutExerciseSet(WorkoutExerciseSet workoutExerciseSet) {
        this.workoutExerciseSets.remove(workoutExerciseSet);
        workoutExerciseSet.setWorkoutExercise(null);
        return this;
    }

    public Exercise getExercise() {
        return this.exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public WorkoutExercise exercise(Exercise exercise) {
        this.setExercise(exercise);
        return this;
    }

    public WorkoutRoutineExercise getWorkoutRoutineExercise() {
        return this.workoutRoutineExercise;
    }

    public void setWorkoutRoutineExercise(WorkoutRoutineExercise workoutRoutineExercise) {
        this.workoutRoutineExercise = workoutRoutineExercise;
    }

    public WorkoutExercise workoutRoutineExercise(WorkoutRoutineExercise workoutRoutineExercise) {
        this.setWorkoutRoutineExercise(workoutRoutineExercise);
        return this;
    }

    public Workout getWorkout() {
        return this.workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public WorkoutExercise workout(Workout workout) {
        this.setWorkout(workout);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutExercise)) {
            return false;
        }
        return id != null && id.equals(((WorkoutExercise) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutExercise{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            ", timer='" + getTimer() + "'" +
            "}";
    }
}
