import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/exercise">
      <Translate contentKey="global.menu.entities.exercise" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/body-measurement">
      <Translate contentKey="global.menu.entities.bodyMeasurement" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/exercise-category">
      <Translate contentKey="global.menu.entities.exerciseCategory" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/exercise-image">
      <Translate contentKey="global.menu.entities.exerciseImage" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/measurement-type">
      <Translate contentKey="global.menu.entities.measurementType" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/muscle">
      <Translate contentKey="global.menu.entities.muscle" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/progress-photo">
      <Translate contentKey="global.menu.entities.progressPhoto" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rep-type">
      <Translate contentKey="global.menu.entities.repType" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/weight">
      <Translate contentKey="global.menu.entities.weight" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/workout">
      <Translate contentKey="global.menu.entities.workout" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/workout-exercise">
      <Translate contentKey="global.menu.entities.workoutExercise" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/workout-exercise-set">
      <Translate contentKey="global.menu.entities.workoutExerciseSet" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/workout-routine">
      <Translate contentKey="global.menu.entities.workoutRoutine" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/workout-routine-exercise">
      <Translate contentKey="global.menu.entities.workoutRoutineExercise" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/workout-routine-exercise-set">
      <Translate contentKey="global.menu.entities.workoutRoutineExerciseSet" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/workout-routine-group">
      <Translate contentKey="global.menu.entities.workoutRoutineGroup" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
