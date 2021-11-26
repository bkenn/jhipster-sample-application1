import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProgressPhoto from './progress-photo';
import ProgressPhotoDetail from './progress-photo-detail';
import ProgressPhotoUpdate from './progress-photo-update';
import ProgressPhotoDeleteDialog from './progress-photo-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProgressPhotoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProgressPhotoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProgressPhotoDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProgressPhoto} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProgressPhotoDeleteDialog} />
  </>
);

export default Routes;
