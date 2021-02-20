import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Sociedad from './sociedad';
import SociedadDetail from './sociedad-detail';
import SociedadUpdate from './sociedad-update';
import SociedadDeleteDialog from './sociedad-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SociedadUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SociedadUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SociedadDetail} />
      <ErrorBoundaryRoute path={match.url} component={Sociedad} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SociedadDeleteDialog} />
  </>
);

export default Routes;
