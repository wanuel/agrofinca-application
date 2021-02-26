import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './potrero-pastoreo.reducer';
import { IPotreroPastoreo } from 'app/shared/model/potrero-pastoreo.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPotreroPastoreoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PotreroPastoreoDetail = (props: IPotreroPastoreoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { potreroPastoreoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="agrofincaApp.potreroPastoreo.detail.title">PotreroPastoreo</Translate> [<b>{potreroPastoreoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="fechaIngreso">
              <Translate contentKey="agrofincaApp.potreroPastoreo.fechaIngreso">Fecha Ingreso</Translate>
            </span>
          </dt>
          <dd>
            {potreroPastoreoEntity.fechaIngreso ? (
              <TextFormat value={potreroPastoreoEntity.fechaIngreso} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="fechaSalida">
              <Translate contentKey="agrofincaApp.potreroPastoreo.fechaSalida">Fecha Salida</Translate>
            </span>
          </dt>
          <dd>
            {potreroPastoreoEntity.fechaSalida ? (
              <TextFormat value={potreroPastoreoEntity.fechaSalida} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="fechaLimpia">
              <Translate contentKey="agrofincaApp.potreroPastoreo.fechaLimpia">Fecha Limpia</Translate>
            </span>
          </dt>
          <dd>
            {potreroPastoreoEntity.fechaLimpia ? (
              <TextFormat value={potreroPastoreoEntity.fechaLimpia} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="diasDescanso">
              <Translate contentKey="agrofincaApp.potreroPastoreo.diasDescanso">Dias Descanso</Translate>
            </span>
          </dt>
          <dd>{potreroPastoreoEntity.diasDescanso}</dd>
          <dt>
            <span id="diasCarga">
              <Translate contentKey="agrofincaApp.potreroPastoreo.diasCarga">Dias Carga</Translate>
            </span>
          </dt>
          <dd>{potreroPastoreoEntity.diasCarga}</dd>
          <dt>
            <span id="limpia">
              <Translate contentKey="agrofincaApp.potreroPastoreo.limpia">Limpia</Translate>
            </span>
          </dt>
          <dd>{potreroPastoreoEntity.limpia}</dd>
          <dt>
            <Translate contentKey="agrofincaApp.potreroPastoreo.lote">Lote</Translate>
          </dt>
          <dd>{potreroPastoreoEntity.lote ? potreroPastoreoEntity.lote.nombre : ''}</dd>
          <dt>
            <Translate contentKey="agrofincaApp.potreroPastoreo.potrero">Potrero</Translate>
          </dt>
          <dd>{potreroPastoreoEntity.potrero ? potreroPastoreoEntity.potrero.nombre : ''}</dd>
        </dl>
        <Button tag={Link} to="/potrero-pastoreo" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/potrero-pastoreo/${potreroPastoreoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ potreroPastoreo }: IRootState) => ({
  potreroPastoreoEntity: potreroPastoreo.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PotreroPastoreoDetail);
