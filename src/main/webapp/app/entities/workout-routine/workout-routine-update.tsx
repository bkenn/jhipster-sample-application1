import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorkoutRoutineExercise } from 'app/shared/model/workout-routine-exercise.model';
import { getEntities as getWorkoutRoutineExercises } from 'app/entities/workout-routine-exercise/workout-routine-exercise.reducer';
import { IWorkoutRoutineGroup } from 'app/shared/model/workout-routine-group.model';
import { getEntities as getWorkoutRoutineGroups } from 'app/entities/workout-routine-group/workout-routine-group.reducer';
import { getEntity, updateEntity, createEntity, reset } from './workout-routine.reducer';
import { IWorkoutRoutine } from 'app/shared/model/workout-routine.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutRoutineUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workoutRoutineExercises = useAppSelector(state => state.workoutRoutineExercise.entities);
  const workoutRoutineGroups = useAppSelector(state => state.workoutRoutineGroup.entities);
  const workoutRoutineEntity = useAppSelector(state => state.workoutRoutine.entity);
  const loading = useAppSelector(state => state.workoutRoutine.loading);
  const updating = useAppSelector(state => state.workoutRoutine.updating);
  const updateSuccess = useAppSelector(state => state.workoutRoutine.updateSuccess);
  const handleClose = () => {
    props.history.push('/workout-routine');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getWorkoutRoutineExercises({}));
    dispatch(getWorkoutRoutineGroups({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...workoutRoutineEntity,
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
          ...workoutRoutineEntity,
          workoutRoutineExercise: workoutRoutineEntity?.workoutRoutineExercise?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplication1App.workoutRoutine.home.createOrEditLabel" data-cy="WorkoutRoutineCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplication1App.workoutRoutine.home.createOrEditLabel">
              Create or edit a WorkoutRoutine
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
                  id="workout-routine-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutRoutine.title')}
                id="workout-routine-title"
                name="title"
                data-cy="title"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutRoutine.description')}
                id="workout-routine-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="workout-routine-workoutRoutineExercise"
                name="workoutRoutineExercise"
                data-cy="workoutRoutineExercise"
                label={translate('jhipsterSampleApplication1App.workoutRoutine.workoutRoutineExercise')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/workout-routine" replace color="info">
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

export default WorkoutRoutineUpdate;
