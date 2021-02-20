import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Socio from './socio';
import SocioDetail from './socio-detail';
import SocioUpdate from './socio-update';
import SocioDeleteDialog from './socio-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SocioUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SocioUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SocioDetail} />
      <ErrorBoundaryRoute path={match.url} component={Socio} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SocioDeleteDialog} />
  </>
);

export default Routes;
