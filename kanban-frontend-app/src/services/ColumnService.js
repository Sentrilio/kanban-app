import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8000/api/';

class ColumnService {

    createColumn(columnName, boardId) {
        console.log(columnName);
        console.log(boardId);
        return axios.post(API_URL + "column/create", { columnName: columnName, boardId: boardId }, { headers: authHeader() })
    }
}
export default new ColumnService();