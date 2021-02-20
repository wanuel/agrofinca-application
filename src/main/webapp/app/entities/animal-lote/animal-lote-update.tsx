import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAnimal } from 'app/shared/model/animal.model';
import { getEntities as getAnimals } from 'app/entities/animal/animal.reducer';
import { ILote } from 'app/shared/model/lote.model';
import { getEntities as getLotes } from 'app/entities/lote/lote.reducer';
import { getEntity, updateEntity, createEntity, reset } from './animal-lote.reducer';
import { IAnimalLote } from 'app/shared/model/animal-lote.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAnimalLoteUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnimalLoteUpdate = (props: IAnimalLoteUpdateProps) => {
  const [animalId, setAnimalId] = useState('0');
  const [loteId, setLoteId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { animalLoteEntity, animals, lotes, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/animal-lote' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getAnimals();
    props.getLotes();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...animalLoteEntity,
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
          <h2 id="agrofincaApp.animalLote.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.animalLote.home.createOrEditLabel">Create or edit a AnimalLote</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : animalLoteEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="animal-lote-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="animal-lote-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="fechaIngresoLabel" for="animal-lote-fechaIngreso">
                  <Translate contentKey="agrofincaApp.animalLote.fechaIngreso">Fecha Ingreso</Translate>
                </Label>
                <AvField
                  id="animal-lote-fechaIngreso"
                  type="date"
                  className="form-control"
                  name="fechaIngreso"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="fechaSalidaLabel" for="animal-lote-fechaSalida">
                  <Translate contentKey="agrofincaApp.animalLote.fechaSalida">Fecha Salida</Translate>
                </Label>
                <AvField id="animal-lote-fechaSalida" type="date" className="form-control" name="fechaSalida" />
              </AvGroup>
              <AvGroup>
                <Label for="animal-lote-animal">
                  <Translate contentKey="agrofincaApp.animalLote.animal">Animal</Translate>
                </Label>
                <AvInput id="animal-lote-animal" type="select" className="form-control" name="animal.id">
                  <option value="" key="0" />
                  {animals
                    ? animals.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="animal-lote-lote">
                  <Translate contentKey="agrofincaApp.animalLote.lote">Lote</Translate>
                </Label>
                <AvInput id="animal-lote-lote" type="select" className="form-control" name="lote.id">
                  <option value="" key="0" />
                  {lotes
                    ? lotes.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/animal-lote" replace color="info">
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
  animals: storeState.animal.entities,
  lotes: storeState.lote.entities,
  animalLoteEntity: storeState.animalLote.entity,
  loading: storeState.animalLote.loading,
  updating: storeState.animalLote.updating,
  updateSuccess: storeState.animalLote.updateSuccess,
});

const mapDispatchToProps = {
  getAnimals,
  getLotes,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnimalLoteUpdate);
