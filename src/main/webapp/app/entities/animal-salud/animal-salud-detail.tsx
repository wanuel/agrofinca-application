import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './animal-salud.reducer';
import { IAnimalSalud } from 'app/shared/model/animal-salud.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAnimalSaludDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnimalSaludDetail = (props: IAnimalSaludDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { animalSaludEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="agrofincaApp.animalSalud.detail.title">AnimalSalud</Translate> [<b>{animalSaludEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="fecha">
              <Translate contentKey="agrofincaApp.animalSalud.fecha">Fecha</Translate>
            </span>
          </dt>
          <dd>
            {animalSaludEntity.fecha ? <TextFormat value={animalSaludEntity.fecha} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="agrofincaApp.animalSalud.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{animalSaludEntity.nombre}</dd>
          <dt>
            <span id="laboratorio">
              <Translate contentKey="agrofincaApp.animalSalud.laboratorio">Laboratorio</Translate>
            </span>
          </dt>
          <dd>{animalSaludEntity.laboratorio}</dd>
          <dt>
            <span id="dosis">
              <Translate contentKey="agrofincaApp.animalSalud.dosis">Dosis</Translate>
            </span>
          </dt>
          <dd>{animalSaludEntity.dosis}</dd>
          <dt>
            <span id="inyectado">
              <Translate contentKey="agrofincaApp.animalSalud.inyectado">Inyectado</Translate>
            </span>
          </dt>
          <dd>{animalSaludEntity.inyectado}</dd>
          <dt>
            <span id="intramuscular">
              <Translate contentKey="agrofincaApp.animalSalud.intramuscular">Intramuscular</Translate>
            </span>
          </dt>
          <dd>{animalSaludEntity.intramuscular}</dd>
          <dt>
            <span id="subcutaneo">
              <Translate contentKey="agrofincaApp.animalSalud.subcutaneo">Subcutaneo</Translate>
            </span>
          </dt>
          <dd>{animalSaludEntity.subcutaneo}</dd>
          <dt>
            <span id="observacion">
              <Translate contentKey="agrofincaApp.animalSalud.observacion">Observacion</Translate>
            </span>
          </dt>
          <dd>{animalSaludEntity.observacion}</dd>
          <dt>
            <Translate contentKey="agrofincaApp.animalSalud.evento">Evento</Translate>
          </dt>
          <dd>{animalSaludEntity.evento ? animalSaludEntity.evento.id : ''}</dd>
          <dt>
            <Translate contentKey="agrofincaApp.animalSalud.medicamento">Medicamento</Translate>
          </dt>
          <dd>{animalSaludEntity.medicamento ? animalSaludEntity.medicamento.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/animal-salud" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/animal-salud/${animalSaludEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ animalSalud }: IRootState) => ({
  animalSaludEntity: animalSalud.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnimalSaludDetail);
