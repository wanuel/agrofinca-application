import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AnimalLote from './animal-lote';
import AnimalLoteDetail from './animal-lote-detail';
import AnimalLoteUpdate from './animal-lote-update';
import AnimalLoteDeleteDialog from './animal-lote-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AnimalLoteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AnimalLoteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AnimalLoteDetail} />
      <ErrorBoundaryRoute path={match.url} component={AnimalLote} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AnimalLoteDeleteDialog} />
  </>
);

export default Routes;
