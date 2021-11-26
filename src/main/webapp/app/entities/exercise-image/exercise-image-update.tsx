import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IExercise } from 'app/shared/model/exercise.model';
import { getEntities as getExercises } from 'app/entities/exercise/exercise.reducer';
import { getEntity, updateEntity, createEntity, reset } from './exercise-image.reducer';
import { IExerciseImage } from 'app/shared/model/exercise-image.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ExerciseImageUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const exercises = useAppSelector(state => state.exercise.entities);
  const exerciseImageEntity = useAppSelector(state => state.exerciseImage.entity);
  const loading = useAppSelector(state => state.exerciseImage.loading);
  const updating = useAppSelector(state => state.exerciseImage.updating);
  const updateSuccess = useAppSelector(state => state.exerciseImage.updateSuccess);
  const handleClose = () => {
    props.history.push('/exercise-image');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getExercises({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...exerciseImageEntity,
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
      ? {}
      : {
          ...exerciseImageEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplication1App.exerciseImage.home.createOrEditLabel" data-cy="ExerciseImageCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplication1App.exerciseImage.home.createOrEditLabel">
              Create or edit a ExerciseImage
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
                  id="exercise-image-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.exerciseImage.uuid')}
                id="exercise-image-uuid"
                name="uuid"
                data-cy="uuid"
                type="text"
              />
              <ValidatedBlobField
                label={translate('jhipsterSampleApplication1App.exerciseImage.image')}
                id="exercise-image-image"
                name="image"
                data-cy="image"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.exerciseImage.main')}
                id="exercise-image-main"
                name="main"
                data-cy="main"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/exercise-image" replace color="info">
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

export default ExerciseImageUpdate;
