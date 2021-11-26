import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './exercise.reducer';
import { IExercise } from 'app/shared/model/exercise.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Exercise = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const exerciseList = useAppSelector(state => state.exercise.entities);
  const loading = useAppSelector(state => state.exercise.loading);

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
      <h2 id="exercise-heading" data-cy="ExerciseHeading">
        <Translate contentKey="jhipsterSampleApplication1App.exercise.home.title">Exercises</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="jhipsterSampleApplication1App.exercise.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="jhipsterSampleApplication1App.exercise.home.createLabel">Create new Exercise</Translate>
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
                  placeholder={translate('jhipsterSampleApplication1App.exercise.home.search')}
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
        {exerciseList && exerciseList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.exercise.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.exercise.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.exercise.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.exercise.repType">Rep Type</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.exercise.exerciseCategory">Exercise Category</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.exercise.exerciseImage">Exercise Image</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.exercise.muscle">Muscle</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {exerciseList.map((exercise, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${exercise.id}`} color="link" size="sm">
                      {exercise.id}
                    </Button>
                  </td>
                  <td>{exercise.name}</td>
                  <td>{exercise.description}</td>
                  <td>{exercise.repType ? <Link to={`rep-type/${exercise.repType.id}`}>{exercise.repType.id}</Link> : ''}</td>
                  <td>
                    {exercise.exerciseCategory ? (
                      <Link to={`exercise-category/${exercise.exerciseCategory.id}`}>{exercise.exerciseCategory.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {exercise.exerciseImages
                      ? exercise.exerciseImages.map((val, j) => (
                          <span key={j}>
                            <Link to={`exercise-image/${val.id}`}>{val.id}</Link>
                            {j === exercise.exerciseImages.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {exercise.muscles
                      ? exercise.muscles.map((val, j) => (
                          <span key={j}>
                            <Link to={`muscle/${val.id}`}>{val.id}</Link>
                            {j === exercise.muscles.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${exercise.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${exercise.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${exercise.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="jhipsterSampleApplication1App.exercise.home.notFound">No Exercises found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Exercise;
