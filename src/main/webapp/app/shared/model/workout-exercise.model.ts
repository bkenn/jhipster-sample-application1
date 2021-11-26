import { IWorkoutExerciseSet } from 'app/shared/model/workout-exercise-set.model';
import { IExercise } from 'app/shared/model/exercise.model';
import { IWorkoutRoutineExercise } from 'app/shared/model/workout-routine-exercise.model';
import { IWorkout } from 'app/shared/model/workout.model';

export interface IWorkoutExercise {
  id?: number;
  note?: string | null;
  timer?: string | null;
  workoutExerciseSets?: IWorkoutExerciseSet[] | null;
  exercise?: IExercise | null;
  workoutRoutineExercise?: IWorkoutRoutineExercise | null;
  workout?: IWorkout | null;
}

export const defaultValue: Readonly<IWorkoutExercise> = {};
