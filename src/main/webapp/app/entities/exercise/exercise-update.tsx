import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRepType } from 'app/shared/model/rep-type.model';
import { getEntities as getRepTypes } from 'app/entities/rep-type/rep-type.reducer';
import { IExerciseCategory } from 'app/shared/model/exercise-category.model';
import { getEntities as getExerciseCategories } from 'app/entities/exercise-category/exercise-category.reducer';
import { IExerciseImage } from 'app/shared/model/exercise-image.model';
import { getEntities as getExerciseImages } from 'app/entities/exercise-image/exercise-image.reducer';
import { IMuscle } from 'app/shared/model/muscle.model';
import { getEntities as getMuscles } from 'app/entities/muscle/muscle.reducer';
import { getEntity, updateEntity, createEntity, reset } from './exercise.reducer';
import { IExercise } from 'app/shared/model/exercise.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ExerciseUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const repTypes = useAppSelector(state => state.repType.entities);
  const exerciseCategories = useAppSelector(state => state.exerciseCategory.entities);
  const exerciseImages = useAppSelector(state => state.exerciseImage.entities);
  const muscles = useAppSelector(state => state.muscle.entities);
  const exerciseEntity = useAppSelector(state => state.exercise.entity);
  const loading = useAppSelector(state => state.exercise.loading);
  const updating = useAppSelector(state => state.exercise.updating);
  const updateSuccess = useAppSelector(state => state.exercise.updateSuccess);
  const handleClose = () => {
    props.history.push('/exercise');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getRepTypes({}));
    dispatch(getExerciseCategories({}));
    dispatch(getExerciseImages({}));
    dispatch(getMuscles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...exerciseEntity,
      ...values,
      exerciseImages: mapIdList(values.exerciseImages),
      muscles: mapIdList(values.muscles),
      repType: repTypes.find(it => it.id.toString() === values.repType.toString()),
      exerciseCategory: exerciseCategories.find(it => it.id.toString() === values.exerciseCategory.toString()),
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
          ...exerciseEntity,
          repType: exerciseEntity?.repType?.id,
          exerciseCategory: exerciseEntity?.exerciseCategory?.id,
          exerciseImages: exerciseEntity?.exerciseImages?.map(e => e.id.toString()),
          muscles: exerciseEntity?.muscles?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplication1App.exercise.home.createOrEditLabel" data-cy="ExerciseCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplication1App.exercise.home.createOrEditLabel">Create or edit a Exercise</Translate>
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
                  id="exercise-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.exercise.name')}
                id="exercise-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.exercise.description')}
                id="exercise-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="exercise-repType"
                name="repType"
                data-cy="repType"
                label={translate('jhipsterSampleApplication1App.exercise.repType')}
                type="select"
              >
                <option value="" key="0" />
                {repTypes
                  ? repTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="exercise-exerciseCategory"
                name="exerciseCategory"
                data-cy="exerciseCategory"
                label={translate('jhipsterSampleApplication1App.exercise.exerciseCategory')}
                type="select"
              >
                <option value="" key="0" />
                {exerciseCategories
                  ? exerciseCategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.exercise.exerciseImage')}
                id="exercise-exerciseImage"
                data-cy="exerciseImage"
                type="select"
                multiple
                name="exerciseImages"
              >
                <option value="" key="0" />
                {exerciseImages
                  ? exerciseImages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.exercise.muscle')}
                id="exercise-muscle"
                data-cy="muscle"
                type="select"
                multiple
                name="muscles"
              >
                <option value="" key="0" />
                {muscles
                  ? muscles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/exercise" replace color="info">
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

export default ExerciseUpdate;
