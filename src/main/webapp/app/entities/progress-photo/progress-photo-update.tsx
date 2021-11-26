import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './progress-photo.reducer';
import { IProgressPhoto } from 'app/shared/model/progress-photo.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProgressPhotoUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const progressPhotoEntity = useAppSelector(state => state.progressPhoto.entity);
  const loading = useAppSelector(state => state.progressPhoto.loading);
  const updating = useAppSelector(state => state.progressPhoto.updating);
  const updateSuccess = useAppSelector(state => state.progressPhoto.updateSuccess);
  const handleClose = () => {
    props.history.push('/progress-photo');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.weightDate = convertDateTimeToServer(values.weightDate);

    const entity = {
      ...progressPhotoEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          weightDate: displayDefaultDateTime(),
        }
      : {
          ...progressPhotoEntity,
          weightDate: convertDateTimeFromServer(progressPhotoEntity.weightDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplication1App.progressPhoto.home.createOrEditLabel" data-cy="ProgressPhotoCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplication1App.progressPhoto.home.createOrEditLabel">
              Create or edit a ProgressPhoto
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="progress-photo-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.progressPhoto.note')}
                id="progress-photo-note"
                name="note"
                data-cy="note"
                type="text"
              />
              <ValidatedBlobField
                label={translate('jhipsterSampleApplication1App.progressPhoto.image')}
                id="progress-photo-image"
                name="image"
                data-cy="image"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.progressPhoto.weightDate')}
                id="progress-photo-weightDate"
                name="weightDate"
                data-cy="weightDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/progress-photo" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProgressPhotoUpdate;
