import { Moment } from 'moment';
import { IAnimalEvento } from 'app/shared/model/animal-evento.model';
import { IParametros } from 'app/shared/model/parametros.model';
import { SINO } from 'app/shared/model/enumerations/sino.model';

export interface IAnimalSalud {
  id?: number;
  fecha?: string;
  nombre?: string;
  laboratorio?: string;
  dosis?: number;
  inyectado?: SINO;
  intramuscular?: SINO;
  subcutaneo?: SINO;
  observacion?: string;
  evento?: IAnimalEvento;
  medicamento?: IParametros;
}

export const defaultValue: Readonly<IAnimalSalud> = {};
