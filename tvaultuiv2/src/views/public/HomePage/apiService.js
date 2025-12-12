import api from '../../../services';

const getAuth = () => api.get('/auth/tvault/renew');
const callRevoke = (url) => api.get(url);
const getUserName = () => api.get('/username');
const getOwnerDetails = (username) =>
  api.get(`/ldap/corpusers?CorpId=${username}`);
export default {
  getAuth,
  getUserName,
  callRevoke,
  getOwnerDetails,
};
