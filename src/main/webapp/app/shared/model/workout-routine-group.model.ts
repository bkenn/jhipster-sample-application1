import { IWorkoutRoutine } from 'app/shared/model/workout-routine.model';

export interface IWorkoutRoutineGroup {
  id?: number;
  name?: string | null;
  workoutRoutines?: IWorkoutRoutine[] | null;
}

export const defaultValue: Readonly<IWorkoutRoutineGroup> = {};
