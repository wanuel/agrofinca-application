import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AnimalEvento from './animal-evento';
import AnimalEventoDetail from './animal-evento-detail';
import AnimalEventoUpdate from './animal-evento-update';
import AnimalEventoDeleteDialog from './animal-evento-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AnimalEventoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AnimalEventoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AnimalEventoDetail} />
      <ErrorBoundaryRoute path={match.url} component={AnimalEvento} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AnimalEventoDeleteDialog} />
  </>
);

export default Routes;
