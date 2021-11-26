import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './muscle.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const MuscleDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const muscleEntity = useAppSelector(state => state.muscle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="muscleDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.muscle.detail.title">Muscle</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{muscleEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterSampleApplication1App.muscle.name">Name</Translate>
            </span>
          </dt>
          <dd>{muscleEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="jhipsterSampleApplication1App.muscle.description">Description</Translate>
            </span>
          </dt>
          <dd>{muscleEntity.description}</dd>
          <dt>
            <span id="muscleOrder">
              <Translate contentKey="jhipsterSampleApplication1App.muscle.muscleOrder">Muscle Order</Translate>
            </span>
          </dt>
          <dd>{muscleEntity.muscleOrder}</dd>
          <dt>
            <span id="imageUrlMain">
              <Translate contentKey="jhipsterSampleApplication1App.muscle.imageUrlMain">Image Url Main</Translate>
            </span>
          </dt>
          <dd>{muscleEntity.imageUrlMain}</dd>
          <dt>
            <span id="imageUrlSecondary">
              <Translate contentKey="jhipsterSampleApplication1App.muscle.imageUrlSecondary">Image Url Secondary</Translate>
            </span>
          </dt>
          <dd>{muscleEntity.imageUrlSecondary}</dd>
          <dt>
            <span id="front">
              <Translate contentKey="jhipsterSampleApplication1App.muscle.front">Front</Translate>
            </span>
          </dt>
          <dd>{muscleEntity.front ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/muscle" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/muscle/${muscleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MuscleDetail;
