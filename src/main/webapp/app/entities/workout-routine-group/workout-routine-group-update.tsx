import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorkoutRoutine } from 'app/shared/model/workout-routine.model';
import { getEntities as getWorkoutRoutines } from 'app/entities/workout-routine/workout-routine.reducer';
import { getEntity, updateEntity, createEntity, reset } from './workout-routine-group.reducer';
import { IWorkoutRoutineGroup } from 'app/shared/model/workout-routine-group.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutRoutineGroupUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workoutRoutines = useAppSelector(state => state.workoutRoutine.entities);
  const workoutRoutineGroupEntity = useAppSelector(state => state.workoutRoutineGroup.entity);
  const loading = useAppSelector(state => state.workoutRoutineGroup.loading);
  const updating = useAppSelector(state => state.workoutRoutineGroup.updating);
  const updateSuccess = useAppSelector(state => state.workoutRoutineGroup.updateSuccess);
  const handleClose = () => {
    props.history.push('/workout-routine-group');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getWorkoutRoutines({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...workoutRoutineGroupEntity,
      ...values,
      workoutRoutines: mapIdList(values.workoutRoutines),
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
          ...workoutRoutineGroupEntity,
          workoutRoutines: workoutRoutineGroupEntity?.workoutRoutines?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="jhipsterSampleApplication1App.workoutRoutineGroup.home.createOrEditLabel"
            data-cy="WorkoutRoutineGroupCreateUpdateHeading"
          >
            <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineGroup.home.createOrEditLabel">
              Create or edit a WorkoutRoutineGroup
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
                  id="workout-routine-group-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutRoutineGroup.name')}
                id="workout-routine-group-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.workoutRoutineGroup.workoutRoutine')}
                id="workout-routine-group-workoutRoutine"
                data-cy="workoutRoutine"
                type="select"
                multiple
                name="workoutRoutines"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/workout-routine-group" replace color="info">
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

export default WorkoutRoutineGroupUpdate;
