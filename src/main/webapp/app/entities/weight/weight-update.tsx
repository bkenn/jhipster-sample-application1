import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IMeasurementType } from 'app/shared/model/measurement-type.model';
import { getEntities as getMeasurementTypes } from 'app/entities/measurement-type/measurement-type.reducer';
import { getEntity, updateEntity, createEntity, reset } from './weight.reducer';
import { IWeight } from 'app/shared/model/weight.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WeightUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const measurementTypes = useAppSelector(state => state.measurementType.entities);
  const weightEntity = useAppSelector(state => state.weight.entity);
  const loading = useAppSelector(state => state.weight.loading);
  const updating = useAppSelector(state => state.weight.updating);
  const updateSuccess = useAppSelector(state => state.weight.updateSuccess);
  const handleClose = () => {
    props.history.push('/weight');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getMeasurementTypes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.weightDateTime = convertDateTimeToServer(values.weightDateTime);

    const entity = {
      ...weightEntity,
      ...values,
      measurementType: measurementTypes.find(it => it.id.toString() === values.measurementType.toString()),
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
          weightDateTime: displayDefaultDateTime(),
        }
      : {
          ...weightEntity,
          weightDateTime: convertDateTimeFromServer(weightEntity.weightDateTime),
          measurementType: weightEntity?.measurementType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplication1App.weight.home.createOrEditLabel" data-cy="WeightCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplication1App.weight.home.createOrEditLabel">Create or edit a Weight</Translate>
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
                  id="weight-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.weight.value')}
                id="weight-value"
                name="value"
                data-cy="value"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.weight.weightDateTime')}
                id="weight-weightDateTime"
                name="weightDateTime"
                data-cy="weightDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="weight-measurementType"
                name="measurementType"
                data-cy="measurementType"
                label={translate('jhipsterSampleApplication1App.weight.measurementType')}
                type="select"
              >
                <option value="" key="0" />
                {measurementTypes
                  ? measurementTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/weight" replace color="info">
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

export default WeightUpdate;
