import dayjs from 'dayjs';
import { IMeasurementType } from 'app/shared/model/measurement-type.model';

export interface IWeight {
  id?: number;
  value?: number | null;
  weightDateTime?: string | null;
  measurementType?: IMeasurementType | null;
}

export const defaultValue: Readonly<IWeight> = {};
