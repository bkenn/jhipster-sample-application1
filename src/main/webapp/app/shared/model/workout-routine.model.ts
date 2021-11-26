import { IWorkoutRoutineExercise } from 'app/shared/model/workout-routine-exercise.model';
import { IWorkout } from 'app/shared/model/workout.model';
import { IWorkoutRoutineGroup } from 'app/shared/model/workout-routine-group.model';

export interface IWorkoutRoutine {
  id?: number;
  title?: string | null;
  description?: string | null;
  workoutRoutineExercise?: IWorkoutRoutineExercise | null;
  workouts?: IWorkout[] | null;
  workoutRoutineGroups?: IWorkoutRoutineGroup[] | null;
}

export const defaultValue: Readonly<IWorkoutRoutine> = {};
