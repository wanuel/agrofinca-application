import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAnimalPeso, defaultValue } from 'app/shared/model/animal-peso.model';

export const ACTION_TYPES = {
  SEARCH_ANIMALPESOS: 'animalPeso/SEARCH_ANIMALPESOS',
  FETCH_ANIMALPESO_LIST: 'animalPeso/FETCH_ANIMALPESO_LIST',
  FETCH_ANIMALPESO: 'animalPeso/FETCH_ANIMALPESO',
  CREATE_ANIMALPESO: 'animalPeso/CREATE_ANIMALPESO',
  UPDATE_ANIMALPESO: 'animalPeso/UPDATE_ANIMALPESO',
  DELETE_ANIMALPESO: 'animalPeso/DELETE_ANIMALPESO',
  RESET: 'animalPeso/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAnimalPeso>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AnimalPesoState = Readonly<typeof initialState>;

// Reducer

export default (state: AnimalPesoState = initialState, action): AnimalPesoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ANIMALPESOS):
    case REQUEST(ACTION_TYPES.FETCH_ANIMALPESO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ANIMALPESO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ANIMALPESO):
    case REQUEST(ACTION_TYPES.UPDATE_ANIMALPESO):
    case REQUEST(ACTION_TYPES.DELETE_ANIMALPESO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ANIMALPESOS):
    case FAILURE(ACTION_TYPES.FETCH_ANIMALPESO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ANIMALPESO):
    case FAILURE(ACTION_TYPES.CREATE_ANIMALPESO):
    case FAILURE(ACTION_TYPES.UPDATE_ANIMALPESO):
    case FAILURE(ACTION_TYPES.DELETE_ANIMALPESO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ANIMALPESOS):
    case SUCCESS(ACTION_TYPES.FETCH_ANIMALPESO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ANIMALPESO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ANIMALPESO):
    case SUCCESS(ACTION_TYPES.UPDATE_ANIMALPESO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ANIMALPESO):
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

const apiUrl = 'api/animal-pesos';
const apiSearchUrl = 'api/_search/animal-pesos';

// Actions

export const getSearchEntities: ICrudSearchAction<IAnimalPeso> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ANIMALPESOS,
  payload: axios.get<IAnimalPeso>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IAnimalPeso> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ANIMALPESO_LIST,
    payload: axios.get<IAnimalPeso>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAnimalPeso> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ANIMALPESO,
    payload: axios.get<IAnimalPeso>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAnimalPeso> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ANIMALPESO,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAnimalPeso> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ANIMALPESO,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAnimalPeso> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ANIMALPESO,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
