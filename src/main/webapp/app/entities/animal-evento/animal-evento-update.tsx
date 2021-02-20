import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAnimal } from 'app/shared/model/animal.model';
import { getEntities as getAnimals } from 'app/entities/animal/animal.reducer';
import { IEvento } from 'app/shared/model/evento.model';
import { getEntities as getEventos } from 'app/entities/evento/evento.reducer';
import { getEntity, updateEntity, createEntity, reset } from './animal-evento.reducer';
import { IAnimalEvento } from 'app/shared/model/animal-evento.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAnimalEventoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnimalEventoUpdate = (props: IAnimalEventoUpdateProps) => {
  const [animalId, setAnimalId] = useState('0');
  const [eventoId, setEventoId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { animalEventoEntity, animals, eventos, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/animal-evento' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getAnimals();
    props.getEventos();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...animalEventoEntity,
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
          <h2 id="agrofincaApp.animalEvento.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.animalEvento.home.createOrEditLabel">Create or edit a AnimalEvento</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : animalEventoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="animal-evento-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="animal-evento-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label for="animal-evento-animal">
                  <Translate contentKey="agrofincaApp.animalEvento.animal">Animal</Translate>
                </Label>
                <AvInput id="animal-evento-animal" type="select" className="form-control" name="animal.id">
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
                <Label for="animal-evento-evento">
                  <Translate contentKey="agrofincaApp.animalEvento.evento">Evento</Translate>
                </Label>
                <AvInput id="animal-evento-evento" type="select" className="form-control" name="evento.id">
                  <option value="" key="0" />
                  {eventos
                    ? eventos.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/animal-evento" replace color="info">
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
  eventos: storeState.evento.entities,
  animalEventoEntity: storeState.animalEvento.entity,
  loading: storeState.animalEvento.loading,
  updating: storeState.animalEvento.updating,
  updateSuccess: storeState.animalEvento.updateSuccess,
});

const mapDispatchToProps = {
  getAnimals,
  getEventos,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnimalEventoUpdate);
