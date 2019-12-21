import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8000/api/';

class UserService {

  createTeam(teamName) {
    return axios.post(API_URL + "userteam/create", { teamName: teamName }, { headers: authHeader() });
  }
  createBoard(boardName, teamId) {
    return axios.post(API_URL + "board/create", { boardName: boardName, teamId: teamId }, { headers: authHeader() })
  }
  createList(listName, boardId) {
    return axios.post(API_URL + "list/create", { listName: listName, boardId: boardId }, { headers: authHeader() })
  }
  createTask(listId, teamId, description, content) {
    return axios.post(API_URL + "board/create", { listId: listId, teamId: teamId, description: description, content: content }, { headers: authHeader() })
  }

  getBoard(boardId) {
    return axios.get("http://localhost:8000/api/board/get/"+ boardId, { headers: authHeader() });
  }
  getTeam(teamId) {
    return axios.get("http://localhost:8000/api/team/get/"+ teamId, { headers: authHeader() });
  }

  getBoards() {
    return axios.get("http://localhost:8000/api/board/get", { headers: authHeader() });
  }
  getTeams() {
    return axios.get("http://localhost:8000/api/userteam/get", { headers: authHeader() });
  }
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