import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkoutRoutineExercise from './workout-routine-exercise';
import WorkoutRoutineExerciseDetail from './workout-routine-exercise-detail';
import WorkoutRoutineExerciseUpdate from './workout-routine-exercise-update';
import WorkoutRoutineExerciseDeleteDialog from './workout-routine-exercise-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkoutRoutineExerciseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkoutRoutineExerciseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkoutRoutineExerciseDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkoutRoutineExercise} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkoutRoutineExerciseDeleteDialog} />
  </>
);

export default Routes;
