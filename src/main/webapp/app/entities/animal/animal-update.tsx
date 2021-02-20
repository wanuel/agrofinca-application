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
import { getEntity, updateEntity, createEntity, reset } from './animal.reducer';
import { IAnimal } from 'app/shared/model/animal.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAnimalUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnimalUpdate = (props: IAnimalUpdateProps) => {
  const [tipoId, setTipoId] = useState('0');
  const [razaId, setRazaId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { animalEntity, parametros, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/animal' + props.location.search);
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
        ...animalEntity,
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
          <h2 id="agrofincaApp.animal.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.animal.home.createOrEditLabel">Create or edit a Animal</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : animalEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="animal-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="animal-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nombreLabel" for="animal-nombre">
                  <Translate contentKey="agrofincaApp.animal.nombre">Nombre</Translate>
                </Label>
                <AvField id="animal-nombre" type="text" name="nombre" />
              </AvGroup>
              <AvGroup>
                <Label id="caracterizacionLabel" for="animal-caracterizacion">
                  <Translate contentKey="agrofincaApp.animal.caracterizacion">Caracterizacion</Translate>
                </Label>
                <AvField id="animal-caracterizacion" type="text" name="caracterizacion" />
              </AvGroup>
              <AvGroup>
                <Label id="hierroLabel" for="animal-hierro">
                  <Translate contentKey="agrofincaApp.animal.hierro">Hierro</Translate>
                </Label>
                <AvInput
                  id="animal-hierro"
                  type="select"
                  className="form-control"
                  name="hierro"
                  value={(!isNew && animalEntity.hierro) || 'SI'}
                >
                  <option value="SI">{translate('agrofincaApp.SINO.SI')}</option>
                  <option value="NO">{translate('agrofincaApp.SINO.NO')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="fechaNacimientoLabel" for="animal-fechaNacimiento">
                  <Translate contentKey="agrofincaApp.animal.fechaNacimiento">Fecha Nacimiento</Translate>
                </Label>
                <AvField id="animal-fechaNacimiento" type="date" className="form-control" name="fechaNacimiento" />
              </AvGroup>
              <AvGroup>
                <Label id="fechaCompraLabel" for="animal-fechaCompra">
                  <Translate contentKey="agrofincaApp.animal.fechaCompra">Fecha Compra</Translate>
                </Label>
                <AvField id="animal-fechaCompra" type="date" className="form-control" name="fechaCompra" />
              </AvGroup>
              <AvGroup>
                <Label id="sexoLabel" for="animal-sexo">
                  <Translate contentKey="agrofincaApp.animal.sexo">Sexo</Translate>
                </Label>
                <AvInput
                  id="animal-sexo"
                  type="select"
                  className="form-control"
                  name="sexo"
                  value={(!isNew && animalEntity.sexo) || 'MACHO'}
                >
                  <option value="MACHO">{translate('agrofincaApp.SEXO.MACHO')}</option>
                  <option value="HEMBRA">{translate('agrofincaApp.SEXO.HEMBRA')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="castradoLabel" for="animal-castrado">
                  <Translate contentKey="agrofincaApp.animal.castrado">Castrado</Translate>
                </Label>
                <AvInput
                  id="animal-castrado"
                  type="select"
                  className="form-control"
                  name="castrado"
                  value={(!isNew && animalEntity.castrado) || 'SI'}
                >
                  <option value="SI">{translate('agrofincaApp.SINO.SI')}</option>
                  <option value="NO">{translate('agrofincaApp.SINO.NO')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="fechaCastracionLabel" for="animal-fechaCastracion">
                  <Translate contentKey="agrofincaApp.animal.fechaCastracion">Fecha Castracion</Translate>
                </Label>
                <AvField id="animal-fechaCastracion" type="date" className="form-control" name="fechaCastracion" />
              </AvGroup>
              <AvGroup>
                <Label id="estadoLabel" for="animal-estado">
                  <Translate contentKey="agrofincaApp.animal.estado">Estado</Translate>
                </Label>
                <AvInput
                  id="animal-estado"
                  type="select"
                  className="form-control"
                  name="estado"
                  value={(!isNew && animalEntity.estado) || 'VIVO'}
                >
                  <option value="VIVO">{translate('agrofincaApp.ESTADOANIMAL.VIVO')}</option>
                  <option value="MUERTO">{translate('agrofincaApp.ESTADOANIMAL.MUERTO')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="animal-tipo">
                  <Translate contentKey="agrofincaApp.animal.tipo">Tipo</Translate>
                </Label>
                <AvInput id="animal-tipo" type="select" className="form-control" name="tipo.id">
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
              <AvGroup>
                <Label for="animal-raza">
                  <Translate contentKey="agrofincaApp.animal.raza">Raza</Translate>
                </Label>
                <AvInput id="animal-raza" type="select" className="form-control" name="raza.id">
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
              <Button tag={Link} id="cancel-save" to="/animal" replace color="info">
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
  animalEntity: storeState.animal.entity,
  loading: storeState.animal.loading,
  updating: storeState.animal.updating,
  updateSuccess: storeState.animal.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(AnimalUpdate);
