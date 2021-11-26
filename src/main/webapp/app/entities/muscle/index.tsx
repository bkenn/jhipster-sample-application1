import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Muscle from './muscle';
import MuscleDetail from './muscle-detail';
import MuscleUpdate from './muscle-update';
import MuscleDeleteDialog from './muscle-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MuscleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MuscleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MuscleDetail} />
      <ErrorBoundaryRoute path={match.url} component={Muscle} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MuscleDeleteDialog} />
  </>
);

export default Routes;
