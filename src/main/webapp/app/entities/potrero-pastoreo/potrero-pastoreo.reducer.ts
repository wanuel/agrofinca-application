import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPotreroPastoreo, defaultValue } from 'app/shared/model/potrero-pastoreo.model';

export const ACTION_TYPES = {
  SEARCH_POTREROPASTOREOS: 'potreroPastoreo/SEARCH_POTREROPASTOREOS',
  FETCH_POTREROPASTOREO_LIST: 'potreroPastoreo/FETCH_POTREROPASTOREO_LIST',
  FETCH_POTREROPASTOREO: 'potreroPastoreo/FETCH_POTREROPASTOREO',
  CREATE_POTREROPASTOREO: 'potreroPastoreo/CREATE_POTREROPASTOREO',
  UPDATE_POTREROPASTOREO: 'potreroPastoreo/UPDATE_POTREROPASTOREO',
  DELETE_POTREROPASTOREO: 'potreroPastoreo/DELETE_POTREROPASTOREO',
  RESET: 'potreroPastoreo/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPotreroPastoreo>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type PotreroPastoreoState = Readonly<typeof initialState>;

// Reducer

export default (state: PotreroPastoreoState = initialState, action): PotreroPastoreoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_POTREROPASTOREOS):
    case REQUEST(ACTION_TYPES.FETCH_POTREROPASTOREO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_POTREROPASTOREO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_POTREROPASTOREO):
    case REQUEST(ACTION_TYPES.UPDATE_POTREROPASTOREO):
    case REQUEST(ACTION_TYPES.DELETE_POTREROPASTOREO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_POTREROPASTOREOS):
    case FAILURE(ACTION_TYPES.FETCH_POTREROPASTOREO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_POTREROPASTOREO):
    case FAILURE(ACTION_TYPES.CREATE_POTREROPASTOREO):
    case FAILURE(ACTION_TYPES.UPDATE_POTREROPASTOREO):
    case FAILURE(ACTION_TYPES.DELETE_POTREROPASTOREO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_POTREROPASTOREOS):
    case SUCCESS(ACTION_TYPES.FETCH_POTREROPASTOREO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_POTREROPASTOREO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_POTREROPASTOREO):
    case SUCCESS(ACTION_TYPES.UPDATE_POTREROPASTOREO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_POTREROPASTOREO):
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

const apiUrl = 'api/potrero-pastoreos';
const apiSearchUrl = 'api/_search/potrero-pastoreos';

// Actions

export const getSearchEntities: ICrudSearchAction<IPotreroPastoreo> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_POTREROPASTOREOS,
  payload: axios.get<IPotreroPastoreo>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IPotreroPastoreo> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_POTREROPASTOREO_LIST,
    payload: axios.get<IPotreroPastoreo>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IPotreroPastoreo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_POTREROPASTOREO,
    payload: axios.get<IPotreroPastoreo>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IPotreroPastoreo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_POTREROPASTOREO,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPotreroPastoreo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_POTREROPASTOREO,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPotreroPastoreo> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_POTREROPASTOREO,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
