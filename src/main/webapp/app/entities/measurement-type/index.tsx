import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MeasurementType from './measurement-type';
import MeasurementTypeDetail from './measurement-type-detail';
import MeasurementTypeUpdate from './measurement-type-update';
import MeasurementTypeDeleteDialog from './measurement-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MeasurementTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MeasurementTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MeasurementTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={MeasurementType} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MeasurementTypeDeleteDialog} />
  </>
);

export default Routes;
