import { Moment } from 'moment';
import { IAnimalLote } from 'app/shared/model/animal-lote.model';
import { IAnimalImagen } from 'app/shared/model/animal-imagen.model';
import { IAnimalEvento } from 'app/shared/model/animal-evento.model';
import { IParametros } from 'app/shared/model/parametros.model';
import { SINO } from 'app/shared/model/enumerations/sino.model';
import { SEXO } from 'app/shared/model/enumerations/sexo.model';
import { ESTADOANIMAL } from 'app/shared/model/enumerations/estadoanimal.model';

export interface IAnimal {
  id?: number;
  nombre?: string;
  caracterizacion?: string;
  hierro?: SINO;
  fechaNacimiento?: string;
  fechaCompra?: string;
  sexo?: SEXO;
  castrado?: SINO;
  fechaCastracion?: string;
  estado?: ESTADOANIMAL;
  lotes?: IAnimalLote[];
  imagenes?: IAnimalImagen[];
  eventos?: IAnimalEvento[];
  tipo?: IParametros;
  raza?: IParametros;
}

export const defaultValue: Readonly<IAnimal> = {};
