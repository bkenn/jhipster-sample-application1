import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Exercise from './exercise';
import BodyMeasurement from './body-measurement';
import ExerciseCategory from './exercise-category';
import ExerciseImage from './exercise-image';
import MeasurementType from './measurement-type';
import Muscle from './muscle';
import ProgressPhoto from './progress-photo';
import RepType from './rep-type';
import Weight from './weight';
import Workout from './workout';
import WorkoutExercise from './workout-exercise';
import WorkoutExerciseSet from './workout-exercise-set';
import WorkoutRoutine from './workout-routine';
import WorkoutRoutineExercise from './workout-routine-exercise';
import WorkoutRoutineExerciseSet from './workout-routine-exercise-set';
import WorkoutRoutineGroup from './workout-routine-group';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}exercise`} component={Exercise} />
      <ErrorBoundaryRoute path={`${match.url}body-measurement`} component={BodyMeasurement} />
      <ErrorBoundaryRoute path={`${match.url}exercise-category`} component={ExerciseCategory} />
      <ErrorBoundaryRoute path={`${match.url}exercise-image`} component={ExerciseImage} />
      <ErrorBoundaryRoute path={`${match.url}measurement-type`} component={MeasurementType} />
      <ErrorBoundaryRoute path={`${match.url}muscle`} component={Muscle} />
      <ErrorBoundaryRoute path={`${match.url}progress-photo`} component={ProgressPhoto} />
      <ErrorBoundaryRoute path={`${match.url}rep-type`} component={RepType} />
      <ErrorBoundaryRoute path={`${match.url}weight`} component={Weight} />
      <ErrorBoundaryRoute path={`${match.url}workout`} component={Workout} />
      <ErrorBoundaryRoute path={`${match.url}workout-exercise`} component={WorkoutExercise} />
      <ErrorBoundaryRoute path={`${match.url}workout-exercise-set`} component={WorkoutExerciseSet} />
      <ErrorBoundaryRoute path={`${match.url}workout-routine`} component={WorkoutRoutine} />
      <ErrorBoundaryRoute path={`${match.url}workout-routine-exercise`} component={WorkoutRoutineExercise} />
      <ErrorBoundaryRoute path={`${match.url}workout-routine-exercise-set`} component={WorkoutRoutineExerciseSet} />
      <ErrorBoundaryRoute path={`${match.url}workout-routine-group`} component={WorkoutRoutineGroup} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
