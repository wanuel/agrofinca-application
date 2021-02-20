import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILote } from 'app/shared/model/lote.model';
import { getEntities as getLotes } from 'app/entities/lote/lote.reducer';
import { IPotrero } from 'app/shared/model/potrero.model';
import { getEntities as getPotreros } from 'app/entities/potrero/potrero.reducer';
import { getEntity, updateEntity, createEntity, reset } from './potrero-pastoreo.reducer';
import { IPotreroPastoreo } from 'app/shared/model/potrero-pastoreo.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPotreroPastoreoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PotreroPastoreoUpdate = (props: IPotreroPastoreoUpdateProps) => {
  const [loteId, setLoteId] = useState('0');
  const [potreroId, setPotreroId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { potreroPastoreoEntity, lotes, potreros, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/potrero-pastoreo' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getLotes();
    props.getPotreros();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...potreroPastoreoEntity,
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
          <h2 id="agrofincaApp.potreroPastoreo.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.potreroPastoreo.home.createOrEditLabel">Create or edit a PotreroPastoreo</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : potreroPastoreoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="potrero-pastoreo-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="potrero-pastoreo-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="fechaIngresoLabel" for="potrero-pastoreo-fechaIngreso">
                  <Translate contentKey="agrofincaApp.potreroPastoreo.fechaIngreso">Fecha Ingreso</Translate>
                </Label>
                <AvField
                  id="potrero-pastoreo-fechaIngreso"
                  type="date"
                  className="form-control"
                  name="fechaIngreso"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="fechaSalidaLabel" for="potrero-pastoreo-fechaSalida">
                  <Translate contentKey="agrofincaApp.potreroPastoreo.fechaSalida">Fecha Salida</Translate>
                </Label>
                <AvField id="potrero-pastoreo-fechaSalida" type="date" className="form-control" name="fechaSalida" />
              </AvGroup>
              <AvGroup>
                <Label id="fechaLimpiaLabel" for="potrero-pastoreo-fechaLimpia">
                  <Translate contentKey="agrofincaApp.potreroPastoreo.fechaLimpia">Fecha Limpia</Translate>
                </Label>
                <AvField id="potrero-pastoreo-fechaLimpia" type="date" className="form-control" name="fechaLimpia" />
              </AvGroup>
              <AvGroup>
                <Label id="diasDescansoLabel" for="potrero-pastoreo-diasDescanso">
                  <Translate contentKey="agrofincaApp.potreroPastoreo.diasDescanso">Dias Descanso</Translate>
                </Label>
                <AvField id="potrero-pastoreo-diasDescanso" type="string" className="form-control" name="diasDescanso" />
              </AvGroup>
              <AvGroup>
                <Label id="diasCargaLabel" for="potrero-pastoreo-diasCarga">
                  <Translate contentKey="agrofincaApp.potreroPastoreo.diasCarga">Dias Carga</Translate>
                </Label>
                <AvField id="potrero-pastoreo-diasCarga" type="string" className="form-control" name="diasCarga" />
              </AvGroup>
              <AvGroup>
                <Label id="limpiaLabel" for="potrero-pastoreo-limpia">
                  <Translate contentKey="agrofincaApp.potreroPastoreo.limpia">Limpia</Translate>
                </Label>
                <AvInput
                  id="potrero-pastoreo-limpia"
                  type="select"
                  className="form-control"
                  name="limpia"
                  value={(!isNew && potreroPastoreoEntity.limpia) || 'SI'}
                >
                  <option value="SI">{translate('agrofincaApp.SINO.SI')}</option>
                  <option value="NO">{translate('agrofincaApp.SINO.NO')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="potrero-pastoreo-lote">
                  <Translate contentKey="agrofincaApp.potreroPastoreo.lote">Lote</Translate>
                </Label>
                <AvInput id="potrero-pastoreo-lote" type="select" className="form-control" name="lote.id">
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
              <AvGroup>
                <Label for="potrero-pastoreo-potrero">
                  <Translate contentKey="agrofincaApp.potreroPastoreo.potrero">Potrero</Translate>
                </Label>
                <AvInput id="potrero-pastoreo-potrero" type="select" className="form-control" name="potrero.id">
                  <option value="" key="0" />
                  {potreros
                    ? potreros.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/potrero-pastoreo" replace color="info">
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
  lotes: storeState.lote.entities,
  potreros: storeState.potrero.entities,
  potreroPastoreoEntity: storeState.potreroPastoreo.entity,
  loading: storeState.potreroPastoreo.loading,
  updating: storeState.potreroPastoreo.updating,
  updateSuccess: storeState.potreroPastoreo.updateSuccess,
});

const mapDispatchToProps = {
  getLotes,
  getPotreros,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PotreroPastoreoUpdate);
