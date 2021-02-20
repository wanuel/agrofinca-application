import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PotreroPastoreo from './potrero-pastoreo';
import PotreroPastoreoDetail from './potrero-pastoreo-detail';
import PotreroPastoreoUpdate from './potrero-pastoreo-update';
import PotreroPastoreoDeleteDialog from './potrero-pastoreo-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PotreroPastoreoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PotreroPastoreoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PotreroPastoreoDetail} />
      <ErrorBoundaryRoute path={match.url} component={PotreroPastoreo} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PotreroPastoreoDeleteDialog} />
  </>
);

export default Routes;
