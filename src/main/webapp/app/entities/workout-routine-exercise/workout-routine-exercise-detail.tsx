import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './workout-routine-exercise.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutRoutineExerciseDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workoutRoutineExerciseEntity = useAppSelector(state => state.workoutRoutineExercise.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workoutRoutineExerciseDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExercise.detail.title">WorkoutRoutineExercise</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{workoutRoutineExerciseEntity.id}</dd>
          <dt>
            <span id="note">
              <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExercise.note">Note</Translate>
            </span>
          </dt>
          <dd>{workoutRoutineExerciseEntity.note}</dd>
          <dt>
            <span id="timer">
              <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExercise.timer">Timer</Translate>
            </span>
          </dt>
          <dd>
            {workoutRoutineExerciseEntity.timer ? <DurationFormat value={workoutRoutineExerciseEntity.timer} /> : null} (
            {workoutRoutineExerciseEntity.timer})
          </dd>
        </dl>
        <Button tag={Link} to="/workout-routine-exercise" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/workout-routine-exercise/${workoutRoutineExerciseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkoutRoutineExerciseDetail;
