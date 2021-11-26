import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ExerciseCategory from './exercise-category';
import ExerciseCategoryDetail from './exercise-category-detail';
import ExerciseCategoryUpdate from './exercise-category-update';
import ExerciseCategoryDeleteDialog from './exercise-category-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExerciseCategoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExerciseCategoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExerciseCategoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={ExerciseCategory} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ExerciseCategoryDeleteDialog} />
  </>
);

export default Routes;
