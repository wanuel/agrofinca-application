import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './lote.reducer';
import { ILote } from 'app/shared/model/lote.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILoteDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LoteDetail = (props: ILoteDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { loteEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="agrofincaApp.lote.detail.title">Lote</Translate> [<b>{loteEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="nombre">
              <Translate contentKey="agrofincaApp.lote.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{loteEntity.nombre}</dd>
          <dt>
            <span id="fecha">
              <Translate contentKey="agrofincaApp.lote.fecha">Fecha</Translate>
            </span>
          </dt>
          <dd>{loteEntity.fecha ? <TextFormat value={loteEntity.fecha} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="estado">
              <Translate contentKey="agrofincaApp.lote.estado">Estado</Translate>
            </span>
          </dt>
          <dd>{loteEntity.estado}</dd>
          <dt>
            <Translate contentKey="agrofincaApp.lote.tipo">Tipo</Translate>
          </dt>
          <dd>{loteEntity.tipo ? loteEntity.tipo.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/lote" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lote/${loteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ lote }: IRootState) => ({
  loteEntity: lote.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LoteDetail);
