import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './exercise.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ExerciseDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const exerciseEntity = useAppSelector(state => state.exercise.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="exerciseDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.exercise.detail.title">Exercise</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{exerciseEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterSampleApplication1App.exercise.name">Name</Translate>
            </span>
          </dt>
          <dd>{exerciseEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="jhipsterSampleApplication1App.exercise.description">Description</Translate>
            </span>
          </dt>
          <dd>{exerciseEntity.description}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.exercise.repType">Rep Type</Translate>
          </dt>
          <dd>{exerciseEntity.repType ? exerciseEntity.repType.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.exercise.exerciseCategory">Exercise Category</Translate>
          </dt>
          <dd>{exerciseEntity.exerciseCategory ? exerciseEntity.exerciseCategory.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.exercise.exerciseImage">Exercise Image</Translate>
          </dt>
          <dd>
            {exerciseEntity.exerciseImages
              ? exerciseEntity.exerciseImages.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {exerciseEntity.exerciseImages && i === exerciseEntity.exerciseImages.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplication1App.exercise.muscle">Muscle</Translate>
          </dt>
          <dd>
            {exerciseEntity.muscles
              ? exerciseEntity.muscles.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {exerciseEntity.muscles && i === exerciseEntity.muscles.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/exercise" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/exercise/${exerciseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExerciseDetail;
