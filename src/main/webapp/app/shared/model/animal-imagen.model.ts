import { Moment } from 'moment';
import { IAnimal } from 'app/shared/model/animal.model';

export interface IAnimalImagen {
  id?: number;
  fecha?: string;
  nota?: string;
  imagenContentType?: string;
  imagen?: any;
  animal?: IAnimal;
}

export const defaultValue: Readonly<IAnimalImagen> = {};
