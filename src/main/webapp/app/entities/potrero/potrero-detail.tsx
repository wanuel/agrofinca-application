import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './potrero.reducer';
import { IPotrero } from 'app/shared/model/potrero.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPotreroDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PotreroDetail = (props: IPotreroDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { potreroEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="agrofincaApp.potrero.detail.title">Potrero</Translate> [<b>{potreroEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="nombre">
              <Translate contentKey="agrofincaApp.potrero.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{potreroEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="agrofincaApp.potrero.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{potreroEntity.descripcion}</dd>
          <dt>
            <span id="pasto">
              <Translate contentKey="agrofincaApp.potrero.pasto">Pasto</Translate>
            </span>
          </dt>
          <dd>{potreroEntity.pasto}</dd>
          <dt>
            <span id="area">
              <Translate contentKey="agrofincaApp.potrero.area">Area</Translate>
            </span>
          </dt>
          <dd>{potreroEntity.area}</dd>
          <dt>
            <Translate contentKey="agrofincaApp.potrero.finca">Finca</Translate>
          </dt>
          <dd>{potreroEntity.finca ? potreroEntity.finca.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/potrero" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/potrero/${potreroEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ potrero }: IRootState) => ({
  potreroEntity: potrero.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PotreroDetail);
