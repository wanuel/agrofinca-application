import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAnimalEvento, defaultValue } from 'app/shared/model/animal-evento.model';

export const ACTION_TYPES = {
  SEARCH_ANIMALEVENTOS: 'animalEvento/SEARCH_ANIMALEVENTOS',
  FETCH_ANIMALEVENTO_LIST: 'animalEvento/FETCH_ANIMALEVENTO_LIST',
  FETCH_ANIMALEVENTO: 'animalEvento/FETCH_ANIMALEVENTO',
  CREATE_ANIMALEVENTO: 'animalEvento/CREATE_ANIMALEVENTO',
  UPDATE_ANIMALEVENTO: 'animalEvento/UPDATE_ANIMALEVENTO',
  DELETE_ANIMALEVENTO: 'animalEvento/DELETE_ANIMALEVENTO',
  RESET: 'animalEvento/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAnimalEvento>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AnimalEventoState = Readonly<typeof initialState>;

// Reducer

export default (state: AnimalEventoState = initialState, action): AnimalEventoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ANIMALEVENTOS):
    case REQUEST(ACTION_TYPES.FETCH_ANIMALEVENTO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ANIMALEVENTO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ANIMALEVENTO):
    case REQUEST(ACTION_TYPES.UPDATE_ANIMALEVENTO):
    case REQUEST(ACTION_TYPES.DELETE_ANIMALEVENTO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ANIMALEVENTOS):
    case FAILURE(ACTION_TYPES.FETCH_ANIMALEVENTO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ANIMALEVENTO):
    case FAILURE(ACTION_TYPES.CREATE_ANIMALEVENTO):
    case FAILURE(ACTION_TYPES.UPDATE_ANIMALEVENTO):
    case FAILURE(ACTION_TYPES.DELETE_ANIMALEVENTO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ANIMALEVENTOS):
    case SUCCESS(ACTION_TYPES.FETCH_ANIMALEVENTO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ANIMALEVENTO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ANIMALEVENTO):
    case SUCCESS(ACTION_TYPES.UPDATE_ANIMALEVENTO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ANIMALEVENTO):
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

const apiUrl = 'api/animal-eventos';
const apiSearchUrl = 'api/_search/animal-eventos';

// Actions

export const getSearchEntities: ICrudSearchAction<IAnimalEvento> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ANIMALEVENTOS,
  payload: axios.get<IAnimalEvento>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IAnimalEvento> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ANIMALEVENTO_LIST,
    payload: axios.get<IAnimalEvento>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAnimalEvento> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ANIMALEVENTO,
    payload: axios.get<IAnimalEvento>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAnimalEvento> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ANIMALEVENTO,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAnimalEvento> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ANIMALEVENTO,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAnimalEvento> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ANIMALEVENTO,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
