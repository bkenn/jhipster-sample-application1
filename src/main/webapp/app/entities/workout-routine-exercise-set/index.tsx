import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkoutRoutineExerciseSet from './workout-routine-exercise-set';
import WorkoutRoutineExerciseSetDetail from './workout-routine-exercise-set-detail';
import WorkoutRoutineExerciseSetUpdate from './workout-routine-exercise-set-update';
import WorkoutRoutineExerciseSetDeleteDialog from './workout-routine-exercise-set-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkoutRoutineExerciseSetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkoutRoutineExerciseSetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkoutRoutineExerciseSetDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkoutRoutineExerciseSet} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkoutRoutineExerciseSetDeleteDialog} />
  </>
);

export default Routes;
