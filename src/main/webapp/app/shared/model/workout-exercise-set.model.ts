import { IWorkoutExercise } from 'app/shared/model/workout-exercise.model';

export interface IWorkoutExerciseSet {
  id?: number;
  reps?: number | null;
  weight?: number | null;
  time?: string | null;
  complete?: boolean | null;
  completeTime?: string | null;
  workoutExercise?: IWorkoutExercise | null;
}

export const defaultValue: Readonly<IWorkoutExerciseSet> = {
  complete: false,
};
