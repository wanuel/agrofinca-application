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
import { getEntity, updateEntity, createEntity, reset } from './animal-costos.reducer';
import { IAnimalCostos } from 'app/shared/model/animal-costos.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAnimalCostosUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnimalCostosUpdate = (props: IAnimalCostosUpdateProps) => {
  const [animalId, setAnimalId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { animalCostosEntity, animalEventos, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/animal-costos' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getAnimalEventos();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...animalCostosEntity,
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
          <h2 id="agrofincaApp.animalCostos.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.animalCostos.home.createOrEditLabel">Create or edit a AnimalCostos</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : animalCostosEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="animal-costos-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="animal-costos-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="fechaLabel" for="animal-costos-fecha">
                  <Translate contentKey="agrofincaApp.animalCostos.fecha">Fecha</Translate>
                </Label>
                <AvField
                  id="animal-costos-fecha"
                  type="date"
                  className="form-control"
                  name="fecha"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="valorLabel" for="animal-costos-valor">
                  <Translate contentKey="agrofincaApp.animalCostos.valor">Valor</Translate>
                </Label>
                <AvField
                  id="animal-costos-valor"
                  type="text"
                  name="valor"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="animal-costos-animal">
                  <Translate contentKey="agrofincaApp.animalCostos.animal">Animal</Translate>
                </Label>
                <AvInput id="animal-costos-animal" type="select" className="form-control" name="animal.id">
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
              <Button tag={Link} id="cancel-save" to="/animal-costos" replace color="info">
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
  animalCostosEntity: storeState.animalCostos.entity,
  loading: storeState.animalCostos.loading,
  updating: storeState.animalCostos.updating,
  updateSuccess: storeState.animalCostos.updateSuccess,
});

const mapDispatchToProps = {
  getAnimalEventos,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnimalCostosUpdate);
