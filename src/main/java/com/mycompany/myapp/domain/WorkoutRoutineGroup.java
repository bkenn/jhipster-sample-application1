package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkoutRoutineGroup.
 */
@Entity
@Table(name = "workout_routine_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workoutroutinegroup")
public class WorkoutRoutineGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
        name = "rel_workout_routine_group__workout_routine",
        joinColumns = @JoinColumn(name = "workout_routine_group_id"),
        inverseJoinColumns = @JoinColumn(name = "workout_routine_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workoutRoutineExercise", "workouts", "workoutRoutineGroups" }, allowSetters = true)
    private Set<WorkoutRoutine> workoutRoutines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkoutRoutineGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public WorkoutRoutineGroup name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<WorkoutRoutine> getWorkoutRoutines() {
        return this.workoutRoutines;
    }

    public void setWorkoutRoutines(Set<WorkoutRoutine> workoutRoutines) {
        this.workoutRoutines = workoutRoutines;
    }

    public WorkoutRoutineGroup workoutRoutines(Set<WorkoutRoutine> workoutRoutines) {
        this.setWorkoutRoutines(workoutRoutines);
        return this;
    }

    public WorkoutRoutineGroup addWorkoutRoutine(WorkoutRoutine workoutRoutine) {
        this.workoutRoutines.add(workoutRoutine);
        workoutRoutine.getWorkoutRoutineGroups().add(this);
        return this;
    }

    public WorkoutRoutineGroup removeWorkoutRoutine(WorkoutRoutine workoutRoutine) {
        this.workoutRoutines.remove(workoutRoutine);
        workoutRoutine.getWorkoutRoutineGroups().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutRoutineGroup)) {
            return false;
        }
        return id != null && id.equals(((WorkoutRoutineGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutRoutineGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
