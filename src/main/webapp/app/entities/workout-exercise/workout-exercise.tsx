import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { DurationFormat } from 'app/shared/DurationFormat';

import { searchEntities, getEntities } from './workout-exercise.reducer';
import { IWorkoutExercise } from 'app/shared/model/workout-exercise.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutExercise = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const workoutExerciseList = useAppSelector(state => state.workoutExercise.entities);
  const loading = useAppSelector(state => state.workoutExercise.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const startSearching = e => {
    if (search) {
      dispatch(searchEntities({ query: search }));
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="workout-exercise-heading" data-cy="WorkoutExerciseHeading">
        <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.home.title">Workout Exercises</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.home.createLabel">Create new Workout Exercise</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('jhipsterSampleApplication1App.workoutExercise.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {workoutExerciseList && workoutExerciseList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.note">Note</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.timer">Timer</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.exercise">Exercise</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.workoutRoutineExercise">
                    Workout Routine Exercise
                  </Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.workout">Workout</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {workoutExerciseList.map((workoutExercise, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${workoutExercise.id}`} color="link" size="sm">
                      {workoutExercise.id}
                    </Button>
                  </td>
                  <td>{workoutExercise.note}</td>
                  <td>{workoutExercise.timer ? <DurationFormat value={workoutExercise.timer} /> : null}</td>
                  <td>
                    {workoutExercise.exercise ? (
                      <Link to={`exercise/${workoutExercise.exercise.id}`}>{workoutExercise.exercise.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {workoutExercise.workoutRoutineExercise ? (
                      <Link to={`workout-routine-exercise/${workoutExercise.workoutRoutineExercise.id}`}>
                        {workoutExercise.workoutRoutineExercise.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {workoutExercise.workout ? <Link to={`workout/${workoutExercise.workout.id}`}>{workoutExercise.workout.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${workoutExercise.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workoutExercise.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workoutExercise.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="jhipsterSampleApplication1App.workoutExercise.home.notFound">No Workout Exercises found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default WorkoutExercise;
