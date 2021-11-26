import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './workout.reducer';
import { IWorkout } from 'app/shared/model/workout.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Workout = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const workoutList = useAppSelector(state => state.workout.entities);
  const loading = useAppSelector(state => state.workout.loading);

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
      <h2 id="workout-heading" data-cy="WorkoutHeading">
        <Translate contentKey="jhipsterSampleApplication1App.workout.home.title">Workouts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="jhipsterSampleApplication1App.workout.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="jhipsterSampleApplication1App.workout.home.createLabel">Create new Workout</Translate>
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
                  placeholder={translate('jhipsterSampleApplication1App.workout.home.search')}
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
        {workoutList && workoutList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workout.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workout.title">Title</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workout.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workout.workoutStartDateTime">Workout Start Date Time</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workout.workoutEndDateTime">Workout End Date Time</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workout.workoutRoutine">Workout Routine</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.workout.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {workoutList.map((workout, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${workout.id}`} color="link" size="sm">
                      {workout.id}
                    </Button>
                  </td>
                  <td>{workout.title}</td>
                  <td>{workout.description}</td>
                  <td>
                    {workout.workoutStartDateTime ? (
                      <TextFormat type="date" value={workout.workoutStartDateTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {workout.workoutEndDateTime ? (
                      <TextFormat type="date" value={workout.workoutEndDateTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {workout.workoutRoutine ? (
                      <Link to={`workout-routine/${workout.workoutRoutine.id}`}>{workout.workoutRoutine.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{workout.user ? workout.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${workout.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${workout.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${workout.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="jhipsterSampleApplication1App.workout.home.notFound">No Workouts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Workout;
