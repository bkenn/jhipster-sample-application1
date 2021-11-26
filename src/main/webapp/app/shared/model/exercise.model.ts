import { IRepType } from 'app/shared/model/rep-type.model';
import { IExerciseCategory } from 'app/shared/model/exercise-category.model';
import { IExerciseImage } from 'app/shared/model/exercise-image.model';
import { IMuscle } from 'app/shared/model/muscle.model';
import { IWorkoutExercise } from 'app/shared/model/workout-exercise.model';

export interface IExercise {
  id?: number;
  name?: string;
  description?: string | null;
  repType?: IRepType | null;
  exerciseCategory?: IExerciseCategory | null;
  exerciseImages?: IExerciseImage[] | null;
  muscles?: IMuscle[] | null;
  workoutExercises?: IWorkoutExercise[] | null;
}

export const defaultValue: Readonly<IExercise> = {};
