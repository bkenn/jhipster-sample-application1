import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ExerciseImage from './exercise-image';
import ExerciseImageDetail from './exercise-image-detail';
import ExerciseImageUpdate from './exercise-image-update';
import ExerciseImageDeleteDialog from './exercise-image-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExerciseImageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExerciseImageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExerciseImageDetail} />
      <ErrorBoundaryRoute path={match.url} component={ExerciseImage} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ExerciseImageDeleteDialog} />
  </>
);

export default Routes;
