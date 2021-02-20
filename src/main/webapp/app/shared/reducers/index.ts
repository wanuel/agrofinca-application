import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from './user-management';
// prettier-ignore
import finca, {
  FincaState
} from 'app/entities/finca/finca.reducer';
// prettier-ignore
import potrero, {
  PotreroState
} from 'app/entities/potrero/potrero.reducer';
// prettier-ignore
import lote, {
  LoteState
} from 'app/entities/lote/lote.reducer';
// prettier-ignore
import potreroPastoreo, {
  PotreroPastoreoState
} from 'app/entities/potrero-pastoreo/potrero-pastoreo.reducer';
// prettier-ignore
import animal, {
  AnimalState
} from 'app/entities/animal/animal.reducer';
// prettier-ignore
import animalLote, {
  AnimalLoteState
} from 'app/entities/animal-lote/animal-lote.reducer';
// prettier-ignore
import parametros, {
  ParametrosState
} from 'app/entities/parametros/parametros.reducer';
// prettier-ignore
import animalCostos, {
  AnimalCostosState
} from 'app/entities/animal-costos/animal-costos.reducer';
// prettier-ignore
import animalEvento, {
  AnimalEventoState
} from 'app/entities/animal-evento/animal-evento.reducer';
// prettier-ignore
import animalPeso, {
  AnimalPesoState
} from 'app/entities/animal-peso/animal-peso.reducer';
// prettier-ignore
import animalImagen, {
  AnimalImagenState
} from 'app/entities/animal-imagen/animal-imagen.reducer';
// prettier-ignore
import animalSalud, {
  AnimalSaludState
} from 'app/entities/animal-salud/animal-salud.reducer';
// prettier-ignore
import evento, {
  EventoState
} from 'app/entities/evento/evento.reducer';
// prettier-ignore
import persona, {
  PersonaState
} from 'app/entities/persona/persona.reducer';
// prettier-ignore
import sociedad, {
  SociedadState
} from 'app/entities/sociedad/sociedad.reducer';
// prettier-ignore
import socio, {
  SocioState
} from 'app/entities/socio/socio.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly finca: FincaState;
  readonly potrero: PotreroState;
  readonly lote: LoteState;
  readonly potreroPastoreo: PotreroPastoreoState;
  readonly animal: AnimalState;
  readonly animalLote: AnimalLoteState;
  readonly parametros: ParametrosState;
  readonly animalCostos: AnimalCostosState;
  readonly animalEvento: AnimalEventoState;
  readonly animalPeso: AnimalPesoState;
  readonly animalImagen: AnimalImagenState;
  readonly animalSalud: AnimalSaludState;
  readonly evento: EventoState;
  readonly persona: PersonaState;
  readonly sociedad: SociedadState;
  readonly socio: SocioState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  finca,
  potrero,
  lote,
  potreroPastoreo,
  animal,
  animalLote,
  parametros,
  animalCostos,
  animalEvento,
  animalPeso,
  animalImagen,
  animalSalud,
  evento,
  persona,
  sociedad,
  socio,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;
