import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './workout-exercise-set.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutExerciseSetDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workoutExerciseSetEntity = useAppSelector(state => state.workoutExerciseSet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workoutExerciseSetDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.detail.title">WorkoutExerciseSet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{workoutExerciseSetEntity.id}</dd>
          <dt>
            <span id="reps">
              <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.reps">Reps</Translate>
            </span>
          </dt>
          <dd>{workoutExerciseSetEntity.reps}</dd>
          <dt>
            <span id="weight">
              <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.weight">Weight</Translate>
            </span>
          </dt>
          <dd>{workoutExerciseSetEntity.weight}</dd>
          <dt>
            <span id="time">
              <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.time">Time</Translate>
            </span>
          </dt>
          <dd>
            {workoutExerciseSetEntity.time ? <DurationFormat value={workoutExerciseSetEntity.time} /> : null} (
            {workoutExerciseSetEntity.time})
          </dd>
          <dt>
            <span id="complete">
              <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.complete">Complete</Translate>
            </span>
          </dt>
          <dd>{workoutExerciseSetEntity.complete ? 'true' : 'false'}</dd>
          <dt>
            <span id="completeTime">
              <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.completeTime">Complete Time</Translate>
            </span>
          </dt>
          <dd>
            {workoutExerciseSetEntity.completeTime ? <DurationFormat value={workoutExerciseSetEntity.completeTime} /> : null} (
            {workoutExerciseSetEntity.completeTime})
          </dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.workoutExercise">Workout Exercise</Translate>
          </dt>
          <dd>{workoutExerciseSetEntity.workoutExercise ? workoutExerciseSetEntity.workoutExercise.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/workout-exercise-set" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/workout-exercise-set/${workoutExerciseSetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkoutExerciseSetDetail;
