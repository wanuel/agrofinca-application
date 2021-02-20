import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AnimalImagen from './animal-imagen';
import AnimalImagenDetail from './animal-imagen-detail';
import AnimalImagenUpdate from './animal-imagen-update';
import AnimalImagenDeleteDialog from './animal-imagen-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AnimalImagenUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AnimalImagenUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AnimalImagenDetail} />
      <ErrorBoundaryRoute path={match.url} component={AnimalImagen} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AnimalImagenDeleteDialog} />
  </>
);

export default Routes;
