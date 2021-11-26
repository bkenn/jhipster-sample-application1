import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './workout-routine-exercise-set.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutRoutineExerciseSetDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workoutRoutineExerciseSetEntity = useAppSelector(state => state.workoutRoutineExerciseSet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workoutRoutineExerciseSetDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.detail.title">WorkoutRoutineExerciseSet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{workoutRoutineExerciseSetEntity.id}</dd>
          <dt>
            <span id="reps">
              <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.reps">Reps</Translate>
            </span>
          </dt>
          <dd>{workoutRoutineExerciseSetEntity.reps}</dd>
          <dt>
            <span id="weight">
              <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.weight">Weight</Translate>
            </span>
          </dt>
          <dd>{workoutRoutineExerciseSetEntity.weight}</dd>
          <dt>
            <span id="time">
              <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.time">Time</Translate>
            </span>
          </dt>
          <dd>
            {workoutRoutineExerciseSetEntity.time ? <DurationFormat value={workoutRoutineExerciseSetEntity.time} /> : null} (
            {workoutRoutineExerciseSetEntity.time})
          </dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.workoutRoutineExercise">
              Workout Routine Exercise
            </Translate>
          </dt>
          <dd>{workoutRoutineExerciseSetEntity.workoutRoutineExercise ? workoutRoutineExerciseSetEntity.workoutRoutineExercise.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/workout-routine-exercise-set" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/workout-routine-exercise-set/${workoutRoutineExerciseSetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkoutRoutineExerciseSetDetail;
