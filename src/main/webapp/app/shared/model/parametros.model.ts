import { IEvento } from 'app/shared/model/evento.model';
import { IAnimalSalud } from 'app/shared/model/animal-salud.model';
import { ILote } from 'app/shared/model/lote.model';
import { IAnimal } from 'app/shared/model/animal.model';

export interface IParametros {
  id?: number;
  nombre?: string;
  descripcion?: string;
  eventos?: IEvento[];
  medicamentos?: IAnimalSalud[];
  parametros?: IParametros[];
  tipoLotes?: ILote[];
  tipos?: IAnimal[];
  razas?: IAnimal[];
  agrupador?: IParametros;
}

export const defaultValue: Readonly<IParametros> = {};
