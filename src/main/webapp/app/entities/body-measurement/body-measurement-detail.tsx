import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './body-measurement.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BodyMeasurementDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const bodyMeasurementEntity = useAppSelector(state => state.bodyMeasurement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bodyMeasurementDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.bodyMeasurement.detail.title">BodyMeasurement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bodyMeasurementEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="jhipsterSampleApplication1App.bodyMeasurement.value">Value</Translate>
            </span>
          </dt>
          <dd>{bodyMeasurementEntity.value}</dd>
          <dt>
            <span id="bodyMeasurementDateTime">
              <Translate contentKey="jhipsterSampleApplication1App.bodyMeasurement.bodyMeasurementDateTime">
                Body Measurement Date Time
              </Translate>
            </span>
          </dt>
          <dd>
            {bodyMeasurementEntity.bodyMeasurementDateTime ? (
              <TextFormat value={bodyMeasurementEntity.bodyMeasurementDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.bodyMeasurement.measurementType">Measurement Type</Translate>
          </dt>
          <dd>{bodyMeasurementEntity.measurementType ? bodyMeasurementEntity.measurementType.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/body-measurement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/body-measurement/${bodyMeasurementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BodyMeasurementDetail;
