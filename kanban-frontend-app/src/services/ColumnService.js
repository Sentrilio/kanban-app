import axios from 'axios';
import authHeader from './AuthHeader';

const API_URL = 'http://localhost:8000/api/column';

class ColumnService {

    createColumn(data) {
        return axios.post(API_URL + "/create", data, { headers: authHeader() })
    }
    changeColumnPosition(data) {
        return axios.post(API_URL + "/change-position", data, { headers: authHeader() });
    }
    deleteColumn(columnId) {
        return axios.delete(API_URL + "/delete/" + columnId, { headers: authHeader() });
    }
    isLimitReached(column) {
        if (column.tasks.length >= column.wipLimit) {
            return true;
        } else {
            return false;
        }
    }
    updateColumnLimit(data){
        return axios.put(API_URL + "/update-limit", data, {headers: authHeader()});
    }

}
export default new ColumnService();