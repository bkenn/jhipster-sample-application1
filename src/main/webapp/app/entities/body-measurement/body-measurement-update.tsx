import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IMeasurementType } from 'app/shared/model/measurement-type.model';
import { getEntities as getMeasurementTypes } from 'app/entities/measurement-type/measurement-type.reducer';
import { getEntity, updateEntity, createEntity, reset } from './body-measurement.reducer';
import { IBodyMeasurement } from 'app/shared/model/body-measurement.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BodyMeasurementUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const measurementTypes = useAppSelector(state => state.measurementType.entities);
  const bodyMeasurementEntity = useAppSelector(state => state.bodyMeasurement.entity);
  const loading = useAppSelector(state => state.bodyMeasurement.loading);
  const updating = useAppSelector(state => state.bodyMeasurement.updating);
  const updateSuccess = useAppSelector(state => state.bodyMeasurement.updateSuccess);
  const handleClose = () => {
    props.history.push('/body-measurement');
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
    values.bodyMeasurementDateTime = convertDateTimeToServer(values.bodyMeasurementDateTime);

    const entity = {
      ...bodyMeasurementEntity,
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
          bodyMeasurementDateTime: displayDefaultDateTime(),
        }
      : {
          ...bodyMeasurementEntity,
          bodyMeasurementDateTime: convertDateTimeFromServer(bodyMeasurementEntity.bodyMeasurementDateTime),
          measurementType: bodyMeasurementEntity?.measurementType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplication1App.bodyMeasurement.home.createOrEditLabel" data-cy="BodyMeasurementCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplication1App.bodyMeasurement.home.createOrEditLabel">
              Create or edit a BodyMeasurement
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
                  id="body-measurement-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.bodyMeasurement.value')}
                id="body-measurement-value"
                name="value"
                data-cy="value"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('jhipsterSampleApplication1App.bodyMeasurement.bodyMeasurementDateTime')}
                id="body-measurement-bodyMeasurementDateTime"
                name="bodyMeasurementDateTime"
                data-cy="bodyMeasurementDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="body-measurement-measurementType"
                name="measurementType"
                data-cy="measurementType"
                label={translate('jhipsterSampleApplication1App.bodyMeasurement.measurementType')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/body-measurement" replace color="info">
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

export default BodyMeasurementUpdate;
