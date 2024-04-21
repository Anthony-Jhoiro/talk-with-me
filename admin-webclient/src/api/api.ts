export const SERVER_BASE_URL = import.meta.env.DEV
  ? 'http://localhost:8080'
  : '';
export const API_ENDPOINT = SERVER_BASE_URL + '/api';
