import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAnimalLote, defaultValue } from 'app/shared/model/animal-lote.model';

export const ACTION_TYPES = {
  SEARCH_ANIMALLOTES: 'animalLote/SEARCH_ANIMALLOTES',
  FETCH_ANIMALLOTE_LIST: 'animalLote/FETCH_ANIMALLOTE_LIST',
  FETCH_ANIMALLOTE: 'animalLote/FETCH_ANIMALLOTE',
  CREATE_ANIMALLOTE: 'animalLote/CREATE_ANIMALLOTE',
  UPDATE_ANIMALLOTE: 'animalLote/UPDATE_ANIMALLOTE',
  DELETE_ANIMALLOTE: 'animalLote/DELETE_ANIMALLOTE',
  RESET: 'animalLote/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAnimalLote>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AnimalLoteState = Readonly<typeof initialState>;

// Reducer

export default (state: AnimalLoteState = initialState, action): AnimalLoteState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ANIMALLOTES):
    case REQUEST(ACTION_TYPES.FETCH_ANIMALLOTE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ANIMALLOTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ANIMALLOTE):
    case REQUEST(ACTION_TYPES.UPDATE_ANIMALLOTE):
    case REQUEST(ACTION_TYPES.DELETE_ANIMALLOTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ANIMALLOTES):
    case FAILURE(ACTION_TYPES.FETCH_ANIMALLOTE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ANIMALLOTE):
    case FAILURE(ACTION_TYPES.CREATE_ANIMALLOTE):
    case FAILURE(ACTION_TYPES.UPDATE_ANIMALLOTE):
    case FAILURE(ACTION_TYPES.DELETE_ANIMALLOTE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ANIMALLOTES):
    case SUCCESS(ACTION_TYPES.FETCH_ANIMALLOTE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ANIMALLOTE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ANIMALLOTE):
    case SUCCESS(ACTION_TYPES.UPDATE_ANIMALLOTE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ANIMALLOTE):
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

const apiUrl = 'api/animal-lotes';
const apiSearchUrl = 'api/_search/animal-lotes';

// Actions

export const getSearchEntities: ICrudSearchAction<IAnimalLote> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ANIMALLOTES,
  payload: axios.get<IAnimalLote>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IAnimalLote> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ANIMALLOTE_LIST,
    payload: axios.get<IAnimalLote>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAnimalLote> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ANIMALLOTE,
    payload: axios.get<IAnimalLote>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAnimalLote> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ANIMALLOTE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAnimalLote> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ANIMALLOTE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAnimalLote> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ANIMALLOTE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
