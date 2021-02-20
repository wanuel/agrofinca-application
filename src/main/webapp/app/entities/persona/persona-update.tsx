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
import { getEntity, updateEntity, createEntity, reset } from './persona.reducer';
import { IPersona } from 'app/shared/model/persona.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPersonaUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PersonaUpdate = (props: IPersonaUpdateProps) => {
  const [socioId, setSocioId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { personaEntity, socios, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/persona' + props.location.search);
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
        ...personaEntity,
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
          <h2 id="agrofincaApp.persona.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.persona.home.createOrEditLabel">Create or edit a Persona</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : personaEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="persona-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="persona-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="tipoDocumentoLabel" for="persona-tipoDocumento">
                  <Translate contentKey="agrofincaApp.persona.tipoDocumento">Tipo Documento</Translate>
                </Label>
                <AvInput
                  id="persona-tipoDocumento"
                  type="select"
                  className="form-control"
                  name="tipoDocumento"
                  value={(!isNew && personaEntity.tipoDocumento) || 'CC'}
                >
                  <option value="CC">{translate('agrofincaApp.TIPODOCUMENTO.CC')}</option>
                  <option value="TI">{translate('agrofincaApp.TIPODOCUMENTO.TI')}</option>
                  <option value="CE">{translate('agrofincaApp.TIPODOCUMENTO.CE')}</option>
                  <option value="NIT">{translate('agrofincaApp.TIPODOCUMENTO.NIT')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="numDocuemntoLabel" for="persona-numDocuemnto">
                  <Translate contentKey="agrofincaApp.persona.numDocuemnto">Num Docuemnto</Translate>
                </Label>
                <AvField
                  id="persona-numDocuemnto"
                  type="string"
                  className="form-control"
                  name="numDocuemnto"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="primerNombreLabel" for="persona-primerNombre">
                  <Translate contentKey="agrofincaApp.persona.primerNombre">Primer Nombre</Translate>
                </Label>
                <AvField
                  id="persona-primerNombre"
                  type="text"
                  name="primerNombre"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="segundoNombreLabel" for="persona-segundoNombre">
                  <Translate contentKey="agrofincaApp.persona.segundoNombre">Segundo Nombre</Translate>
                </Label>
                <AvField id="persona-segundoNombre" type="text" name="segundoNombre" />
              </AvGroup>
              <AvGroup>
                <Label id="primerApellidoLabel" for="persona-primerApellido">
                  <Translate contentKey="agrofincaApp.persona.primerApellido">Primer Apellido</Translate>
                </Label>
                <AvField id="persona-primerApellido" type="text" name="primerApellido" />
              </AvGroup>
              <AvGroup>
                <Label id="segundoApellidoLabel" for="persona-segundoApellido">
                  <Translate contentKey="agrofincaApp.persona.segundoApellido">Segundo Apellido</Translate>
                </Label>
                <AvField id="persona-segundoApellido" type="text" name="segundoApellido" />
              </AvGroup>
              <AvGroup>
                <Label id="fechaNacimientoLabel" for="persona-fechaNacimiento">
                  <Translate contentKey="agrofincaApp.persona.fechaNacimiento">Fecha Nacimiento</Translate>
                </Label>
                <AvField id="persona-fechaNacimiento" type="date" className="form-control" name="fechaNacimiento" />
              </AvGroup>
              <AvGroup>
                <Label id="generoLabel" for="persona-genero">
                  <Translate contentKey="agrofincaApp.persona.genero">Genero</Translate>
                </Label>
                <AvInput
                  id="persona-genero"
                  type="select"
                  className="form-control"
                  name="genero"
                  value={(!isNew && personaEntity.genero) || 'MASCULINO'}
                >
                  <option value="MASCULINO">{translate('agrofincaApp.GENERO.MASCULINO')}</option>
                  <option value="FEMENINO">{translate('agrofincaApp.GENERO.FEMENINO')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="persona-socio">
                  <Translate contentKey="agrofincaApp.persona.socio">Socio</Translate>
                </Label>
                <AvInput id="persona-socio" type="select" className="form-control" name="socio.id">
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
              <Button tag={Link} id="cancel-save" to="/persona" replace color="info">
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
  personaEntity: storeState.persona.entity,
  loading: storeState.persona.loading,
  updating: storeState.persona.updating,
  updateSuccess: storeState.persona.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(PersonaUpdate);
