import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAnimalCostos, defaultValue } from 'app/shared/model/animal-costos.model';

export const ACTION_TYPES = {
  SEARCH_ANIMALCOSTOS: 'animalCostos/SEARCH_ANIMALCOSTOS',
  FETCH_ANIMALCOSTOS_LIST: 'animalCostos/FETCH_ANIMALCOSTOS_LIST',
  FETCH_ANIMALCOSTOS: 'animalCostos/FETCH_ANIMALCOSTOS',
  CREATE_ANIMALCOSTOS: 'animalCostos/CREATE_ANIMALCOSTOS',
  UPDATE_ANIMALCOSTOS: 'animalCostos/UPDATE_ANIMALCOSTOS',
  DELETE_ANIMALCOSTOS: 'animalCostos/DELETE_ANIMALCOSTOS',
  RESET: 'animalCostos/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAnimalCostos>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AnimalCostosState = Readonly<typeof initialState>;

// Reducer

export default (state: AnimalCostosState = initialState, action): AnimalCostosState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ANIMALCOSTOS):
    case REQUEST(ACTION_TYPES.FETCH_ANIMALCOSTOS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ANIMALCOSTOS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ANIMALCOSTOS):
    case REQUEST(ACTION_TYPES.UPDATE_ANIMALCOSTOS):
    case REQUEST(ACTION_TYPES.DELETE_ANIMALCOSTOS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ANIMALCOSTOS):
    case FAILURE(ACTION_TYPES.FETCH_ANIMALCOSTOS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ANIMALCOSTOS):
    case FAILURE(ACTION_TYPES.CREATE_ANIMALCOSTOS):
    case FAILURE(ACTION_TYPES.UPDATE_ANIMALCOSTOS):
    case FAILURE(ACTION_TYPES.DELETE_ANIMALCOSTOS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ANIMALCOSTOS):
    case SUCCESS(ACTION_TYPES.FETCH_ANIMALCOSTOS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ANIMALCOSTOS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ANIMALCOSTOS):
    case SUCCESS(ACTION_TYPES.UPDATE_ANIMALCOSTOS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ANIMALCOSTOS):
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

const apiUrl = 'api/animal-costos';
const apiSearchUrl = 'api/_search/animal-costos';

// Actions

export const getSearchEntities: ICrudSearchAction<IAnimalCostos> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ANIMALCOSTOS,
  payload: axios.get<IAnimalCostos>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IAnimalCostos> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ANIMALCOSTOS_LIST,
    payload: axios.get<IAnimalCostos>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAnimalCostos> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ANIMALCOSTOS,
    payload: axios.get<IAnimalCostos>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAnimalCostos> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ANIMALCOSTOS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAnimalCostos> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ANIMALCOSTOS,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAnimalCostos> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ANIMALCOSTOS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
