import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './sociedad.reducer';
import { ISociedad } from 'app/shared/model/sociedad.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISociedadDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SociedadDetail = (props: ISociedadDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { sociedadEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="agrofincaApp.sociedad.detail.title">Sociedad</Translate> [<b>{sociedadEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="nombre">
              <Translate contentKey="agrofincaApp.sociedad.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{sociedadEntity.nombre}</dd>
          <dt>
            <span id="fechaCreacion">
              <Translate contentKey="agrofincaApp.sociedad.fechaCreacion">Fecha Creacion</Translate>
            </span>
          </dt>
          <dd>
            {sociedadEntity.fechaCreacion ? (
              <TextFormat value={sociedadEntity.fechaCreacion} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="estado">
              <Translate contentKey="agrofincaApp.sociedad.estado">Estado</Translate>
            </span>
          </dt>
          <dd>{sociedadEntity.estado}</dd>
          <dt>
            <span id="observaciones">
              <Translate contentKey="agrofincaApp.sociedad.observaciones">Observaciones</Translate>
            </span>
          </dt>
          <dd>{sociedadEntity.observaciones}</dd>
          <dt>
            <Translate contentKey="agrofincaApp.sociedad.socio">Socio</Translate>
          </dt>
          <dd>{sociedadEntity.socio ? sociedadEntity.socio.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/sociedad" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sociedad/${sociedadEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ sociedad }: IRootState) => ({
  sociedadEntity: sociedad.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SociedadDetail);
