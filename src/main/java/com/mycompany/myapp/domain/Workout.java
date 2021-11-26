package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Workout.
 */
@Entity
@Table(name = "workout")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workout")
public class Workout implements Serializable {

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

    @Column(name = "workout_start_date_time")
    private ZonedDateTime workoutStartDateTime;

    @Column(name = "workout_end_date_time")
    private ZonedDateTime workoutEndDateTime;

    @OneToMany(mappedBy = "workout")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workoutExerciseSets", "exercise", "workoutRoutineExercise", "workout" }, allowSetters = true)
    private Set<WorkoutExercise> workoutExercises = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "workoutRoutineExercise", "workouts", "workoutRoutineGroups" }, allowSetters = true)
    private WorkoutRoutine workoutRoutine;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Workout id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Workout title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Workout description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getWorkoutStartDateTime() {
        return this.workoutStartDateTime;
    }

    public Workout workoutStartDateTime(ZonedDateTime workoutStartDateTime) {
        this.setWorkoutStartDateTime(workoutStartDateTime);
        return this;
    }

    public void setWorkoutStartDateTime(ZonedDateTime workoutStartDateTime) {
        this.workoutStartDateTime = workoutStartDateTime;
    }

    public ZonedDateTime getWorkoutEndDateTime() {
        return this.workoutEndDateTime;
    }

    public Workout workoutEndDateTime(ZonedDateTime workoutEndDateTime) {
        this.setWorkoutEndDateTime(workoutEndDateTime);
        return this;
    }

    public void setWorkoutEndDateTime(ZonedDateTime workoutEndDateTime) {
        this.workoutEndDateTime = workoutEndDateTime;
    }

    public Set<WorkoutExercise> getWorkoutExercises() {
        return this.workoutExercises;
    }

    public void setWorkoutExercises(Set<WorkoutExercise> workoutExercises) {
        if (this.workoutExercises != null) {
            this.workoutExercises.forEach(i -> i.setWorkout(null));
        }
        if (workoutExercises != null) {
            workoutExercises.forEach(i -> i.setWorkout(this));
        }
        this.workoutExercises = workoutExercises;
    }

    public Workout workoutExercises(Set<WorkoutExercise> workoutExercises) {
        this.setWorkoutExercises(workoutExercises);
        return this;
    }

    public Workout addWorkoutExercise(WorkoutExercise workoutExercise) {
        this.workoutExercises.add(workoutExercise);
        workoutExercise.setWorkout(this);
        return this;
    }

    public Workout removeWorkoutExercise(WorkoutExercise workoutExercise) {
        this.workoutExercises.remove(workoutExercise);
        workoutExercise.setWorkout(null);
        return this;
    }

    public WorkoutRoutine getWorkoutRoutine() {
        return this.workoutRoutine;
    }

    public void setWorkoutRoutine(WorkoutRoutine workoutRoutine) {
        this.workoutRoutine = workoutRoutine;
    }

    public Workout workoutRoutine(WorkoutRoutine workoutRoutine) {
        this.setWorkoutRoutine(workoutRoutine);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Workout user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Workout)) {
            return false;
        }
        return id != null && id.equals(((Workout) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Workout{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", workoutStartDateTime='" + getWorkoutStartDateTime() + "'" +
            ", workoutEndDateTime='" + getWorkoutEndDateTime() + "'" +
            "}";
    }
}
