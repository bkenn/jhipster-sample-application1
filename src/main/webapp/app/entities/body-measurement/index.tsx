import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BodyMeasurement from './body-measurement';
import BodyMeasurementDetail from './body-measurement-detail';
import BodyMeasurementUpdate from './body-measurement-update';
import BodyMeasurementDeleteDialog from './body-measurement-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BodyMeasurementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BodyMeasurementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BodyMeasurementDetail} />
      <ErrorBoundaryRoute path={match.url} component={BodyMeasurement} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BodyMeasurementDeleteDialog} />
  </>
);

export default Routes;
