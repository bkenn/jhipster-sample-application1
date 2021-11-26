import { IWorkoutRoutineExerciseSet } from 'app/shared/model/workout-routine-exercise-set.model';
import { IWorkoutExercise } from 'app/shared/model/workout-exercise.model';
import { IWorkoutRoutine } from 'app/shared/model/workout-routine.model';

export interface IWorkoutRoutineExercise {
  id?: number;
  note?: string | null;
  timer?: string | null;
  workoutRoutineExerciseSets?: IWorkoutRoutineExerciseSet[] | null;
  workoutExercises?: IWorkoutExercise[] | null;
  workoutRoutines?: IWorkoutRoutine[] | null;
}

export const defaultValue: Readonly<IWorkoutRoutineExercise> = {};
