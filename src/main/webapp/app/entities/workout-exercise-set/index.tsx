import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkoutExerciseSet from './workout-exercise-set';
import WorkoutExerciseSetDetail from './workout-exercise-set-detail';
import WorkoutExerciseSetUpdate from './workout-exercise-set-update';
import WorkoutExerciseSetDeleteDialog from './workout-exercise-set-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkoutExerciseSetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkoutExerciseSetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkoutExerciseSetDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkoutExerciseSet} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkoutExerciseSetDeleteDialog} />
  </>
);

export default Routes;
