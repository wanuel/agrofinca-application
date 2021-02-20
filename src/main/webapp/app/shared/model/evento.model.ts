import { Moment } from 'moment';
import { IAnimalEvento } from 'app/shared/model/animal-evento.model';
import { IParametros } from 'app/shared/model/parametros.model';

export interface IEvento {
  id?: number;
  fecha?: string;
  observacion?: string;
  eventos?: IAnimalEvento[];
  evento?: IParametros;
}

export const defaultValue: Readonly<IEvento> = {};
