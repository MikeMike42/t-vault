import api from '../services';

const getAuth = () => api.get('/auth/tvault/renew');

export default {
  getAuth,
};
