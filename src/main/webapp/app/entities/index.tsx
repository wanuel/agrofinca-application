import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Finca from './finca';
import Potrero from './potrero';
import Lote from './lote';
import PotreroPastoreo from './potrero-pastoreo';
import Animal from './animal';
import AnimalLote from './animal-lote';
import Parametros from './parametros';
import AnimalCostos from './animal-costos';
import AnimalEvento from './animal-evento';
import AnimalPeso from './animal-peso';
import AnimalImagen from './animal-imagen';
import AnimalSalud from './animal-salud';
import Evento from './evento';
import Persona from './persona';
import Sociedad from './sociedad';
import Socio from './socio';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}finca`} component={Finca} />
      <ErrorBoundaryRoute path={`${match.url}potrero`} component={Potrero} />
      <ErrorBoundaryRoute path={`${match.url}lote`} component={Lote} />
      <ErrorBoundaryRoute path={`${match.url}potrero-pastoreo`} component={PotreroPastoreo} />
      <ErrorBoundaryRoute path={`${match.url}animal`} component={Animal} />
      <ErrorBoundaryRoute path={`${match.url}animal-lote`} component={AnimalLote} />
      <ErrorBoundaryRoute path={`${match.url}parametros`} component={Parametros} />
      <ErrorBoundaryRoute path={`${match.url}animal-costos`} component={AnimalCostos} />
      <ErrorBoundaryRoute path={`${match.url}animal-evento`} component={AnimalEvento} />
      <ErrorBoundaryRoute path={`${match.url}animal-peso`} component={AnimalPeso} />
      <ErrorBoundaryRoute path={`${match.url}animal-imagen`} component={AnimalImagen} />
      <ErrorBoundaryRoute path={`${match.url}animal-salud`} component={AnimalSalud} />
      <ErrorBoundaryRoute path={`${match.url}evento`} component={Evento} />
      <ErrorBoundaryRoute path={`${match.url}persona`} component={Persona} />
      <ErrorBoundaryRoute path={`${match.url}sociedad`} component={Sociedad} />
      <ErrorBoundaryRoute path={`${match.url}socio`} component={Socio} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
