import config from './config';

const hostName = () => {
  return window.location.origin;
};

let baseUrl = 'http://localhost:8080';
let redirectUrl = 'http://localhost:3000';

// if (process.env.NODE_ENV === 'development') {
//   baseUrl = config.DEV_ENDPOINT_HOST_NAME;
//   redirectUrl = config.DEV_OIDC_REDIRECT_URL;
// } else {
//   baseUrl = config.OIDC_REDIRECT_URL || hostName();
//   redirectUrl = config.OIDC_REDIRECT_URL || hostName();
// }

baseUrl = `${baseUrl}`;

export default { baseUrl, redirectUrl };
