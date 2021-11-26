import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorkoutExercise } from 'app/shared/model/workout-exercise.model';
import { getEntities as getWorkoutExercises } from 'app/entities/workout-exercise/workout-exercise.reducer';
import { getEntity, updateEntity, createEntity, reset } from './workout-exercise-set.reducer';
import { IWorkoutExerciseSet } from 'app/shared/model/workout-exercise-set.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutExerciseSetUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workoutExercises = useAppSelector(state => state.workoutExercise.entities);
  const workoutExerciseSetEntity = useAppSelector(state => state.workoutExerciseSet.entity);
  const loading = useAppSelector(state => state.workoutExerciseSet.loading);
  const updating = useAppSelector(state => state.workoutExerciseSet.updating);
  const updateSuccess = useAppSelector(state => state.workoutExerciseSet.updateSuccess);
  const handleClose = () => {
    props.history.push('/workout-exercise-set');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getWorkoutExercises({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...workoutExerciseSetEntity,
      ...values,
      workoutExercise: workoutExercises.find(it => it.id.toString() === values.workoutExercise.toString()),
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
          ...workoutExerciseSetEntity,
          workoutExercise: workoutExerciseSetEntity?.workoutExercise?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplication1App.workoutExerciseSet.home.createOrEditLabel" data-cy="WorkoutExerciseSetCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.home.createOrEditLabel">
              Create or edit a WorkoutExerciseSet
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
                  id="workout-exercise-set-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutExerciseSet.reps')}
                id="workout-exercise-set-reps"
                name="reps"
                data-cy="reps"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutExerciseSet.weight')}
                id="workout-exercise-set-weight"
                name="weight"
                data-cy="weight"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutExerciseSet.time')}
                id="workout-exercise-set-time"
                name="time"
                data-cy="time"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutExerciseSet.complete')}
                id="workout-exercise-set-complete"
                name="complete"
                data-cy="complete"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutExerciseSet.completeTime')}
                id="workout-exercise-set-completeTime"
                name="completeTime"
                data-cy="completeTime"
                type="text"
              />
              <ValidatedField
                id="workout-exercise-set-workoutExercise"
                name="workoutExercise"
                data-cy="workoutExercise"
                label={translate('jhipsterSampleApplication1App.workoutExerciseSet.workoutExercise')}
                type="select"
              >
                <option value="" key="0" />
                {workoutExercises
                  ? workoutExercises.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/workout-exercise-set" replace color="info">
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

export default WorkoutExerciseSetUpdate;
