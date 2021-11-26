import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RepType from './rep-type';
import RepTypeDetail from './rep-type-detail';
import RepTypeUpdate from './rep-type-update';
import RepTypeDeleteDialog from './rep-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RepTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RepTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RepTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={RepType} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RepTypeDeleteDialog} />
  </>
);

export default Routes;
