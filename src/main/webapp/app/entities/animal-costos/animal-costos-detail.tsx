import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './animal-costos.reducer';
import { IAnimalCostos } from 'app/shared/model/animal-costos.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAnimalCostosDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnimalCostosDetail = (props: IAnimalCostosDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { animalCostosEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="agrofincaApp.animalCostos.detail.title">AnimalCostos</Translate> [<b>{animalCostosEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="fecha">
              <Translate contentKey="agrofincaApp.animalCostos.fecha">Fecha</Translate>
            </span>
          </dt>
          <dd>
            {animalCostosEntity.fecha ? <TextFormat value={animalCostosEntity.fecha} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="valor">
              <Translate contentKey="agrofincaApp.animalCostos.valor">Valor</Translate>
            </span>
          </dt>
          <dd>{animalCostosEntity.valor}</dd>
          <dt>
            <Translate contentKey="agrofincaApp.animalCostos.animal">Animal</Translate>
          </dt>
          <dd>{animalCostosEntity.animal ? animalCostosEntity.animal.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/animal-costos" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/animal-costos/${animalCostosEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ animalCostos }: IRootState) => ({
  animalCostosEntity: animalCostos.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnimalCostosDetail);
