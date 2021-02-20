import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './animal.reducer';
import { IAnimal } from 'app/shared/model/animal.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAnimalDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnimalDetail = (props: IAnimalDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { animalEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="agrofincaApp.animal.detail.title">Animal</Translate> [<b>{animalEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="nombre">
              <Translate contentKey="agrofincaApp.animal.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{animalEntity.nombre}</dd>
          <dt>
            <span id="caracterizacion">
              <Translate contentKey="agrofincaApp.animal.caracterizacion">Caracterizacion</Translate>
            </span>
          </dt>
          <dd>{animalEntity.caracterizacion}</dd>
          <dt>
            <span id="hierro">
              <Translate contentKey="agrofincaApp.animal.hierro">Hierro</Translate>
            </span>
          </dt>
          <dd>{animalEntity.hierro}</dd>
          <dt>
            <span id="fechaNacimiento">
              <Translate contentKey="agrofincaApp.animal.fechaNacimiento">Fecha Nacimiento</Translate>
            </span>
          </dt>
          <dd>
            {animalEntity.fechaNacimiento ? (
              <TextFormat value={animalEntity.fechaNacimiento} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="fechaCompra">
              <Translate contentKey="agrofincaApp.animal.fechaCompra">Fecha Compra</Translate>
            </span>
          </dt>
          <dd>
            {animalEntity.fechaCompra ? <TextFormat value={animalEntity.fechaCompra} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="sexo">
              <Translate contentKey="agrofincaApp.animal.sexo">Sexo</Translate>
            </span>
          </dt>
          <dd>{animalEntity.sexo}</dd>
          <dt>
            <span id="castrado">
              <Translate contentKey="agrofincaApp.animal.castrado">Castrado</Translate>
            </span>
          </dt>
          <dd>{animalEntity.castrado}</dd>
          <dt>
            <span id="fechaCastracion">
              <Translate contentKey="agrofincaApp.animal.fechaCastracion">Fecha Castracion</Translate>
            </span>
          </dt>
          <dd>
            {animalEntity.fechaCastracion ? (
              <TextFormat value={animalEntity.fechaCastracion} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="estado">
              <Translate contentKey="agrofincaApp.animal.estado">Estado</Translate>
            </span>
          </dt>
          <dd>{animalEntity.estado}</dd>
          <dt>
            <Translate contentKey="agrofincaApp.animal.tipo">Tipo</Translate>
          </dt>
          <dd>{animalEntity.tipo ? animalEntity.tipo.id : ''}</dd>
          <dt>
            <Translate contentKey="agrofincaApp.animal.raza">Raza</Translate>
          </dt>
          <dd>{animalEntity.raza ? animalEntity.raza.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/animal" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/animal/${animalEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ animal }: IRootState) => ({
  animalEntity: animal.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnimalDetail);
