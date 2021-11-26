import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './exercise-image.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ExerciseImageDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const exerciseImageEntity = useAppSelector(state => state.exerciseImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="exerciseImageDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.exerciseImage.detail.title">ExerciseImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{exerciseImageEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="jhipsterSampleApplication1App.exerciseImage.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{exerciseImageEntity.uuid}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="jhipsterSampleApplication1App.exerciseImage.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {exerciseImageEntity.image ? (
              <div>
                {exerciseImageEntity.imageContentType ? (
                  <a onClick={openFile(exerciseImageEntity.imageContentType, exerciseImageEntity.image)}>
                    <img
                      src={`data:${exerciseImageEntity.imageContentType};base64,${exerciseImageEntity.image}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {exerciseImageEntity.imageContentType}, {byteSize(exerciseImageEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="main">
              <Translate contentKey="jhipsterSampleApplication1App.exerciseImage.main">Main</Translate>
            </span>
          </dt>
          <dd>{exerciseImageEntity.main ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/exercise-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/exercise-image/${exerciseImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExerciseImageDetail;
