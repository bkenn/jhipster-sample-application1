import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkoutExercise from './workout-exercise';
import WorkoutExerciseDetail from './workout-exercise-detail';
import WorkoutExerciseUpdate from './workout-exercise-update';
import WorkoutExerciseDeleteDialog from './workout-exercise-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkoutExerciseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkoutExerciseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkoutExerciseDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkoutExercise} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkoutExerciseDeleteDialog} />
  </>
);

export default Routes;
