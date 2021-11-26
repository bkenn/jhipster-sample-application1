import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale from './locale';
import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from './user-management';
// prettier-ignore
import exercise from 'app/entities/exercise/exercise.reducer';
// prettier-ignore
import bodyMeasurement from 'app/entities/body-measurement/body-measurement.reducer';
// prettier-ignore
import exerciseCategory from 'app/entities/exercise-category/exercise-category.reducer';
// prettier-ignore
import exerciseImage from 'app/entities/exercise-image/exercise-image.reducer';
// prettier-ignore
import measurementType from 'app/entities/measurement-type/measurement-type.reducer';
// prettier-ignore
import muscle from 'app/entities/muscle/muscle.reducer';
// prettier-ignore
import progressPhoto from 'app/entities/progress-photo/progress-photo.reducer';
// prettier-ignore
import repType from 'app/entities/rep-type/rep-type.reducer';
// prettier-ignore
import weight from 'app/entities/weight/weight.reducer';
// prettier-ignore
import workout from 'app/entities/workout/workout.reducer';
// prettier-ignore
import workoutExercise from 'app/entities/workout-exercise/workout-exercise.reducer';
// prettier-ignore
import workoutExerciseSet from 'app/entities/workout-exercise-set/workout-exercise-set.reducer';
// prettier-ignore
import workoutRoutine from 'app/entities/workout-routine/workout-routine.reducer';
// prettier-ignore
import workoutRoutineExercise from 'app/entities/workout-routine-exercise/workout-routine-exercise.reducer';
// prettier-ignore
import workoutRoutineExerciseSet from 'app/entities/workout-routine-exercise-set/workout-routine-exercise-set.reducer';
// prettier-ignore
import workoutRoutineGroup from 'app/entities/workout-routine-group/workout-routine-group.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  exercise,
  bodyMeasurement,
  exerciseCategory,
  exerciseImage,
  measurementType,
  muscle,
  progressPhoto,
  repType,
  weight,
  workout,
  workoutExercise,
  workoutExerciseSet,
  workoutRoutine,
  workoutRoutineExercise,
  workoutRoutineExerciseSet,
  workoutRoutineGroup,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
