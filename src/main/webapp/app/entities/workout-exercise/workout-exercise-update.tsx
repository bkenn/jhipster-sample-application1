import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IExercise } from 'app/shared/model/exercise.model';
import { getEntities as getExercises } from 'app/entities/exercise/exercise.reducer';
import { IWorkoutRoutineExercise } from 'app/shared/model/workout-routine-exercise.model';
import { getEntities as getWorkoutRoutineExercises } from 'app/entities/workout-routine-exercise/workout-routine-exercise.reducer';
import { IWorkout } from 'app/shared/model/workout.model';
import { getEntities as getWorkouts } from 'app/entities/workout/workout.reducer';
import { getEntity, updateEntity, createEntity, reset } from './workout-exercise.reducer';
import { IWorkoutExercise } from 'app/shared/model/workout-exercise.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutExerciseUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const exercises = useAppSelector(state => state.exercise.entities);
  const workoutRoutineExercises = useAppSelector(state => state.workoutRoutineExercise.entities);
  const workouts = useAppSelector(state => state.workout.entities);
  const workoutExerciseEntity = useAppSelector(state => state.workoutExercise.entity);
  const loading = useAppSelector(state => state.workoutExercise.loading);
  const updating = useAppSelector(state => state.workoutExercise.updating);
  const updateSuccess = useAppSelector(state => state.workoutExercise.updateSuccess);
  const handleClose = () => {
    props.history.push('/workout-exercise');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getExercises({}));
    dispatch(getWorkoutRoutineExercises({}));
    dispatch(getWorkouts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...workoutExerciseEntity,
      ...values,
      exercise: exercises.find(it => it.id.toString() === values.exercise.toString()),
      workoutRoutineExercise: workoutRoutineExercises.find(it => it.id.toString() === values.workoutRoutineExercise.toString()),
      workout: workouts.find(it => it.id.toString() === values.workout.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...workoutExerciseEntity,
          exercise: workoutExerciseEntity?.exercise?.id,
          workoutRoutineExercise: workoutExerciseEntity?.workoutRoutineExercise?.id,
          workout: workoutExerciseEntity?.workout?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplication1App.workoutExercise.home.createOrEditLabel" data-cy="WorkoutExerciseCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.home.createOrEditLabel">
              Create or edit a WorkoutExercise
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="workout-exercise-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutExercise.note')}
                id="workout-exercise-note"
                name="note"
                data-cy="note"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutExercise.timer')}
                id="workout-exercise-timer"
                name="timer"
                data-cy="timer"
                type="text"
              />
              <ValidatedField
                id="workout-exercise-exercise"
                name="exercise"
                data-cy="exercise"
                label={translate('jhipsterSampleApplication1App.workoutExercise.exercise')}
                type="select"
              >
                <option value="" key="0" />
                {exercises
                  ? exercises.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="workout-exercise-workoutRoutineExercise"
                name="workoutRoutineExercise"
                data-cy="workoutRoutineExercise"
                label={translate('jhipsterSampleApplication1App.workoutExercise.workoutRoutineExercise')}
                type="select"
              >
                <option value="" key="0" />
                {workoutRoutineExercises
                  ? workoutRoutineExercises.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="workout-exercise-workout"
                name="workout"
                data-cy="workout"
                label={translate('jhipsterSampleApplication1App.workoutExercise.workout')}
                type="select"
              >
                <option value="" key="0" />
                {workouts
                  ? workouts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/workout-exercise" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default WorkoutExerciseUpdate;
