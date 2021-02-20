import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAnimalImagen, defaultValue } from 'app/shared/model/animal-imagen.model';

export const ACTION_TYPES = {
  SEARCH_ANIMALIMAGENS: 'animalImagen/SEARCH_ANIMALIMAGENS',
  FETCH_ANIMALIMAGEN_LIST: 'animalImagen/FETCH_ANIMALIMAGEN_LIST',
  FETCH_ANIMALIMAGEN: 'animalImagen/FETCH_ANIMALIMAGEN',
  CREATE_ANIMALIMAGEN: 'animalImagen/CREATE_ANIMALIMAGEN',
  UPDATE_ANIMALIMAGEN: 'animalImagen/UPDATE_ANIMALIMAGEN',
  DELETE_ANIMALIMAGEN: 'animalImagen/DELETE_ANIMALIMAGEN',
  SET_BLOB: 'animalImagen/SET_BLOB',
  RESET: 'animalImagen/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAnimalImagen>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AnimalImagenState = Readonly<typeof initialState>;

// Reducer

export default (state: AnimalImagenState = initialState, action): AnimalImagenState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ANIMALIMAGENS):
    case REQUEST(ACTION_TYPES.FETCH_ANIMALIMAGEN_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ANIMALIMAGEN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ANIMALIMAGEN):
    case REQUEST(ACTION_TYPES.UPDATE_ANIMALIMAGEN):
    case REQUEST(ACTION_TYPES.DELETE_ANIMALIMAGEN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ANIMALIMAGENS):
    case FAILURE(ACTION_TYPES.FETCH_ANIMALIMAGEN_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ANIMALIMAGEN):
    case FAILURE(ACTION_TYPES.CREATE_ANIMALIMAGEN):
    case FAILURE(ACTION_TYPES.UPDATE_ANIMALIMAGEN):
    case FAILURE(ACTION_TYPES.DELETE_ANIMALIMAGEN):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ANIMALIMAGENS):
    case SUCCESS(ACTION_TYPES.FETCH_ANIMALIMAGEN_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ANIMALIMAGEN):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ANIMALIMAGEN):
    case SUCCESS(ACTION_TYPES.UPDATE_ANIMALIMAGEN):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ANIMALIMAGEN):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType,
        },
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/animal-imagens';
const apiSearchUrl = 'api/_search/animal-imagens';

// Actions

export const getSearchEntities: ICrudSearchAction<IAnimalImagen> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ANIMALIMAGENS,
  payload: axios.get<IAnimalImagen>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IAnimalImagen> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ANIMALIMAGEN_LIST,
    payload: axios.get<IAnimalImagen>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAnimalImagen> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ANIMALIMAGEN,
    payload: axios.get<IAnimalImagen>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAnimalImagen> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ANIMALIMAGEN,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAnimalImagen> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ANIMALIMAGEN,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAnimalImagen> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ANIMALIMAGEN,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType,
  },
});

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
