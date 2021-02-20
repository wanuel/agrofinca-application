import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './finca.reducer';
import { IFinca } from 'app/shared/model/finca.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFincaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FincaDetail = (props: IFincaDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { fincaEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="agrofincaApp.finca.detail.title">Finca</Translate> [<b>{fincaEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="nombre">
              <Translate contentKey="agrofincaApp.finca.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{fincaEntity.nombre}</dd>
          <dt>
            <span id="area">
              <Translate contentKey="agrofincaApp.finca.area">Area</Translate>
            </span>
          </dt>
          <dd>{fincaEntity.area}</dd>
          <dt>
            <span id="matricula">
              <Translate contentKey="agrofincaApp.finca.matricula">Matricula</Translate>
            </span>
          </dt>
          <dd>{fincaEntity.matricula}</dd>
          <dt>
            <span id="codigoCatastral">
              <Translate contentKey="agrofincaApp.finca.codigoCatastral">Codigo Catastral</Translate>
            </span>
          </dt>
          <dd>{fincaEntity.codigoCatastral}</dd>
          <dt>
            <span id="municipio">
              <Translate contentKey="agrofincaApp.finca.municipio">Municipio</Translate>
            </span>
          </dt>
          <dd>{fincaEntity.municipio}</dd>
          <dt>
            <span id="vereda">
              <Translate contentKey="agrofincaApp.finca.vereda">Vereda</Translate>
            </span>
          </dt>
          <dd>{fincaEntity.vereda}</dd>
          <dt>
            <span id="obserrvaciones">
              <Translate contentKey="agrofincaApp.finca.obserrvaciones">Obserrvaciones</Translate>
            </span>
          </dt>
          <dd>{fincaEntity.obserrvaciones}</dd>
        </dl>
        <Button tag={Link} to="/finca" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/finca/${fincaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ finca }: IRootState) => ({
  fincaEntity: finca.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FincaDetail);
