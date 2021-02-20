import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IPotreroPastoreo } from 'app/shared/model/potrero-pastoreo.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './potrero-pastoreo.reducer';

export interface IPotreroPastoreoDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PotreroPastoreoDeleteDialog = (props: IPotreroPastoreoDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/potrero-pastoreo' + props.location.search);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.potreroPastoreoEntity.id);
  };

  const { potreroPastoreoEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="agrofincaApp.potreroPastoreo.delete.question">
        <Translate contentKey="agrofincaApp.potreroPastoreo.delete.question" interpolate={{ id: potreroPastoreoEntity.id }}>
          Are you sure you want to delete this PotreroPastoreo?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-potreroPastoreo" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ potreroPastoreo }: IRootState) => ({
  potreroPastoreoEntity: potreroPastoreo.entity,
  updateSuccess: potreroPastoreo.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PotreroPastoreoDeleteDialog);
