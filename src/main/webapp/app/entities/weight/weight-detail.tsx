import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './weight.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WeightDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const weightEntity = useAppSelector(state => state.weight.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="weightDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.weight.detail.title">Weight</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{weightEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="jhipsterSampleApplication1App.weight.value">Value</Translate>
            </span>
          </dt>
          <dd>{weightEntity.value}</dd>
          <dt>
            <span id="weightDateTime">
              <Translate contentKey="jhipsterSampleApplication1App.weight.weightDateTime">Weight Date Time</Translate>
            </span>
          </dt>
          <dd>
            {weightEntity.weightDateTime ? <TextFormat value={weightEntity.weightDateTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.weight.measurementType">Measurement Type</Translate>
          </dt>
          <dd>{weightEntity.measurementType ? weightEntity.measurementType.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/weight" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/weight/${weightEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WeightDetail;
