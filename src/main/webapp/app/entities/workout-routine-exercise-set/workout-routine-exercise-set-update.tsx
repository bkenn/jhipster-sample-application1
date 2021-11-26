import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorkoutRoutineExercise } from 'app/shared/model/workout-routine-exercise.model';
import { getEntities as getWorkoutRoutineExercises } from 'app/entities/workout-routine-exercise/workout-routine-exercise.reducer';
import { getEntity, updateEntity, createEntity, reset } from './workout-routine-exercise-set.reducer';
import { IWorkoutRoutineExerciseSet } from 'app/shared/model/workout-routine-exercise-set.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutRoutineExerciseSetUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workoutRoutineExercises = useAppSelector(state => state.workoutRoutineExercise.entities);
  const workoutRoutineExerciseSetEntity = useAppSelector(state => state.workoutRoutineExerciseSet.entity);
  const loading = useAppSelector(state => state.workoutRoutineExerciseSet.loading);
  const updating = useAppSelector(state => state.workoutRoutineExerciseSet.updating);
  const updateSuccess = useAppSelector(state => state.workoutRoutineExerciseSet.updateSuccess);
  const handleClose = () => {
    props.history.push('/workout-routine-exercise-set');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getWorkoutRoutineExercises({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...workoutRoutineExerciseSetEntity,
      ...values,
      workoutRoutineExercise: workoutRoutineExercises.find(it => it.id.toString() === values.workoutRoutineExercise.toString()),
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
          ...workoutRoutineExerciseSetEntity,
          workoutRoutineExercise: workoutRoutineExerciseSetEntity?.workoutRoutineExercise?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="jhipsterSampleApplication1App.workoutRoutineExerciseSet.home.createOrEditLabel"
            data-cy="WorkoutRoutineExerciseSetCreateUpdateHeading"
          >
            <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.home.createOrEditLabel">
              Create or edit a WorkoutRoutineExerciseSet
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
                  id="workout-routine-exercise-set-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutRoutineExerciseSet.reps')}
                id="workout-routine-exercise-set-reps"
                name="reps"
                data-cy="reps"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutRoutineExerciseSet.weight')}
                id="workout-routine-exercise-set-weight"
                name="weight"
                data-cy="weight"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutRoutineExerciseSet.time')}
                id="workout-routine-exercise-set-time"
                name="time"
                data-cy="time"
                type="text"
              />
              <ValidatedField
                id="workout-routine-exercise-set-workoutRoutineExercise"
                name="workoutRoutineExercise"
                data-cy="workoutRoutineExercise"
                label={translate('jhipsterSampleApplication1App.workoutRoutineExerciseSet.workoutRoutineExercise')}
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
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/workout-routine-exercise-set"
                replace
                color="info"
              >
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

export default WorkoutRoutineExerciseSetUpdate;
