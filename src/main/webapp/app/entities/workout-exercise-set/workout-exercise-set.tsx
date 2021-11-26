import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { DurationFormat } from 'app/shared/DurationFormat';

import { searchEntities, getEntities } from './workout-exercise-set.reducer';
import { IWorkoutExerciseSet } from 'app/shared/model/workout-exercise-set.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkoutExerciseSet = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const workoutExerciseSetList = useAppSelector(state => state.workoutExerciseSet.entities);
  const loading = useAppSelector(state => state.workoutExerciseSet.loading);

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
      <h2 id="workout-exercise-set-heading" data-cy="WorkoutExerciseSetHeading">
        <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.home.title">Workout Exercise Sets</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.home.createLabel">
              Create new Workout Exercise Set
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
                  placeholder={translate('jhipsterSampleApplication1App.workoutExerciseSet.home.search')}
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
        {workoutExerciseSetList && workoutExerciseSetList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.reps">Reps</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.weight">Weight</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.time">Time</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.complete">Complete</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.completeTime">Complete Time</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.workoutExercise">Workout Exercise</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {workoutExerciseSetList.map((workoutExerciseSet, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${workoutExerciseSet.id}`} color="link" size="sm">
                      {workoutExerciseSet.id}
                    </Button>
                  </td>
                  <td>{workoutExerciseSet.reps}</td>
                  <td>{workoutExerciseSet.weight}</td>
                  <td>{workoutExerciseSet.time ? <DurationFormat value={workoutExerciseSet.time} /> : null}</td>
                  <td>{workoutExerciseSet.complete ? 'true' : 'false'}</td>
                  <td>{workoutExerciseSet.completeTime ? <DurationFormat value={workoutExerciseSet.completeTime} /> : null}</td>
                  <td>
                    {workoutExerciseSet.workoutExercise ? (
                      <Link to={`workout-exercise/${workoutExerciseSet.workoutExercise.id}`}>{workoutExerciseSet.workoutExercise.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${workoutExerciseSet.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workoutExerciseSet.id}/edit`}
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
                        to={`${match.url}/${workoutExerciseSet.id}/delete`}
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
              <Translate contentKey="jhipsterSampleApplication1App.workoutExerciseSet.home.notFound">
                No Workout Exercise Sets found
              </Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default WorkoutExerciseSet;
