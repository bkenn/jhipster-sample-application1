import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IExercise } from 'app/shared/model/exercise.model';
import { getEntities as getExercises } from 'app/entities/exercise/exercise.reducer';
import { getEntity, updateEntity, createEntity, reset } from './muscle.reducer';
import { IMuscle } from 'app/shared/model/muscle.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const MuscleUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const exercises = useAppSelector(state => state.exercise.entities);
  const muscleEntity = useAppSelector(state => state.muscle.entity);
  const loading = useAppSelector(state => state.muscle.loading);
  const updating = useAppSelector(state => state.muscle.updating);
  const updateSuccess = useAppSelector(state => state.muscle.updateSuccess);
  const handleClose = () => {
    props.history.push('/muscle');
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
      ...muscleEntity,
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
          ...muscleEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplication1App.muscle.home.createOrEditLabel" data-cy="MuscleCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplication1App.muscle.home.createOrEditLabel">Create or edit a Muscle</Translate>
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
                  id="muscle-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.muscle.name')}
                id="muscle-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.muscle.description')}
                id="muscle-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.muscle.muscleOrder')}
                id="muscle-muscleOrder"
                name="muscleOrder"
                data-cy="muscleOrder"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.muscle.imageUrlMain')}
                id="muscle-imageUrlMain"
                name="imageUrlMain"
                data-cy="imageUrlMain"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.muscle.imageUrlSecondary')}
                id="muscle-imageUrlSecondary"
                name="imageUrlSecondary"
                data-cy="imageUrlSecondary"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.muscle.front')}
                id="muscle-front"
                name="front"
                data-cy="front"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/muscle" replace color="info">
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

export default MuscleUpdate;
