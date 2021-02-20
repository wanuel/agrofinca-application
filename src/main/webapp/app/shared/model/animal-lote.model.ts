import { Moment } from 'moment';
import { IAnimal } from 'app/shared/model/animal.model';
import { ILote } from 'app/shared/model/lote.model';

export interface IAnimalLote {
  id?: number;
  fechaIngreso?: string;
  fechaSalida?: string;
  animal?: IAnimal;
  lote?: ILote;
}

export const defaultValue: Readonly<IAnimalLote> = {};
