import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAnimalSalud, defaultValue } from 'app/shared/model/animal-salud.model';

export const ACTION_TYPES = {
  SEARCH_ANIMALSALUDS: 'animalSalud/SEARCH_ANIMALSALUDS',
  FETCH_ANIMALSALUD_LIST: 'animalSalud/FETCH_ANIMALSALUD_LIST',
  FETCH_ANIMALSALUD: 'animalSalud/FETCH_ANIMALSALUD',
  CREATE_ANIMALSALUD: 'animalSalud/CREATE_ANIMALSALUD',
  UPDATE_ANIMALSALUD: 'animalSalud/UPDATE_ANIMALSALUD',
  DELETE_ANIMALSALUD: 'animalSalud/DELETE_ANIMALSALUD',
  RESET: 'animalSalud/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAnimalSalud>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AnimalSaludState = Readonly<typeof initialState>;

// Reducer

export default (state: AnimalSaludState = initialState, action): AnimalSaludState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ANIMALSALUDS):
    case REQUEST(ACTION_TYPES.FETCH_ANIMALSALUD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ANIMALSALUD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ANIMALSALUD):
    case REQUEST(ACTION_TYPES.UPDATE_ANIMALSALUD):
    case REQUEST(ACTION_TYPES.DELETE_ANIMALSALUD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ANIMALSALUDS):
    case FAILURE(ACTION_TYPES.FETCH_ANIMALSALUD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ANIMALSALUD):
    case FAILURE(ACTION_TYPES.CREATE_ANIMALSALUD):
    case FAILURE(ACTION_TYPES.UPDATE_ANIMALSALUD):
    case FAILURE(ACTION_TYPES.DELETE_ANIMALSALUD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ANIMALSALUDS):
    case SUCCESS(ACTION_TYPES.FETCH_ANIMALSALUD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ANIMALSALUD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ANIMALSALUD):
    case SUCCESS(ACTION_TYPES.UPDATE_ANIMALSALUD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ANIMALSALUD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/animal-saluds';
const apiSearchUrl = 'api/_search/animal-saluds';

// Actions

export const getSearchEntities: ICrudSearchAction<IAnimalSalud> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ANIMALSALUDS,
  payload: axios.get<IAnimalSalud>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IAnimalSalud> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ANIMALSALUD_LIST,
    payload: axios.get<IAnimalSalud>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAnimalSalud> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ANIMALSALUD,
    payload: axios.get<IAnimalSalud>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAnimalSalud> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ANIMALSALUD,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAnimalSalud> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ANIMALSALUD,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAnimalSalud> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ANIMALSALUD,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
