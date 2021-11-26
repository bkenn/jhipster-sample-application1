import React, { useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './workout-routine-exercise-set.reducer';

export const WorkoutRoutineExerciseSetDeleteDialog = (props: RouteComponentProps<{ id: string }>) => {
  const [loadModal, setLoadModal] = useState(false);
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    setLoadModal(true);
  }, []);

  const workoutRoutineExerciseSetEntity = useAppSelector(state => state.workoutRoutineExerciseSet.entity);
  const updateSuccess = useAppSelector(state => state.workoutRoutineExerciseSet.updateSuccess);

  const handleClose = () => {
    props.history.push('/workout-routine-exercise-set');
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(workoutRoutineExerciseSetEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="workoutRoutineExerciseSetDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="jhipsterSampleApplication1App.workoutRoutineExerciseSet.delete.question">
        <Translate
          contentKey="jhipsterSampleApplication1App.workoutRoutineExerciseSet.delete.question"
          interpolate={{ id: workoutRoutineExerciseSetEntity.id }}
        >
          Are you sure you want to delete this WorkoutRoutineExerciseSet?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button
          id="jhi-confirm-delete-workoutRoutineExerciseSet"
          data-cy="entityConfirmDeleteButton"
          color="danger"
          onClick={confirmDelete}
        >
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default WorkoutRoutineExerciseSetDeleteDialog;
