package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExerciseImage.
 */
@Entity
@Table(name = "exercise_image")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "exerciseimage")
public class ExerciseImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "main")
    private Boolean main;

    @ManyToMany(mappedBy = "exerciseImages")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "repType", "exerciseCategory", "exerciseImages", "muscles", "workoutExercises" }, allowSetters = true)
    private Set<Exercise> exercises = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExerciseImage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public ExerciseImage uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public byte[] getImage() {
        return this.image;
    }

    public ExerciseImage image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public ExerciseImage imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Boolean getMain() {
        return this.main;
    }

    public ExerciseImage main(Boolean main) {
        this.setMain(main);
        return this;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    public Set<Exercise> getExercises() {
        return this.exercises;
    }

    public void setExercises(Set<Exercise> exercises) {
        if (this.exercises != null) {
            this.exercises.forEach(i -> i.removeExerciseImage(this));
        }
        if (exercises != null) {
            exercises.forEach(i -> i.addExerciseImage(this));
        }
        this.exercises = exercises;
    }

    public ExerciseImage exercises(Set<Exercise> exercises) {
        this.setExercises(exercises);
        return this;
    }

    public ExerciseImage addExercise(Exercise exercise) {
        this.exercises.add(exercise);
        exercise.getExerciseImages().add(this);
        return this;
    }

    public ExerciseImage removeExercise(Exercise exercise) {
        this.exercises.remove(exercise);
        exercise.getExerciseImages().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExerciseImage)) {
            return false;
        }
        return id != null && id.equals(((ExerciseImage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciseImage{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", main='" + getMain() + "'" +
            "}";
    }
}
