import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './animal-lote.reducer';
import { IAnimalLote } from 'app/shared/model/animal-lote.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAnimalLoteDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnimalLoteDetail = (props: IAnimalLoteDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { animalLoteEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="agrofincaApp.animalLote.detail.title">AnimalLote</Translate> [<b>{animalLoteEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="fechaIngreso">
              <Translate contentKey="agrofincaApp.animalLote.fechaIngreso">Fecha Ingreso</Translate>
            </span>
          </dt>
          <dd>
            {animalLoteEntity.fechaIngreso ? (
              <TextFormat value={animalLoteEntity.fechaIngreso} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="fechaSalida">
              <Translate contentKey="agrofincaApp.animalLote.fechaSalida">Fecha Salida</Translate>
            </span>
          </dt>
          <dd>
            {animalLoteEntity.fechaSalida ? (
              <TextFormat value={animalLoteEntity.fechaSalida} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="agrofincaApp.animalLote.animal">Animal</Translate>
          </dt>
          <dd>{animalLoteEntity.animal ? animalLoteEntity.animal.id : ''}</dd>
          <dt>
            <Translate contentKey="agrofincaApp.animalLote.lote">Lote</Translate>
          </dt>
          <dd>{animalLoteEntity.lote ? animalLoteEntity.lote.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/animal-lote" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/animal-lote/${animalLoteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ animalLote }: IRootState) => ({
  animalLoteEntity: animalLote.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnimalLoteDetail);
