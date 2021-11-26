import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { DurationFormat } from 'app/shared/DurationFormat';

import { searchEntities, getEntities } from './workout-routine-exercise-set.reducer';
import { IWorkoutRoutineExerciseSet } from 'app/shared/model/workout-routine-exercise-set.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutRoutineExerciseSet = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const workoutRoutineExerciseSetList = useAppSelector(state => state.workoutRoutineExerciseSet.entities);
  const loading = useAppSelector(state => state.workoutRoutineExerciseSet.loading);

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
      <h2 id="workout-routine-exercise-set-heading" data-cy="WorkoutRoutineExerciseSetHeading">
        <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.home.title">Workout Routine Exercise Sets</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.home.createLabel">
              Create new Workout Routine Exercise Set
            </Translate>
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
                  placeholder={translate('jhipsterSampleApplication1App.workoutRoutineExerciseSet.home.search')}
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
        {workoutRoutineExerciseSetList && workoutRoutineExerciseSetList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.reps">Reps</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.weight">Weight</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.time">Time</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.workoutRoutineExercise">
                    Workout Routine Exercise
                  </Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {workoutRoutineExerciseSetList.map((workoutRoutineExerciseSet, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${workoutRoutineExerciseSet.id}`} color="link" size="sm">
                      {workoutRoutineExerciseSet.id}
                    </Button>
                  </td>
                  <td>{workoutRoutineExerciseSet.reps}</td>
                  <td>{workoutRoutineExerciseSet.weight}</td>
                  <td>{workoutRoutineExerciseSet.time ? <DurationFormat value={workoutRoutineExerciseSet.time} /> : null}</td>
                  <td>
                    {workoutRoutineExerciseSet.workoutRoutineExercise ? (
                      <Link to={`workout-routine-exercise/${workoutRoutineExerciseSet.workoutRoutineExercise.id}`}>
                        {workoutRoutineExerciseSet.workoutRoutineExercise.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`${match.url}/${workoutRoutineExerciseSet.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workoutRoutineExerciseSet.id}/edit`}
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
                        to={`${match.url}/${workoutRoutineExerciseSet.id}/delete`}
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
              <Translate contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.home.notFound">
                No Workout Routine Exercise Sets found
              </Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default WorkoutRoutineExerciseSet;
