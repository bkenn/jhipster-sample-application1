import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './weight.reducer';
import { IWeight } from 'app/shared/model/weight.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Weight = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const weightList = useAppSelector(state => state.weight.entities);
  const loading = useAppSelector(state => state.weight.loading);

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
      <h2 id="weight-heading" data-cy="WeightHeading">
        <Translate contentKey="jhipsterSampleApplication1App.weight.home.title">Weights</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="jhipsterSampleApplication1App.weight.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="jhipsterSampleApplication1App.weight.home.createLabel">Create new Weight</Translate>
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
                  placeholder={translate('jhipsterSampleApplication1App.weight.home.search')}
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
        {weightList && weightList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.weight.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.weight.value">Value</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.weight.weightDateTime">Weight Date Time</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.weight.measurementType">Measurement Type</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {weightList.map((weight, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${weight.id}`} color="link" size="sm">
                      {weight.id}
                    </Button>
                  </td>
                  <td>{weight.value}</td>
                  <td>
                    {weight.weightDateTime ? <TextFormat type="date" value={weight.weightDateTime} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {weight.measurementType ? (
                      <Link to={`measurement-type/${weight.measurementType.id}`}>{weight.measurementType.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${weight.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${weight.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${weight.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="jhipsterSampleApplication1App.weight.home.notFound">No Weights found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Weight;
