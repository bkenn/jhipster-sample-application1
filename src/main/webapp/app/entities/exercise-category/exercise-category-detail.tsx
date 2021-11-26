import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './exercise-category.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ExerciseCategoryDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const exerciseCategoryEntity = useAppSelector(state => state.exerciseCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="exerciseCategoryDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.exerciseCategory.detail.title">ExerciseCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{exerciseCategoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterSampleApplication1App.exerciseCategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{exerciseCategoryEntity.name}</dd>
          <dt>
            <span id="categoryOrder">
              <Translate contentKey="jhipsterSampleApplication1App.exerciseCategory.categoryOrder">Category Order</Translate>
            </span>
          </dt>
          <dd>{exerciseCategoryEntity.categoryOrder}</dd>
        </dl>
        <Button tag={Link} to="/exercise-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/exercise-category/${exerciseCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExerciseCategoryDetail;
