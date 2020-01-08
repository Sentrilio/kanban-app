import axios from 'axios';
import authHeader from './AuthHeader';

const API_URL = 'http://localhost:8000/api/';

class UserService {

  //other

  getPublicContent() {
    return axios.get(API_URL + 'test/all');
  }

  getUserBoard() {
    return axios.get(API_URL + 'test/user', { headers: authHeader() });
  }

  getModeratorBoard() {
    return axios.get(API_URL + 'test/mod', { headers: authHeader() });
  }

  getAdminBoard() {
    return axios.get(API_URL + 'test/admin', { headers: authHeader() });
  }
}

export default new UserService();