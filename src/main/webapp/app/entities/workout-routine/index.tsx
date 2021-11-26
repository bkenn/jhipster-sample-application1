import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkoutRoutine from './workout-routine';
import WorkoutRoutineDetail from './workout-routine-detail';
import WorkoutRoutineUpdate from './workout-routine-update';
import WorkoutRoutineDeleteDialog from './workout-routine-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkoutRoutineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkoutRoutineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkoutRoutineDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkoutRoutine} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkoutRoutineDeleteDialog} />
  </>
);

export default Routes;
