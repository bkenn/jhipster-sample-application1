import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './body-measurement.reducer';
import { IBodyMeasurement } from 'app/shared/model/body-measurement.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BodyMeasurement = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const bodyMeasurementList = useAppSelector(state => state.bodyMeasurement.entities);
  const loading = useAppSelector(state => state.bodyMeasurement.loading);

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
      <h2 id="body-measurement-heading" data-cy="BodyMeasurementHeading">
        <Translate contentKey="jhipsterSampleApplication1App.bodyMeasurement.home.title">Body Measurements</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="jhipsterSampleApplication1App.bodyMeasurement.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="jhipsterSampleApplication1App.bodyMeasurement.home.createLabel">Create new Body Measurement</Translate>
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
                  placeholder={translate('jhipsterSampleApplication1App.bodyMeasurement.home.search')}
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
        {bodyMeasurementList && bodyMeasurementList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.bodyMeasurement.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.bodyMeasurement.value">Value</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.bodyMeasurement.bodyMeasurementDateTime">
                    Body Measurement Date Time
                  </Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplication1App.bodyMeasurement.measurementType">Measurement Type</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bodyMeasurementList.map((bodyMeasurement, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${bodyMeasurement.id}`} color="link" size="sm">
                      {bodyMeasurement.id}
                    </Button>
                  </td>
                  <td>{bodyMeasurement.value}</td>
                  <td>
                    {bodyMeasurement.bodyMeasurementDateTime ? (
                      <TextFormat type="date" value={bodyMeasurement.bodyMeasurementDateTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {bodyMeasurement.measurementType ? (
                      <Link to={`measurement-type/${bodyMeasurement.measurementType.id}`}>{bodyMeasurement.measurementType.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${bodyMeasurement.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${bodyMeasurement.id}/edit`}
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
                        to={`${match.url}/${bodyMeasurement.id}/delete`}
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
              <Translate contentKey="jhipsterSampleApplication1App.bodyMeasurement.home.notFound">No Body Measurements found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default BodyMeasurement;
