import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/finca">
      <Translate contentKey="global.menu.entities.finca" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/potrero">
      <Translate contentKey="global.menu.entities.potrero" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/lote">
      <Translate contentKey="global.menu.entities.lote" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/potrero-pastoreo">
      <Translate contentKey="global.menu.entities.potreroPastoreo" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/animal">
      <Translate contentKey="global.menu.entities.animal" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/animal-lote">
      <Translate contentKey="global.menu.entities.animalLote" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/parametros">
      <Translate contentKey="global.menu.entities.parametros" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/animal-costos">
      <Translate contentKey="global.menu.entities.animalCostos" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/animal-evento">
      <Translate contentKey="global.menu.entities.animalEvento" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/animal-peso">
      <Translate contentKey="global.menu.entities.animalPeso" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/animal-imagen">
      <Translate contentKey="global.menu.entities.animalImagen" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/animal-salud">
      <Translate contentKey="global.menu.entities.animalSalud" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/evento">
      <Translate contentKey="global.menu.entities.evento" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/persona">
      <Translate contentKey="global.menu.entities.persona" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/sociedad">
      <Translate contentKey="global.menu.entities.sociedad" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/socio">
      <Translate contentKey="global.menu.entities.socio" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
