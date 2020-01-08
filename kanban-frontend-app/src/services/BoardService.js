import axios from 'axios';
import authHeader from './AuthHeader';

const API_URL = 'http://localhost:8000/api/';

class BoardService {

    createBoard(boardName, teamId) {
        return axios.post(API_URL + "board/create", { boardName: boardName, teamId: teamId }, { headers: authHeader() });
    }
    updateBoard(boardId, columns) {
        return axios.post(API_URL + "board/update", { boardId: boardId, columns: columns }, { headers: authHeader() });
    }
    getBoard(boardId) {
        return axios.get(API_URL + "board/get/" + boardId, { headers: authHeader() });
    }
    getBoards() {
        return axios.get(API_URL + "board/get", { headers: authHeader() });
    }
}
export default new BoardService();