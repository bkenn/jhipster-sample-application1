import { IExercise } from 'app/shared/model/exercise.model';

export interface IRepType {
  id?: number;
  name?: string | null;
  display?: string | null;
  exercises?: IExercise[] | null;
}

export const defaultValue: Readonly<IRepType> = {};
