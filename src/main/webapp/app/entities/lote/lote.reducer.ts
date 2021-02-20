import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILote, defaultValue } from 'app/shared/model/lote.model';

export const ACTION_TYPES = {
  SEARCH_LOTES: 'lote/SEARCH_LOTES',
  FETCH_LOTE_LIST: 'lote/FETCH_LOTE_LIST',
  FETCH_LOTE: 'lote/FETCH_LOTE',
  CREATE_LOTE: 'lote/CREATE_LOTE',
  UPDATE_LOTE: 'lote/UPDATE_LOTE',
  DELETE_LOTE: 'lote/DELETE_LOTE',
  RESET: 'lote/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILote>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type LoteState = Readonly<typeof initialState>;

// Reducer

export default (state: LoteState = initialState, action): LoteState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_LOTES):
    case REQUEST(ACTION_TYPES.FETCH_LOTE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LOTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_LOTE):
    case REQUEST(ACTION_TYPES.UPDATE_LOTE):
    case REQUEST(ACTION_TYPES.DELETE_LOTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_LOTES):
    case FAILURE(ACTION_TYPES.FETCH_LOTE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LOTE):
    case FAILURE(ACTION_TYPES.CREATE_LOTE):
    case FAILURE(ACTION_TYPES.UPDATE_LOTE):
    case FAILURE(ACTION_TYPES.DELETE_LOTE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_LOTES):
    case SUCCESS(ACTION_TYPES.FETCH_LOTE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOTE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_LOTE):
    case SUCCESS(ACTION_TYPES.UPDATE_LOTE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_LOTE):
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

const apiUrl = 'api/lotes';
const apiSearchUrl = 'api/_search/lotes';

// Actions

export const getSearchEntities: ICrudSearchAction<ILote> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_LOTES,
  payload: axios.get<ILote>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ILote> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_LOTE_LIST,
    payload: axios.get<ILote>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ILote> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LOTE,
    payload: axios.get<ILote>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ILote> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LOTE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILote> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LOTE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILote> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LOTE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
