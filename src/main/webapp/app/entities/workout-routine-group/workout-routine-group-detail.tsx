import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './workout-routine-group.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutRoutineGroupDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workoutRoutineGroupEntity = useAppSelector(state => state.workoutRoutineGroup.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workoutRoutineGroupDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineGroup.detail.title">WorkoutRoutineGroup</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{workoutRoutineGroupEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineGroup.name">Name</Translate>
            </span>
          </dt>
          <dd>{workoutRoutineGroupEntity.name}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineGroup.workoutRoutine">Workout Routine</Translate>
          </dt>
          <dd>
            {workoutRoutineGroupEntity.workoutRoutines
              ? workoutRoutineGroupEntity.workoutRoutines.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {workoutRoutineGroupEntity.workoutRoutines && i === workoutRoutineGroupEntity.workoutRoutines.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/workout-routine-group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/workout-routine-group/${workoutRoutineGroupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkoutRoutineGroupDetail;
