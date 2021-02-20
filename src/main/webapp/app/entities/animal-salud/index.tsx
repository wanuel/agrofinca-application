import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AnimalSalud from './animal-salud';
import AnimalSaludDetail from './animal-salud-detail';
import AnimalSaludUpdate from './animal-salud-update';
import AnimalSaludDeleteDialog from './animal-salud-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AnimalSaludUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AnimalSaludUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AnimalSaludDetail} />
      <ErrorBoundaryRoute path={match.url} component={AnimalSalud} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AnimalSaludDeleteDialog} />
  </>
);

export default Routes;
