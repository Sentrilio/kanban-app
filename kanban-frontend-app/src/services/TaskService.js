import axios from 'axios';
import authHeader from './AuthHeader';

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
    deleteTask(taskId) {
        return axios.delete(API_URL + "task/delete/" + taskId, { headers: authHeader() })
    }
}
export default new TaskService();