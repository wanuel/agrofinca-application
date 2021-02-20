import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Lote from './lote';
import LoteDetail from './lote-detail';
import LoteUpdate from './lote-update';
import LoteDeleteDialog from './lote-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LoteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LoteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LoteDetail} />
      <ErrorBoundaryRoute path={match.url} component={Lote} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LoteDeleteDialog} />
  </>
);

export default Routes;
