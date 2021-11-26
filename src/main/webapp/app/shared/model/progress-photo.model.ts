import dayjs from 'dayjs';

export interface IProgressPhoto {
  id?: number;
  note?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  weightDate?: string | null;
}

export const defaultValue: Readonly<IProgressPhoto> = {};
