import axios from 'axios';
import authHeader from './AuthHeader';

const API_URL = 'http://localhost:8000/api/';

class ColumnService {

    createColumn(data) {
        return axios.post(API_URL + "column/create", data, { headers: authHeader() })
    }
    changeColumnPosition(data) {
        return axios.post(API_URL + "column/change-position", data, { headers: authHeader() })
    }
    isLimitReached(column) {
        if (column.tasks.length >= column.wipLimit) {
            return true;
        } else {
            return false;
        }
    }
}
export default new ColumnService();