import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorkoutRoutine } from 'app/shared/model/workout-routine.model';
import { getEntities as getWorkoutRoutines } from 'app/entities/workout-routine/workout-routine.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/shared/reducers/user-management';
import { getEntity, updateEntity, createEntity, reset } from './workout.reducer';
import { IWorkout } from 'app/shared/model/workout.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workoutRoutines = useAppSelector(state => state.workoutRoutine.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const workoutEntity = useAppSelector(state => state.workout.entity);
  const loading = useAppSelector(state => state.workout.loading);
  const updating = useAppSelector(state => state.workout.updating);
  const updateSuccess = useAppSelector(state => state.workout.updateSuccess);
  const handleClose = () => {
    props.history.push('/workout');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getWorkoutRoutines({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.workoutStartDateTime = convertDateTimeToServer(values.workoutStartDateTime);
    values.workoutEndDateTime = convertDateTimeToServer(values.workoutEndDateTime);

    const entity = {
      ...workoutEntity,
      ...values,
      workoutRoutine: workoutRoutines.find(it => it.id.toString() === values.workoutRoutine.toString()),
      user: users.find(it => it.id.toString() === values.user.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          workoutStartDateTime: displayDefaultDateTime(),
          workoutEndDateTime: displayDefaultDateTime(),
        }
      : {
          ...workoutEntity,
          workoutStartDateTime: convertDateTimeFromServer(workoutEntity.workoutStartDateTime),
          workoutEndDateTime: convertDateTimeFromServer(workoutEntity.workoutEndDateTime),
          workoutRoutine: workoutEntity?.workoutRoutine?.id,
          user: workoutEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplication1App.workout.home.createOrEditLabel" data-cy="WorkoutCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplication1App.workout.home.createOrEditLabel">Create or edit a Workout</Translate>
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
                  id="workout-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workout.title')}
                id="workout-title"
                name="title"
                data-cy="title"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workout.description')}
                id="workout-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workout.workoutStartDateTime')}
                id="workout-workoutStartDateTime"
                name="workoutStartDateTime"
                data-cy="workoutStartDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workout.workoutEndDateTime')}
                id="workout-workoutEndDateTime"
                name="workoutEndDateTime"
                data-cy="workoutEndDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="workout-workoutRoutine"
                name="workoutRoutine"
                data-cy="workoutRoutine"
                label={translate('jhipsterSampleApplication1App.workout.workoutRoutine')}
                type="select"
              >
                <option value="" key="0" />
                {workoutRoutines
                  ? workoutRoutines.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="workout-user"
                name="user"
                data-cy="user"
                label={translate('jhipsterSampleApplication1App.workout.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/workout" replace color="info">
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

export default WorkoutUpdate;
