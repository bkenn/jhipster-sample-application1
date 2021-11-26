package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkoutRoutine.
 */
@Entity
@Table(name = "workout_routine")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workoutroutine")
public class WorkoutRoutine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "workoutRoutineExerciseSets", "workoutExercises", "workoutRoutines" }, allowSetters = true)
    private WorkoutRoutineExercise workoutRoutineExercise;

    @OneToMany(mappedBy = "workoutRoutine")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workoutExercises", "workoutRoutine", "user" }, allowSetters = true)
    private Set<Workout> workouts = new HashSet<>();

    @ManyToMany(mappedBy = "workoutRoutines")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workoutRoutines" }, allowSetters = true)
    private Set<WorkoutRoutineGroup> workoutRoutineGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkoutRoutine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public WorkoutRoutine title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public WorkoutRoutine description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WorkoutRoutineExercise getWorkoutRoutineExercise() {
        return this.workoutRoutineExercise;
    }

    public void setWorkoutRoutineExercise(WorkoutRoutineExercise workoutRoutineExercise) {
        this.workoutRoutineExercise = workoutRoutineExercise;
    }

    public WorkoutRoutine workoutRoutineExercise(WorkoutRoutineExercise workoutRoutineExercise) {
        this.setWorkoutRoutineExercise(workoutRoutineExercise);
        return this;
    }

    public Set<Workout> getWorkouts() {
        return this.workouts;
    }

    public void setWorkouts(Set<Workout> workouts) {
        if (this.workouts != null) {
            this.workouts.forEach(i -> i.setWorkoutRoutine(null));
        }
        if (workouts != null) {
            workouts.forEach(i -> i.setWorkoutRoutine(this));
        }
        this.workouts = workouts;
    }

    public WorkoutRoutine workouts(Set<Workout> workouts) {
        this.setWorkouts(workouts);
        return this;
    }

    public WorkoutRoutine addWorkout(Workout workout) {
        this.workouts.add(workout);
        workout.setWorkoutRoutine(this);
        return this;
    }

    public WorkoutRoutine removeWorkout(Workout workout) {
        this.workouts.remove(workout);
        workout.setWorkoutRoutine(null);
        return this;
    }

    public Set<WorkoutRoutineGroup> getWorkoutRoutineGroups() {
        return this.workoutRoutineGroups;
    }

    public void setWorkoutRoutineGroups(Set<WorkoutRoutineGroup> workoutRoutineGroups) {
        if (this.workoutRoutineGroups != null) {
            this.workoutRoutineGroups.forEach(i -> i.removeWorkoutRoutine(this));
        }
        if (workoutRoutineGroups != null) {
            workoutRoutineGroups.forEach(i -> i.addWorkoutRoutine(this));
        }
        this.workoutRoutineGroups = workoutRoutineGroups;
    }

    public WorkoutRoutine workoutRoutineGroups(Set<WorkoutRoutineGroup> workoutRoutineGroups) {
        this.setWorkoutRoutineGroups(workoutRoutineGroups);
        return this;
    }

    public WorkoutRoutine addWorkoutRoutineGroup(WorkoutRoutineGroup workoutRoutineGroup) {
        this.workoutRoutineGroups.add(workoutRoutineGroup);
        workoutRoutineGroup.getWorkoutRoutines().add(this);
        return this;
    }

    public WorkoutRoutine removeWorkoutRoutineGroup(WorkoutRoutineGroup workoutRoutineGroup) {
        this.workoutRoutineGroups.remove(workoutRoutineGroup);
        workoutRoutineGroup.getWorkoutRoutines().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutRoutine)) {
            return false;
        }
        return id != null && id.equals(((WorkoutRoutine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutRoutine{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
