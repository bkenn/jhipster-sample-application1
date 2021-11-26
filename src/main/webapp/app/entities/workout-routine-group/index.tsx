import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkoutRoutineGroup from './workout-routine-group';
import WorkoutRoutineGroupDetail from './workout-routine-group-detail';
import WorkoutRoutineGroupUpdate from './workout-routine-group-update';
import WorkoutRoutineGroupDeleteDialog from './workout-routine-group-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkoutRoutineGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkoutRoutineGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkoutRoutineGroupDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkoutRoutineGroup} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkoutRoutineGroupDeleteDialog} />
  </>
);

export default Routes;
