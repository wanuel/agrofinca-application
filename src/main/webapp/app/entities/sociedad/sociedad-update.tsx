import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISocio } from 'app/shared/model/socio.model';
import { getEntities as getSocios } from 'app/entities/socio/socio.reducer';
import { getEntity, updateEntity, createEntity, reset } from './sociedad.reducer';
import { ISociedad } from 'app/shared/model/sociedad.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISociedadUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SociedadUpdate = (props: ISociedadUpdateProps) => {
  const [socioId, setSocioId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { sociedadEntity, socios, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/sociedad' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getSocios();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...sociedadEntity,
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
          <h2 id="agrofincaApp.sociedad.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.sociedad.home.createOrEditLabel">Create or edit a Sociedad</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : sociedadEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="sociedad-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="sociedad-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nombreLabel" for="sociedad-nombre">
                  <Translate contentKey="agrofincaApp.sociedad.nombre">Nombre</Translate>
                </Label>
                <AvField
                  id="sociedad-nombre"
                  type="text"
                  name="nombre"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="fechaCreacionLabel" for="sociedad-fechaCreacion">
                  <Translate contentKey="agrofincaApp.sociedad.fechaCreacion">Fecha Creacion</Translate>
                </Label>
                <AvField
                  id="sociedad-fechaCreacion"
                  type="date"
                  className="form-control"
                  name="fechaCreacion"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="estadoLabel" for="sociedad-estado">
                  <Translate contentKey="agrofincaApp.sociedad.estado">Estado</Translate>
                </Label>
                <AvInput
                  id="sociedad-estado"
                  type="select"
                  className="form-control"
                  name="estado"
                  value={(!isNew && sociedadEntity.estado) || 'ACTIVO'}
                >
                  <option value="ACTIVO">{translate('agrofincaApp.ESTADOSOCIEDAD.ACTIVO')}</option>
                  <option value="INACTIVO">{translate('agrofincaApp.ESTADOSOCIEDAD.INACTIVO')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="observacionesLabel" for="sociedad-observaciones">
                  <Translate contentKey="agrofincaApp.sociedad.observaciones">Observaciones</Translate>
                </Label>
                <AvField id="sociedad-observaciones" type="text" name="observaciones" />
              </AvGroup>
              <AvGroup>
                <Label for="sociedad-socio">
                  <Translate contentKey="agrofincaApp.sociedad.socio">Socio</Translate>
                </Label>
                <AvInput id="sociedad-socio" type="select" className="form-control" name="socio.id">
                  <option value="" key="0" />
                  {socios
                    ? socios.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/sociedad" replace color="info">
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
  socios: storeState.socio.entities,
  sociedadEntity: storeState.sociedad.entity,
  loading: storeState.sociedad.loading,
  updating: storeState.sociedad.updating,
  updateSuccess: storeState.sociedad.updateSuccess,
});

const mapDispatchToProps = {
  getSocios,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SociedadUpdate);
