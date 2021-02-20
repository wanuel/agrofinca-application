import { Moment } from 'moment';
import { IAnimalEvento } from 'app/shared/model/animal-evento.model';

export interface IAnimalCostos {
  id?: number;
  fecha?: string;
  valor?: number;
  animal?: IAnimalEvento;
}

export const defaultValue: Readonly<IAnimalCostos> = {};
