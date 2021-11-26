import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './workout-exercise.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutExerciseDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workoutExerciseEntity = useAppSelector(state => state.workoutExercise.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workoutExerciseDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.detail.title">WorkoutExercise</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{workoutExerciseEntity.id}</dd>
          <dt>
            <span id="note">
              <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.note">Note</Translate>
            </span>
          </dt>
          <dd>{workoutExerciseEntity.note}</dd>
          <dt>
            <span id="timer">
              <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.timer">Timer</Translate>
            </span>
          </dt>
          <dd>
            {workoutExerciseEntity.timer ? <DurationFormat value={workoutExerciseEntity.timer} /> : null} ({workoutExerciseEntity.timer})
          </dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.exercise">Exercise</Translate>
          </dt>
          <dd>{workoutExerciseEntity.exercise ? workoutExerciseEntity.exercise.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.workoutRoutineExercise">
              Workout Routine Exercise
            </Translate>
          </dt>
          <dd>{workoutExerciseEntity.workoutRoutineExercise ? workoutExerciseEntity.workoutRoutineExercise.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.workout">Workout</Translate>
          </dt>
          <dd>{workoutExerciseEntity.workout ? workoutExerciseEntity.workout.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/workout-exercise" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/workout-exercise/${workoutExerciseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkoutExerciseDetail;
