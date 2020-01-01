import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8000/api/';

class UserService {

  createTeam(teamName) {
    return axios.post(API_URL + "userteam/create", { teamName: teamName }, { headers: authHeader() });
  }
  createBoard(boardName, teamId) {

    return axios.post(API_URL + "board/create", { boardName: boardName, teamId: teamId }, { headers: authHeader() });
  }
  updateBoard(boardId, columns) {
    // console.log("board id: " + boardId);
    // console.log("columns: " + JSON.stringify(columns));
    return axios.post(API_URL + "board/update", { boardId: boardId, columns: columns }, { headers: authHeader() });
  }
  createColumn(columnName, boardId) {
    console.log(columnName);
    console.log(boardId);
    return axios.post(API_URL + "column/create", { columnName: columnName, boardId: boardId }, { headers: authHeader() })
  }
  createTask(columnId, description) {
    console.log(columnId);
    console.log(description);
    return axios.post(API_URL + "task/create", { columnId: columnId, description: description }, { headers: authHeader() })
  }
  updateTask(data) {
    return axios.post(API_URL + "task/update", data, { headers: authHeader() })
  }

  getBoard(boardId) {
    return axios.get(API_URL + "board/get/" + boardId, { headers: authHeader() });
  }
  getTeam(teamId) {
    return axios.get(API_URL + "team/get/" + teamId, { headers: authHeader() });
  }

  getBoards() {
    return axios.get(API_URL + "board/get", { headers: authHeader() });
  }
  getTeams() {
    return axios.get(API_URL + "team/get", { headers: authHeader() });
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