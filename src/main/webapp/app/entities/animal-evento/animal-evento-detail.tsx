import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './animal-evento.reducer';
import { IAnimalEvento } from 'app/shared/model/animal-evento.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAnimalEventoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnimalEventoDetail = (props: IAnimalEventoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { animalEventoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="agrofincaApp.animalEvento.detail.title">AnimalEvento</Translate> [<b>{animalEventoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <Translate contentKey="agrofincaApp.animalEvento.animal">Animal</Translate>
          </dt>
          <dd>{animalEventoEntity.animal ? animalEventoEntity.animal.id : ''}</dd>
          <dt>
            <Translate contentKey="agrofincaApp.animalEvento.evento">Evento</Translate>
          </dt>
          <dd>{animalEventoEntity.evento ? animalEventoEntity.evento.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/animal-evento" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/animal-evento/${animalEventoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ animalEvento }: IRootState) => ({
  animalEventoEntity: animalEvento.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnimalEventoDetail);
