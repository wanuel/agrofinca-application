import { Moment } from 'moment';
import { IAnimalEvento } from 'app/shared/model/animal-evento.model';

export interface IAnimalPeso {
  id?: number;
  fecha?: string;
  peso?: number;
  animal?: IAnimalEvento;
}

export const defaultValue: Readonly<IAnimalPeso> = {};
