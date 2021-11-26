import { IWorkoutRoutineExercise } from 'app/shared/model/workout-routine-exercise.model';

export interface IWorkoutRoutineExerciseSet {
  id?: number;
  reps?: number | null;
  weight?: number | null;
  time?: string | null;
  workoutRoutineExercise?: IWorkoutRoutineExercise | null;
}

export const defaultValue: Readonly<IWorkoutRoutineExerciseSet> = {};
