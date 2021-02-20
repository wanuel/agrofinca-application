import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './finca.reducer';
import { IFinca } from 'app/shared/model/finca.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFincaUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FincaUpdate = (props: IFincaUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { fincaEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/finca' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...fincaEntity,
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
          <h2 id="agrofincaApp.finca.home.createOrEditLabel">
            <Translate contentKey="agrofincaApp.finca.home.createOrEditLabel">Create or edit a Finca</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : fincaEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="finca-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="finca-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nombreLabel" for="finca-nombre">
                  <Translate contentKey="agrofincaApp.finca.nombre">Nombre</Translate>
                </Label>
                <AvField
                  id="finca-nombre"
                  type="text"
                  name="nombre"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="areaLabel" for="finca-area">
                  <Translate contentKey="agrofincaApp.finca.area">Area</Translate>
                </Label>
                <AvField id="finca-area" type="text" name="area" />
              </AvGroup>
              <AvGroup>
                <Label id="matriculaLabel" for="finca-matricula">
                  <Translate contentKey="agrofincaApp.finca.matricula">Matricula</Translate>
                </Label>
                <AvField id="finca-matricula" type="text" name="matricula" />
              </AvGroup>
              <AvGroup>
                <Label id="codigoCatastralLabel" for="finca-codigoCatastral">
                  <Translate contentKey="agrofincaApp.finca.codigoCatastral">Codigo Catastral</Translate>
                </Label>
                <AvField id="finca-codigoCatastral" type="text" name="codigoCatastral" />
              </AvGroup>
              <AvGroup>
                <Label id="municipioLabel" for="finca-municipio">
                  <Translate contentKey="agrofincaApp.finca.municipio">Municipio</Translate>
                </Label>
                <AvField id="finca-municipio" type="text" name="municipio" />
              </AvGroup>
              <AvGroup>
                <Label id="veredaLabel" for="finca-vereda">
                  <Translate contentKey="agrofincaApp.finca.vereda">Vereda</Translate>
                </Label>
                <AvField id="finca-vereda" type="text" name="vereda" />
              </AvGroup>
              <AvGroup>
                <Label id="obserrvacionesLabel" for="finca-obserrvaciones">
                  <Translate contentKey="agrofincaApp.finca.obserrvaciones">Obserrvaciones</Translate>
                </Label>
                <AvField id="finca-obserrvaciones" type="text" name="obserrvaciones" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/finca" replace color="info">
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
  fincaEntity: storeState.finca.entity,
  loading: storeState.finca.loading,
  updating: storeState.finca.updating,
  updateSuccess: storeState.finca.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FincaUpdate);
