import { IAnimalSalud } from 'app/shared/model/animal-salud.model';
import { IAnimalPeso } from 'app/shared/model/animal-peso.model';
import { IAnimalCostos } from 'app/shared/model/animal-costos.model';
import { IAnimal } from 'app/shared/model/animal.model';
import { IEvento } from 'app/shared/model/evento.model';

export interface IAnimalEvento {
  id?: number;
  tratamientos?: IAnimalSalud[];
  pesos?: IAnimalPeso[];
  costos?: IAnimalCostos[];
  animal?: IAnimal;
  evento?: IEvento;
}

export const defaultValue: Readonly<IAnimalEvento> = {};
