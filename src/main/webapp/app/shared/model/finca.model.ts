import { IPotrero } from 'app/shared/model/potrero.model';

export interface IFinca {
  id?: number;
  nombre?: string;
  area?: number;
  matricula?: string;
  codigoCatastral?: string;
  municipio?: string;
  vereda?: string;
  obserrvaciones?: string;
  potreros?: IPotrero[];
}

export const defaultValue: Readonly<IFinca> = {};
