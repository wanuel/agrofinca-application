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
import { getEntity, updateEntity, createEntity, reset } from './lote.reducer';
import { ILote } from 'app/shared/model/lote.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILoteUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LoteUpdate = (props: ILoteUpdateProps) => {
  const [tipoId, setTipoId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { loteEntity, parametros, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/lote' + props.location.search);
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
        ...loteEntity,
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
          <h2 id="agrofincaApp.lote.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.lote.home.createOrEditLabel">Create or edit a Lote</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : loteEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="lote-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="lote-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nombreLabel" for="lote-nombre">
                  <Translate contentKey="agrofincaApp.lote.nombre">Nombre</Translate>
                </Label>
                <AvField
                  id="lote-nombre"
                  type="text"
                  name="nombre"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="fechaLabel" for="lote-fecha">
                  <Translate contentKey="agrofincaApp.lote.fecha">Fecha</Translate>
                </Label>
                <AvField
                  id="lote-fecha"
                  type="date"
                  className="form-control"
                  name="fecha"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="estadoLabel" for="lote-estado">
                  <Translate contentKey="agrofincaApp.lote.estado">Estado</Translate>
                </Label>
                <AvInput
                  id="lote-estado"
                  type="select"
                  className="form-control"
                  name="estado"
                  value={(!isNew && loteEntity.estado) || 'ACTIVO'}
                >
                  <option value="ACTIVO">{translate('agrofincaApp.ESTADOLOTE.ACTIVO')}</option>
                  <option value="INACTIVO">{translate('agrofincaApp.ESTADOLOTE.INACTIVO')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="lote-tipo">
                  <Translate contentKey="agrofincaApp.lote.tipo">Tipo</Translate>
                </Label>
                <AvInput id="lote-tipo" type="select" className="form-control" name="tipo.id">
                  <option value="" key="0" />
                  {parametros
                    ? parametros.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/lote" replace color="info">
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
  loteEntity: storeState.lote.entity,
  loading: storeState.lote.loading,
  updating: storeState.lote.updating,
  updateSuccess: storeState.lote.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(LoteUpdate);
