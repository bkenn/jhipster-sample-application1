import dayjs from 'dayjs';
import { IMeasurementType } from 'app/shared/model/measurement-type.model';

export interface IBodyMeasurement {
  id?: number;
  value?: number;
  bodyMeasurementDateTime?: string | null;
  measurementType?: IMeasurementType | null;
}

export const defaultValue: Readonly<IBodyMeasurement> = {};
