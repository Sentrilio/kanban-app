import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8000/api/test/';

class UserService {

  createTeam(teamName) {
    return axios.post("http://localhost:8000/user-team/create/"+teamName, { headers: authHeader() });
  }

  getBoards() {
    // return axios.get("http://localhost:8000/board/user-boards", { headers: authHeader() });
  }
  getTeams() {
    return axios.get("http://localhost:8000/user-team/get", { headers: authHeader() });
  }
  getPublicContent() {
    return axios.get(API_URL + 'all');
  }

  getUserBoard() {
    return axios.get(API_URL + 'user', { headers: authHeader() });
  }

  getModeratorBoard() {
    return axios.get(API_URL + 'mod', { headers: authHeader() });
  }

  getAdminBoard() {
    return axios.get(API_URL + 'admin', { headers: authHeader() });
  }
}

export default new UserService();