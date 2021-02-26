import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IParametros } from 'app/shared/model/parametros.model';
import { getEntities as getParametros } from 'app/entities/parametros/parametros.reducer';
import { getEntity, updateEntity, createEntity, reset } from './evento.reducer';
import { IEvento } from 'app/shared/model/evento.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IEventoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EventoUpdate = (props: IEventoUpdateProps) => {
  const [eventoId, setEventoId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { eventoEntity, parametros, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/evento' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getParametros();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...eventoEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agrofincaApp.evento.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.evento.home.createOrEditLabel">Create or edit a Evento</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : eventoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="evento-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="evento-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="fechaLabel" for="evento-fecha">
                  <Translate contentKey="agrofincaApp.evento.fecha">Fecha</Translate>
                </Label>
                <AvField
                  id="evento-fecha"
                  type="date"
                  className="form-control"
                  name="fecha"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="observacionLabel" for="evento-observacion">
                  <Translate contentKey="agrofincaApp.evento.observacion">Observacion</Translate>
                </Label>
                <AvField id="evento-observacion" type="text" name="observacion" />
              </AvGroup>
              <AvGroup>
                <Label for="evento-evento">
                  <Translate contentKey="agrofincaApp.evento.evento">Evento</Translate>
                </Label>
                <AvInput id="evento-evento" type="select" className="form-control" name="evento.id">
                  <option value="" key="0" />
                  {parametros
                    ? parametros.map(otherEntity => (
                        <option value={otherEntity.nombre} key={otherEntity.id}>
                          {otherEntity.nombre}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/evento" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  parametros: storeState.parametros.entities,
  eventoEntity: storeState.evento.entity,
  loading: storeState.evento.loading,
  updating: storeState.evento.updating,
  updateSuccess: storeState.evento.updateSuccess,
});

const mapDispatchToProps = {
  getParametros,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EventoUpdate);
