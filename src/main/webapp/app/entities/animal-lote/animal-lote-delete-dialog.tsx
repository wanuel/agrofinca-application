import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IAnimalLote } from 'app/shared/model/animal-lote.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './animal-lote.reducer';

export interface IAnimalLoteDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnimalLoteDeleteDialog = (props: IAnimalLoteDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/animal-lote' + props.location.search);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.animalLoteEntity.id);
  };

  const { animalLoteEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="agrofincaApp.animalLote.delete.question">
        <Translate contentKey="agrofincaApp.animalLote.delete.question" interpolate={{ id: animalLoteEntity.id }}>
          Are you sure you want to delete this AnimalLote?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-animalLote" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ animalLote }: IRootState) => ({
  animalLoteEntity: animalLote.entity,
  updateSuccess: animalLote.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnimalLoteDeleteDialog);
