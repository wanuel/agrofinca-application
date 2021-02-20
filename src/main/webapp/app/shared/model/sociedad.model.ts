import { Moment } from 'moment';
import { ISocio } from 'app/shared/model/socio.model';
import { ESTADOSOCIEDAD } from 'app/shared/model/enumerations/estadosociedad.model';

export interface ISociedad {
  id?: number;
  nombre?: string;
  fechaCreacion?: string;
  estado?: ESTADOSOCIEDAD;
  observaciones?: string;
  socio?: ISocio;
}

export const defaultValue: Readonly<ISociedad> = {};
