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
 * A WorkoutRoutineExercise.
 */
@Entity
@Table(name = "workout_routine_exercise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workoutroutineexercise")
public class WorkoutRoutineExercise implements Serializable {

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

    @OneToMany(mappedBy = "workoutRoutineExercise")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workoutRoutineExercise" }, allowSetters = true)
    private Set<WorkoutRoutineExerciseSet> workoutRoutineExerciseSets = new HashSet<>();

    @OneToMany(mappedBy = "workoutRoutineExercise")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workoutExerciseSets", "exercise", "workoutRoutineExercise", "workout" }, allowSetters = true)
    private Set<WorkoutExercise> workoutExercises = new HashSet<>();

    @OneToMany(mappedBy = "workoutRoutineExercise")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workoutRoutineExercise", "workouts", "workoutRoutineGroups" }, allowSetters = true)
    private Set<WorkoutRoutine> workoutRoutines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkoutRoutineExercise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return this.note;
    }

    public WorkoutRoutineExercise note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Duration getTimer() {
        return this.timer;
    }

    public WorkoutRoutineExercise timer(Duration timer) {
        this.setTimer(timer);
        return this;
    }

    public void setTimer(Duration timer) {
        this.timer = timer;
    }

    public Set<WorkoutRoutineExerciseSet> getWorkoutRoutineExerciseSets() {
        return this.workoutRoutineExerciseSets;
    }

    public void setWorkoutRoutineExerciseSets(Set<WorkoutRoutineExerciseSet> workoutRoutineExerciseSets) {
        if (this.workoutRoutineExerciseSets != null) {
            this.workoutRoutineExerciseSets.forEach(i -> i.setWorkoutRoutineExercise(null));
        }
        if (workoutRoutineExerciseSets != null) {
            workoutRoutineExerciseSets.forEach(i -> i.setWorkoutRoutineExercise(this));
        }
        this.workoutRoutineExerciseSets = workoutRoutineExerciseSets;
    }

    public WorkoutRoutineExercise workoutRoutineExerciseSets(Set<WorkoutRoutineExerciseSet> workoutRoutineExerciseSets) {
        this.setWorkoutRoutineExerciseSets(workoutRoutineExerciseSets);
        return this;
    }

    public WorkoutRoutineExercise addWorkoutRoutineExerciseSet(WorkoutRoutineExerciseSet workoutRoutineExerciseSet) {
        this.workoutRoutineExerciseSets.add(workoutRoutineExerciseSet);
        workoutRoutineExerciseSet.setWorkoutRoutineExercise(this);
        return this;
    }

    public WorkoutRoutineExercise removeWorkoutRoutineExerciseSet(WorkoutRoutineExerciseSet workoutRoutineExerciseSet) {
        this.workoutRoutineExerciseSets.remove(workoutRoutineExerciseSet);
        workoutRoutineExerciseSet.setWorkoutRoutineExercise(null);
        return this;
    }

    public Set<WorkoutExercise> getWorkoutExercises() {
        return this.workoutExercises;
    }

    public void setWorkoutExercises(Set<WorkoutExercise> workoutExercises) {
        if (this.workoutExercises != null) {
            this.workoutExercises.forEach(i -> i.setWorkoutRoutineExercise(null));
        }
        if (workoutExercises != null) {
            workoutExercises.forEach(i -> i.setWorkoutRoutineExercise(this));
        }
        this.workoutExercises = workoutExercises;
    }

    public WorkoutRoutineExercise workoutExercises(Set<WorkoutExercise> workoutExercises) {
        this.setWorkoutExercises(workoutExercises);
        return this;
    }

    public WorkoutRoutineExercise addWorkoutExercise(WorkoutExercise workoutExercise) {
        this.workoutExercises.add(workoutExercise);
        workoutExercise.setWorkoutRoutineExercise(this);
        return this;
    }

    public WorkoutRoutineExercise removeWorkoutExercise(WorkoutExercise workoutExercise) {
        this.workoutExercises.remove(workoutExercise);
        workoutExercise.setWorkoutRoutineExercise(null);
        return this;
    }

    public Set<WorkoutRoutine> getWorkoutRoutines() {
        return this.workoutRoutines;
    }

    public void setWorkoutRoutines(Set<WorkoutRoutine> workoutRoutines) {
        if (this.workoutRoutines != null) {
            this.workoutRoutines.forEach(i -> i.setWorkoutRoutineExercise(null));
        }
        if (workoutRoutines != null) {
            workoutRoutines.forEach(i -> i.setWorkoutRoutineExercise(this));
        }
        this.workoutRoutines = workoutRoutines;
    }

    public WorkoutRoutineExercise workoutRoutines(Set<WorkoutRoutine> workoutRoutines) {
        this.setWorkoutRoutines(workoutRoutines);
        return this;
    }

    public WorkoutRoutineExercise addWorkoutRoutine(WorkoutRoutine workoutRoutine) {
        this.workoutRoutines.add(workoutRoutine);
        workoutRoutine.setWorkoutRoutineExercise(this);
        return this;
    }

    public WorkoutRoutineExercise removeWorkoutRoutine(WorkoutRoutine workoutRoutine) {
        this.workoutRoutines.remove(workoutRoutine);
        workoutRoutine.setWorkoutRoutineExercise(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutRoutineExercise)) {
            return false;
        }
        return id != null && id.equals(((WorkoutRoutineExercise) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutRoutineExercise{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            ", timer='" + getTimer() + "'" +
            "}";
    }
}
