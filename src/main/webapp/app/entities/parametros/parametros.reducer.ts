import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IParametros, defaultValue } from 'app/shared/model/parametros.model';

export const ACTION_TYPES = {
  SEARCH_PARAMETROS: 'parametros/SEARCH_PARAMETROS',
  FETCH_PARAMETROS_LIST: 'parametros/FETCH_PARAMETROS_LIST',
  FETCH_PARAMETROS: 'parametros/FETCH_PARAMETROS',
  CREATE_PARAMETROS: 'parametros/CREATE_PARAMETROS',
  UPDATE_PARAMETROS: 'parametros/UPDATE_PARAMETROS',
  DELETE_PARAMETROS: 'parametros/DELETE_PARAMETROS',
  RESET: 'parametros/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IParametros>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ParametrosState = Readonly<typeof initialState>;

// Reducer

export default (state: ParametrosState = initialState, action): ParametrosState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PARAMETROS):
    case REQUEST(ACTION_TYPES.FETCH_PARAMETROS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PARAMETROS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PARAMETROS):
    case REQUEST(ACTION_TYPES.UPDATE_PARAMETROS):
    case REQUEST(ACTION_TYPES.DELETE_PARAMETROS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_PARAMETROS):
    case FAILURE(ACTION_TYPES.FETCH_PARAMETROS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PARAMETROS):
    case FAILURE(ACTION_TYPES.CREATE_PARAMETROS):
    case FAILURE(ACTION_TYPES.UPDATE_PARAMETROS):
    case FAILURE(ACTION_TYPES.DELETE_PARAMETROS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PARAMETROS):
    case SUCCESS(ACTION_TYPES.FETCH_PARAMETROS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARAMETROS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PARAMETROS):
    case SUCCESS(ACTION_TYPES.UPDATE_PARAMETROS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PARAMETROS):
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

const apiUrl = 'api/parametros';
const apiSearchUrl = 'api/_search/parametros';

// Actions

export const getSearchEntities: ICrudSearchAction<IParametros> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_PARAMETROS,
  payload: axios.get<IParametros>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IParametros> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PARAMETROS_LIST,
    payload: axios.get<IParametros>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IParametros> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PARAMETROS,
    payload: axios.get<IParametros>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IParametros> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PARAMETROS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IParametros> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PARAMETROS,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IParametros> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PARAMETROS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
