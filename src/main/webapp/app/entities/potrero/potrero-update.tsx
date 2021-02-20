import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IFinca } from 'app/shared/model/finca.model';
import { getEntities as getFincas } from 'app/entities/finca/finca.reducer';
import { getEntity, updateEntity, createEntity, reset } from './potrero.reducer';
import { IPotrero } from 'app/shared/model/potrero.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPotreroUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PotreroUpdate = (props: IPotreroUpdateProps) => {
  const [fincaId, setFincaId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { potreroEntity, fincas, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/potrero' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getFincas();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...potreroEntity,
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
          <h2 id="agrofincaApp.potrero.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.potrero.home.createOrEditLabel">Create or edit a Potrero</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : potreroEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="potrero-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="potrero-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nombreLabel" for="potrero-nombre">
                  <Translate contentKey="agrofincaApp.potrero.nombre">Nombre</Translate>
                </Label>
                <AvField
                  id="potrero-nombre"
                  type="text"
                  name="nombre"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="descripcionLabel" for="potrero-descripcion">
                  <Translate contentKey="agrofincaApp.potrero.descripcion">Descripcion</Translate>
                </Label>
                <AvField id="potrero-descripcion" type="text" name="descripcion" />
              </AvGroup>
              <AvGroup>
                <Label id="pastoLabel" for="potrero-pasto">
                  <Translate contentKey="agrofincaApp.potrero.pasto">Pasto</Translate>
                </Label>
                <AvField id="potrero-pasto" type="text" name="pasto" />
              </AvGroup>
              <AvGroup>
                <Label id="areaLabel" for="potrero-area">
                  <Translate contentKey="agrofincaApp.potrero.area">Area</Translate>
                </Label>
                <AvField id="potrero-area" type="text" name="area" />
              </AvGroup>
              <AvGroup>
                <Label for="potrero-finca">
                  <Translate contentKey="agrofincaApp.potrero.finca">Finca</Translate>
                </Label>
                <AvInput id="potrero-finca" type="select" className="form-control" name="finca.id">
                  <option value="" key="0" />
                  {fincas
                    ? fincas.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/potrero" replace color="info">
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
  fincas: storeState.finca.entities,
  potreroEntity: storeState.potrero.entity,
  loading: storeState.potrero.loading,
  updating: storeState.potrero.updating,
  updateSuccess: storeState.potrero.updateSuccess,
});

const mapDispatchToProps = {
  getFincas,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PotreroUpdate);
