import { Moment } from 'moment';
import { IPersona } from 'app/shared/model/persona.model';
import { ISociedad } from 'app/shared/model/sociedad.model';

export interface ISocio {
  id?: number;
  fechaIngreso?: string;
  participacion?: number;
  personas?: IPersona[];
  sociedades?: ISociedad[];
}

export const defaultValue: Readonly<ISocio> = {};
