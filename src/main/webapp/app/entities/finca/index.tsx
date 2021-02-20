import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Finca from './finca';
import FincaDetail from './finca-detail';
import FincaUpdate from './finca-update';
import FincaDeleteDialog from './finca-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FincaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FincaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FincaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Finca} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FincaDeleteDialog} />
  </>
);

export default Routes;
