import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISociedad, defaultValue } from 'app/shared/model/sociedad.model';

export const ACTION_TYPES = {
  SEARCH_SOCIEDADS: 'sociedad/SEARCH_SOCIEDADS',
  FETCH_SOCIEDAD_LIST: 'sociedad/FETCH_SOCIEDAD_LIST',
  FETCH_SOCIEDAD: 'sociedad/FETCH_SOCIEDAD',
  CREATE_SOCIEDAD: 'sociedad/CREATE_SOCIEDAD',
  UPDATE_SOCIEDAD: 'sociedad/UPDATE_SOCIEDAD',
  DELETE_SOCIEDAD: 'sociedad/DELETE_SOCIEDAD',
  RESET: 'sociedad/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISociedad>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type SociedadState = Readonly<typeof initialState>;

// Reducer

export default (state: SociedadState = initialState, action): SociedadState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SOCIEDADS):
    case REQUEST(ACTION_TYPES.FETCH_SOCIEDAD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SOCIEDAD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SOCIEDAD):
    case REQUEST(ACTION_TYPES.UPDATE_SOCIEDAD):
    case REQUEST(ACTION_TYPES.DELETE_SOCIEDAD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_SOCIEDADS):
    case FAILURE(ACTION_TYPES.FETCH_SOCIEDAD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SOCIEDAD):
    case FAILURE(ACTION_TYPES.CREATE_SOCIEDAD):
    case FAILURE(ACTION_TYPES.UPDATE_SOCIEDAD):
    case FAILURE(ACTION_TYPES.DELETE_SOCIEDAD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SOCIEDADS):
    case SUCCESS(ACTION_TYPES.FETCH_SOCIEDAD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SOCIEDAD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SOCIEDAD):
    case SUCCESS(ACTION_TYPES.UPDATE_SOCIEDAD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SOCIEDAD):
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

const apiUrl = 'api/sociedads';
const apiSearchUrl = 'api/_search/sociedads';

// Actions

export const getSearchEntities: ICrudSearchAction<ISociedad> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SOCIEDADS,
  payload: axios.get<ISociedad>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ISociedad> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SOCIEDAD_LIST,
    payload: axios.get<ISociedad>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ISociedad> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SOCIEDAD,
    payload: axios.get<ISociedad>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISociedad> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SOCIEDAD,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISociedad> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SOCIEDAD,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISociedad> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SOCIEDAD,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
