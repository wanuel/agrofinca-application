import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Persona from './persona';
import PersonaDetail from './persona-detail';
import PersonaUpdate from './persona-update';
import PersonaDeleteDialog from './persona-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PersonaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PersonaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PersonaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Persona} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PersonaDeleteDialog} />
  </>
);

export default Routes;
