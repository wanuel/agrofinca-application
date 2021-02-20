import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAnimal } from 'app/shared/model/animal.model';
import { getEntities as getAnimals } from 'app/entities/animal/animal.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './animal-imagen.reducer';
import { IAnimalImagen } from 'app/shared/model/animal-imagen.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAnimalImagenUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnimalImagenUpdate = (props: IAnimalImagenUpdateProps) => {
  const [animalId, setAnimalId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { animalImagenEntity, animals, loading, updating } = props;

  const { imagen, imagenContentType } = animalImagenEntity;

  const handleClose = () => {
    props.history.push('/animal-imagen' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getAnimals();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...animalImagenEntity,
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
          <h2 id="agrofincaApp.animalImagen.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.animalImagen.home.createOrEditLabel">Create or edit a AnimalImagen</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : animalImagenEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="animal-imagen-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="animal-imagen-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="fechaLabel" for="animal-imagen-fecha">
                  <Translate contentKey="agrofincaApp.animalImagen.fecha">Fecha</Translate>
                </Label>
                <AvField
                  id="animal-imagen-fecha"
                  type="date"
                  className="form-control"
                  name="fecha"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="notaLabel" for="animal-imagen-nota">
                  <Translate contentKey="agrofincaApp.animalImagen.nota">Nota</Translate>
                </Label>
                <AvField id="animal-imagen-nota" type="text" name="nota" />
              </AvGroup>
              <AvGroup>
                <AvGroup>
                  <Label id="imagenLabel" for="imagen">
                    <Translate contentKey="agrofincaApp.animalImagen.imagen">Imagen</Translate>
                  </Label>
                  <br />
                  {imagen ? (
                    <div>
                      {imagenContentType ? (
                        <a onClick={openFile(imagenContentType, imagen)}>
                          <Translate contentKey="entity.action.open">Open</Translate>
                        </a>
                      ) : null}
                      <br />
                      <Row>
                        <Col md="11">
                          <span>
                            {imagenContentType}, {byteSize(imagen)}
                          </span>
                        </Col>
                        <Col md="1">
                          <Button color="danger" onClick={clearBlob('imagen')}>
                            <FontAwesomeIcon icon="times-circle" />
                          </Button>
                        </Col>
                      </Row>
                    </div>
                  ) : null}
                  <input id="file_imagen" type="file" onChange={onBlobChange(false, 'imagen')} />
                  <AvInput
                    type="hidden"
                    name="imagen"
                    value={imagen}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                    }}
                  />
                </AvGroup>
              </AvGroup>
              <AvGroup>
                <Label for="animal-imagen-animal">
                  <Translate contentKey="agrofincaApp.animalImagen.animal">Animal</Translate>
                </Label>
                <AvInput id="animal-imagen-animal" type="select" className="form-control" name="animal.id">
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
              <Button tag={Link} id="cancel-save" to="/animal-imagen" replace color="info">
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
  animalImagenEntity: storeState.animalImagen.entity,
  loading: storeState.animalImagen.loading,
  updating: storeState.animalImagen.updating,
  updateSuccess: storeState.animalImagen.updateSuccess,
});

const mapDispatchToProps = {
  getAnimals,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnimalImagenUpdate);
