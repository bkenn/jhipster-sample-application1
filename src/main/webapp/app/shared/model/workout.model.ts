import dayjs from 'dayjs';
import { IWorkoutExercise } from 'app/shared/model/workout-exercise.model';
import { IWorkoutRoutine } from 'app/shared/model/workout-routine.model';
import { IUser } from 'app/shared/model/user.model';

export interface IWorkout {
  id?: number;
  title?: string | null;
  description?: string | null;
  workoutStartDateTime?: string | null;
  workoutEndDateTime?: string | null;
  workoutExercises?: IWorkoutExercise[] | null;
  workoutRoutine?: IWorkoutRoutine | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IWorkout> = {};
