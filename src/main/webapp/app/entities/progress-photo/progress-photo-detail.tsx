import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './progress-photo.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProgressPhotoDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const progressPhotoEntity = useAppSelector(state => state.progressPhoto.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="progressPhotoDetailsHeading">
          <Translate contentKey="jhipsterSampleApplication1App.progressPhoto.detail.title">ProgressPhoto</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{progressPhotoEntity.id}</dd>
          <dt>
            <span id="note">
              <Translate contentKey="jhipsterSampleApplication1App.progressPhoto.note">Note</Translate>
            </span>
          </dt>
          <dd>{progressPhotoEntity.note}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="jhipsterSampleApplication1App.progressPhoto.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {progressPhotoEntity.image ? (
              <div>
                {progressPhotoEntity.imageContentType ? (
                  <a onClick={openFile(progressPhotoEntity.imageContentType, progressPhotoEntity.image)}>
                    <img
                      src={`data:${progressPhotoEntity.imageContentType};base64,${progressPhotoEntity.image}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {progressPhotoEntity.imageContentType}, {byteSize(progressPhotoEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="weightDate">
              <Translate contentKey="jhipsterSampleApplication1App.progressPhoto.weightDate">Weight Date</Translate>
            </span>
          </dt>
          <dd>
            {progressPhotoEntity.weightDate ? (
              <TextFormat value={progressPhotoEntity.weightDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/progress-photo" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/progress-photo/${progressPhotoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProgressPhotoDetail;
