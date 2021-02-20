import { IPotreroPastoreo } from 'app/shared/model/potrero-pastoreo.model';
import { IFinca } from 'app/shared/model/finca.model';

export interface IPotrero {
  id?: number;
  nombre?: string;
  descripcion?: string;
  pasto?: string;
  area?: number;
  pastoreos?: IPotreroPastoreo[];
  finca?: IFinca;
}

export const defaultValue: Readonly<IPotrero> = {};
