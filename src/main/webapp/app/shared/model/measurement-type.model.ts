import { IBodyMeasurement } from 'app/shared/model/body-measurement.model';
import { IWeight } from 'app/shared/model/weight.model';

export interface IMeasurementType {
  id?: number;
  name?: string;
  description?: string | null;
  measurementOrder?: number | null;
  measurementUnit?: string | null;
  bodyMeasurements?: IBodyMeasurement[] | null;
  weights?: IWeight[] | null;
}

export const defaultValue: Readonly<IMeasurementType> = {};
