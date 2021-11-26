import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './workout-routine.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutRoutineDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workoutRoutineEntity = useAppSelector(state => state.workoutRoutine.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workoutRoutineDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.workoutRoutine.detail.title">WorkoutRoutine</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{workoutRoutineEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="jhipsterSampleApplication1App.workoutRoutine.title">Title</Translate>
            </span>
          </dt>
          <dd>{workoutRoutineEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="jhipsterSampleApplication1App.workoutRoutine.description">Description</Translate>
            </span>
          </dt>
          <dd>{workoutRoutineEntity.description}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.workoutRoutine.workoutRoutineExercise">Workout Routine Exercise</Translate>
          </dt>
          <dd>{workoutRoutineEntity.workoutRoutineExercise ? workoutRoutineEntity.workoutRoutineExercise.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/workout-routine" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/workout-routine/${workoutRoutineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkoutRoutineDetail;
