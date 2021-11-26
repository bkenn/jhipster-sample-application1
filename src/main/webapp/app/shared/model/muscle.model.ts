import { IExercise } from 'app/shared/model/exercise.model';

export interface IMuscle {
  id?: number;
  name?: string | null;
  description?: string | null;
  muscleOrder?: number | null;
  imageUrlMain?: string | null;
  imageUrlSecondary?: string | null;
  front?: boolean | null;
  exercises?: IExercise[] | null;
}

export const defaultValue: Readonly<IMuscle> = {
  front: false,
};
