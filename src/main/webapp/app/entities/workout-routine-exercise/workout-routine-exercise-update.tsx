import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './workout-routine-exercise.reducer';
import { IWorkoutRoutineExercise } from 'app/shared/model/workout-routine-exercise.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutRoutineExerciseUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workoutRoutineExerciseEntity = useAppSelector(state => state.workoutRoutineExercise.entity);
  const loading = useAppSelector(state => state.workoutRoutineExercise.loading);
  const updating = useAppSelector(state => state.workoutRoutineExercise.updating);
  const updateSuccess = useAppSelector(state => state.workoutRoutineExercise.updateSuccess);
  const handleClose = () => {
    props.history.push('/workout-routine-exercise');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...workoutRoutineExerciseEntity,
      ...values,
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
          ...workoutRoutineExerciseEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="jhipsterSampleApplication1App.workoutRoutineExercise.home.createOrEditLabel"
            data-cy="WorkoutRoutineExerciseCreateUpdateHeading"
          >
            <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExercise.home.createOrEditLabel">
              Create or edit a WorkoutRoutineExercise
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
                  id="workout-routine-exercise-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutRoutineExercise.note')}
                id="workout-routine-exercise-note"
                name="note"
                data-cy="note"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutRoutineExercise.timer')}
                id="workout-routine-exercise-timer"
                name="timer"
                data-cy="timer"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/workout-routine-exercise" replace color="info">
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

export default WorkoutRoutineExerciseUpdate;
