import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AnimalCostos from './animal-costos';
import AnimalCostosDetail from './animal-costos-detail';
import AnimalCostosUpdate from './animal-costos-update';
import AnimalCostosDeleteDialog from './animal-costos-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AnimalCostosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AnimalCostosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AnimalCostosDetail} />
      <ErrorBoundaryRoute path={match.url} component={AnimalCostos} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AnimalCostosDeleteDialog} />
  </>
);

export default Routes;
