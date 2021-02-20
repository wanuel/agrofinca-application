import { Moment } from 'moment';
import { IPotreroPastoreo } from 'app/shared/model/potrero-pastoreo.model';
import { IAnimalLote } from 'app/shared/model/animal-lote.model';
import { IParametros } from 'app/shared/model/parametros.model';
import { ESTADOLOTE } from 'app/shared/model/enumerations/estadolote.model';

export interface ILote {
  id?: number;
  nombre?: string;
  fecha?: string;
  estado?: ESTADOLOTE;
  pastoreos?: IPotreroPastoreo[];
  animales?: IAnimalLote[];
  tipo?: IParametros;
}

export const defaultValue: Readonly<ILote> = {};
