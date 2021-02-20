import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AnimalPeso from './animal-peso';
import AnimalPesoDetail from './animal-peso-detail';
import AnimalPesoUpdate from './animal-peso-update';
import AnimalPesoDeleteDialog from './animal-peso-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AnimalPesoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AnimalPesoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AnimalPesoDetail} />
      <ErrorBoundaryRoute path={match.url} component={AnimalPeso} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AnimalPesoDeleteDialog} />
  </>
);

export default Routes;
