import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './persona.reducer';
import { IPersona } from 'app/shared/model/persona.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPersonaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PersonaDetail = (props: IPersonaDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { personaEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="agrofincaApp.persona.detail.title">Persona</Translate> [<b>{personaEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="tipoDocumento">
              <Translate contentKey="agrofincaApp.persona.tipoDocumento">Tipo Documento</Translate>
            </span>
          </dt>
          <dd>{personaEntity.tipoDocumento}</dd>
          <dt>
            <span id="numDocuemnto">
              <Translate contentKey="agrofincaApp.persona.numDocuemnto">Num Docuemnto</Translate>
            </span>
          </dt>
          <dd>{personaEntity.numDocuemnto}</dd>
          <dt>
            <span id="primerNombre">
              <Translate contentKey="agrofincaApp.persona.primerNombre">Primer Nombre</Translate>
            </span>
          </dt>
          <dd>{personaEntity.primerNombre}</dd>
          <dt>
            <span id="segundoNombre">
              <Translate contentKey="agrofincaApp.persona.segundoNombre">Segundo Nombre</Translate>
            </span>
          </dt>
          <dd>{personaEntity.segundoNombre}</dd>
          <dt>
            <span id="primerApellido">
              <Translate contentKey="agrofincaApp.persona.primerApellido">Primer Apellido</Translate>
            </span>
          </dt>
          <dd>{personaEntity.primerApellido}</dd>
          <dt>
            <span id="segundoApellido">
              <Translate contentKey="agrofincaApp.persona.segundoApellido">Segundo Apellido</Translate>
            </span>
          </dt>
          <dd>{personaEntity.segundoApellido}</dd>
          <dt>
            <span id="fechaNacimiento">
              <Translate contentKey="agrofincaApp.persona.fechaNacimiento">Fecha Nacimiento</Translate>
            </span>
          </dt>
          <dd>
            {personaEntity.fechaNacimiento ? (
              <TextFormat value={personaEntity.fechaNacimiento} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="genero">
              <Translate contentKey="agrofincaApp.persona.genero">Genero</Translate>
            </span>
          </dt>
          <dd>{personaEntity.genero}</dd>
          <dt>
            <Translate contentKey="agrofincaApp.persona.socio">Socio</Translate>
          </dt>
          <dd>{personaEntity.socio ? personaEntity.socio.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/persona" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/persona/${personaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ persona }: IRootState) => ({
  personaEntity: persona.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PersonaDetail);
