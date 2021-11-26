import { IExercise } from 'app/shared/model/exercise.model';

export interface IExerciseImage {
  id?: number;
  uuid?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  main?: boolean | null;
  exercises?: IExercise[] | null;
}

export const defaultValue: Readonly<IExerciseImage> = {
  main: false,
};
