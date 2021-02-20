import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Evento from './evento';
import EventoDetail from './evento-detail';
import EventoUpdate from './evento-update';
import EventoDeleteDialog from './evento-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EventoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EventoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EventoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Evento} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EventoDeleteDialog} />
  </>
);

export default Routes;
