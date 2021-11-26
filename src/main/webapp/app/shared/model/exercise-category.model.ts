import { IExercise } from 'app/shared/model/exercise.model';

export interface IExerciseCategory {
  id?: number;
  name?: string;
  categoryOrder?: number | null;
  exercises?: IExercise[] | null;
}

export const defaultValue: Readonly<IExerciseCategory> = {};
