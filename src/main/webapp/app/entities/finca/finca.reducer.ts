import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFinca, defaultValue } from 'app/shared/model/finca.model';

export const ACTION_TYPES = {
  SEARCH_FINCAS: 'finca/SEARCH_FINCAS',
  FETCH_FINCA_LIST: 'finca/FETCH_FINCA_LIST',
  FETCH_FINCA: 'finca/FETCH_FINCA',
  CREATE_FINCA: 'finca/CREATE_FINCA',
  UPDATE_FINCA: 'finca/UPDATE_FINCA',
  DELETE_FINCA: 'finca/DELETE_FINCA',
  RESET: 'finca/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFinca>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type FincaState = Readonly<typeof initialState>;

// Reducer

export default (state: FincaState = initialState, action): FincaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_FINCAS):
    case REQUEST(ACTION_TYPES.FETCH_FINCA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FINCA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_FINCA):
    case REQUEST(ACTION_TYPES.UPDATE_FINCA):
    case REQUEST(ACTION_TYPES.DELETE_FINCA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_FINCAS):
    case FAILURE(ACTION_TYPES.FETCH_FINCA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FINCA):
    case FAILURE(ACTION_TYPES.CREATE_FINCA):
    case FAILURE(ACTION_TYPES.UPDATE_FINCA):
    case FAILURE(ACTION_TYPES.DELETE_FINCA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_FINCAS):
    case SUCCESS(ACTION_TYPES.FETCH_FINCA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_FINCA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_FINCA):
    case SUCCESS(ACTION_TYPES.UPDATE_FINCA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_FINCA):
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

const apiUrl = 'api/fincas';
const apiSearchUrl = 'api/_search/fincas';

// Actions

export const getSearchEntities: ICrudSearchAction<IFinca> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_FINCAS,
  payload: axios.get<IFinca>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IFinca> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FINCA_LIST,
    payload: axios.get<IFinca>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IFinca> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FINCA,
    payload: axios.get<IFinca>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IFinca> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FINCA,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFinca> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FINCA,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFinca> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FINCA,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
