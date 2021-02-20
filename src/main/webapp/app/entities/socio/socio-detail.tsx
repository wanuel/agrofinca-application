import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './socio.reducer';
import { ISocio } from 'app/shared/model/socio.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISocioDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SocioDetail = (props: ISocioDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { socioEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="agrofincaApp.socio.detail.title">Socio</Translate> [<b>{socioEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="fechaIngreso">
              <Translate contentKey="agrofincaApp.socio.fechaIngreso">Fecha Ingreso</Translate>
            </span>
          </dt>
          <dd>
            {socioEntity.fechaIngreso ? <TextFormat value={socioEntity.fechaIngreso} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="participacion">
              <Translate contentKey="agrofincaApp.socio.participacion">Participacion</Translate>
            </span>
          </dt>
          <dd>{socioEntity.participacion}</dd>
        </dl>
        <Button tag={Link} to="/socio" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/socio/${socioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ socio }: IRootState) => ({
  socioEntity: socio.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SocioDetail);
