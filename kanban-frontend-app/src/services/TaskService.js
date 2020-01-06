import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8000/api/';

class TaskService {

    createTask(columnId, description) {
        console.log(columnId);
        console.log(description);
        return axios.post(API_URL + "task/create", { columnId: columnId, description: description }, { headers: authHeader() })
    }

    updateTask(data) {
        return axios.post(API_URL + "task/update", data, { headers: authHeader() })
    }
}
export default new TaskService();