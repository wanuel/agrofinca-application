import { Moment } from 'moment';
import { ILote } from 'app/shared/model/lote.model';
import { IPotrero } from 'app/shared/model/potrero.model';
import { SINO } from 'app/shared/model/enumerations/sino.model';

export interface IPotreroPastoreo {
  id?: number;
  fechaIngreso?: string;
  fechaSalida?: string;
  fechaLimpia?: string;
  diasDescanso?: number;
  diasCarga?: number;
  limpia?: SINO;
  lote?: ILote;
  potrero?: IPotrero;
}

export const defaultValue: Readonly<IPotreroPastoreo> = {};
