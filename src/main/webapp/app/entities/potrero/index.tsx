import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Potrero from './potrero';
import PotreroDetail from './potrero-detail';
import PotreroUpdate from './potrero-update';
import PotreroDeleteDialog from './potrero-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PotreroUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PotreroUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PotreroDetail} />
      <ErrorBoundaryRoute path={match.url} component={Potrero} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PotreroDeleteDialog} />
  </>
);

export default Routes;
