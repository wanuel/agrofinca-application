import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAnimalEvento } from 'app/shared/model/animal-evento.model';
import { getEntities as getAnimalEventos } from 'app/entities/animal-evento/animal-evento.reducer';
import { IParametros } from 'app/shared/model/parametros.model';
import { getEntities as getParametros } from 'app/entities/parametros/parametros.reducer';
import { getEntity, updateEntity, createEntity, reset } from './animal-salud.reducer';
import { IAnimalSalud } from 'app/shared/model/animal-salud.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAnimalSaludUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnimalSaludUpdate = (props: IAnimalSaludUpdateProps) => {
  const [eventoId, setEventoId] = useState('0');
  const [medicamentoId, setMedicamentoId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { animalSaludEntity, animalEventos, parametros, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/animal-salud' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getAnimalEventos();
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
        ...animalSaludEntity,
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
          <h2 id="agrofincaApp.animalSalud.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.animalSalud.home.createOrEditLabel">Create or edit a AnimalSalud</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : animalSaludEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="animal-salud-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="animal-salud-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="fechaLabel" for="animal-salud-fecha">
                  <Translate contentKey="agrofincaApp.animalSalud.fecha">Fecha</Translate>
                </Label>
                <AvField
                  id="animal-salud-fecha"
                  type="date"
                  className="form-control"
                  name="fecha"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nombreLabel" for="animal-salud-nombre">
                  <Translate contentKey="agrofincaApp.animalSalud.nombre">Nombre</Translate>
                </Label>
                <AvField
                  id="animal-salud-nombre"
                  type="text"
                  name="nombre"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="laboratorioLabel" for="animal-salud-laboratorio">
                  <Translate contentKey="agrofincaApp.animalSalud.laboratorio">Laboratorio</Translate>
                </Label>
                <AvField id="animal-salud-laboratorio" type="text" name="laboratorio" />
              </AvGroup>
              <AvGroup>
                <Label id="dosisLabel" for="animal-salud-dosis">
                  <Translate contentKey="agrofincaApp.animalSalud.dosis">Dosis</Translate>
                </Label>
                <AvField
                  id="animal-salud-dosis"
                  type="text"
                  name="dosis"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="inyectadoLabel" for="animal-salud-inyectado">
                  <Translate contentKey="agrofincaApp.animalSalud.inyectado">Inyectado</Translate>
                </Label>
                <AvInput
                  id="animal-salud-inyectado"
                  type="select"
                  className="form-control"
                  name="inyectado"
                  value={(!isNew && animalSaludEntity.inyectado) || 'SI'}
                >
                  <option value="SI">{translate('agrofincaApp.SINO.SI')}</option>
                  <option value="NO">{translate('agrofincaApp.SINO.NO')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="intramuscularLabel" for="animal-salud-intramuscular">
                  <Translate contentKey="agrofincaApp.animalSalud.intramuscular">Intramuscular</Translate>
                </Label>
                <AvInput
                  id="animal-salud-intramuscular"
                  type="select"
                  className="form-control"
                  name="intramuscular"
                  value={(!isNew && animalSaludEntity.intramuscular) || 'SI'}
                >
                  <option value="SI">{translate('agrofincaApp.SINO.SI')}</option>
                  <option value="NO">{translate('agrofincaApp.SINO.NO')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="subcutaneoLabel" for="animal-salud-subcutaneo">
                  <Translate contentKey="agrofincaApp.animalSalud.subcutaneo">Subcutaneo</Translate>
                </Label>
                <AvInput
                  id="animal-salud-subcutaneo"
                  type="select"
                  className="form-control"
                  name="subcutaneo"
                  value={(!isNew && animalSaludEntity.subcutaneo) || 'SI'}
                >
                  <option value="SI">{translate('agrofincaApp.SINO.SI')}</option>
                  <option value="NO">{translate('agrofincaApp.SINO.NO')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="observacionLabel" for="animal-salud-observacion">
                  <Translate contentKey="agrofincaApp.animalSalud.observacion">Observacion</Translate>
                </Label>
                <AvField id="animal-salud-observacion" type="text" name="observacion" />
              </AvGroup>
              <AvGroup>
                <Label for="animal-salud-evento">
                  <Translate contentKey="agrofincaApp.animalSalud.evento">Evento</Translate>
                </Label>
                <AvInput id="animal-salud-evento" type="select" className="form-control" name="evento.id">
                  <option value="" key="0" />
                  {animalEventos
                    ? animalEventos.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="animal-salud-medicamento">
                  <Translate contentKey="agrofincaApp.animalSalud.medicamento">Medicamento</Translate>
                </Label>
                <AvInput id="animal-salud-medicamento" type="select" className="form-control" name="medicamento.id">
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
              <Button tag={Link} id="cancel-save" to="/animal-salud" replace color="info">
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
  animalEventos: storeState.animalEvento.entities,
  parametros: storeState.parametros.entities,
  animalSaludEntity: storeState.animalSalud.entity,
  loading: storeState.animalSalud.loading,
  updating: storeState.animalSalud.updating,
  updateSuccess: storeState.animalSalud.updateSuccess,
});

const mapDispatchToProps = {
  getAnimalEventos,
  getParametros,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnimalSaludUpdate);
