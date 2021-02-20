import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntities as getParametros } from 'app/entities/parametros/parametros.reducer';
import { getEntity, updateEntity, createEntity, reset } from './parametros.reducer';
import { IParametros } from 'app/shared/model/parametros.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IParametrosUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ParametrosUpdate = (props: IParametrosUpdateProps) => {
  const [parametrosId, setParametrosId] = useState('0');
  const [agrupadorId, setAgrupadorId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { parametrosEntity, parametros, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/parametros' + props.location.search);
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
        ...parametrosEntity,
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
          <h2 id="agrofincaApp.parametros.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.parametros.home.createOrEditLabel">Create or edit a Parametros</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : parametrosEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="parametros-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="parametros-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nombreLabel" for="parametros-nombre">
                  <Translate contentKey="agrofincaApp.parametros.nombre">Nombre</Translate>
                </Label>
                <AvField
                  id="parametros-nombre"
                  type="text"
                  name="nombre"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="descripcionLabel" for="parametros-descripcion">
                  <Translate contentKey="agrofincaApp.parametros.descripcion">Descripcion</Translate>
                </Label>
                <AvField id="parametros-descripcion" type="text" name="descripcion" />
              </AvGroup>
              <AvGroup>
                <Label for="parametros-agrupador">
                  <Translate contentKey="agrofincaApp.parametros.agrupador">Agrupador</Translate>
                </Label>
                <AvInput id="parametros-agrupador" type="select" className="form-control" name="agrupador.id">
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
              <Button tag={Link} id="cancel-save" to="/parametros" replace color="info">
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
  parametrosEntity: storeState.parametros.entity,
  loading: storeState.parametros.loading,
  updating: storeState.parametros.updating,
  updateSuccess: storeState.parametros.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(ParametrosUpdate);
