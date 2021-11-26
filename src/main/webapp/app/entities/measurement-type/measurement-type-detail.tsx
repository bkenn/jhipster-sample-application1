import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './measurement-type.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const MeasurementTypeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const measurementTypeEntity = useAppSelector(state => state.measurementType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="measurementTypeDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.measurementType.detail.title">MeasurementType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{measurementTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterSampleApplication1App.measurementType.name">Name</Translate>
            </span>
          </dt>
          <dd>{measurementTypeEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="jhipsterSampleApplication1App.measurementType.description">Description</Translate>
            </span>
          </dt>
          <dd>{measurementTypeEntity.description}</dd>
          <dt>
            <span id="measurementOrder">
              <Translate contentKey="jhipsterSampleApplication1App.measurementType.measurementOrder">Measurement Order</Translate>
            </span>
          </dt>
          <dd>{measurementTypeEntity.measurementOrder}</dd>
          <dt>
            <span id="measurementUnit">
              <Translate contentKey="jhipsterSampleApplication1App.measurementType.measurementUnit">Measurement Unit</Translate>
            </span>
          </dt>
          <dd>{measurementTypeEntity.measurementUnit}</dd>
        </dl>
        <Button tag={Link} to="/measurement-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/measurement-type/${measurementTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MeasurementTypeDetail;
