import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Parametros from './parametros';
import ParametrosDetail from './parametros-detail';
import ParametrosUpdate from './parametros-update';
import ParametrosDeleteDialog from './parametros-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ParametrosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ParametrosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ParametrosDetail} />
      <ErrorBoundaryRoute path={match.url} component={Parametros} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ParametrosDeleteDialog} />
  </>
);

export default Routes;
