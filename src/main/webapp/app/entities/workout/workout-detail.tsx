import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './workout.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workoutEntity = useAppSelector(state => state.workout.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workoutDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.workout.detail.title">Workout</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{workoutEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="jhipsterSampleApplication1App.workout.title">Title</Translate>
            </span>
          </dt>
          <dd>{workoutEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="jhipsterSampleApplication1App.workout.description">Description</Translate>
            </span>
          </dt>
          <dd>{workoutEntity.description}</dd>
          <dt>
            <span id="workoutStartDateTime">
              <Translate contentKey="jhipsterSampleApplication1App.workout.workoutStartDateTime">Workout Start Date Time</Translate>
            </span>
          </dt>
          <dd>
            {workoutEntity.workoutStartDateTime ? (
              <TextFormat value={workoutEntity.workoutStartDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="workoutEndDateTime">
              <Translate contentKey="jhipsterSampleApplication1App.workout.workoutEndDateTime">Workout End Date Time</Translate>
            </span>
          </dt>
          <dd>
            {workoutEntity.workoutEndDateTime ? (
              <TextFormat value={workoutEntity.workoutEndDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.workout.workoutRoutine">Workout Routine</Translate>
          </dt>
          <dd>{workoutEntity.workoutRoutine ? workoutEntity.workoutRoutine.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.workout.user">User</Translate>
          </dt>
          <dd>{workoutEntity.user ? workoutEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/workout" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/workout/${workoutEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkoutDetail;
