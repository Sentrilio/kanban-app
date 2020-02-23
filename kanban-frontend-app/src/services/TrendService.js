import axios from 'axios';
import authHeader from './AuthHeader';

const API_URL = 'http://localhost:8000/api/';

class TrendService {

    getTrends(boardId) {
        return axios.get(API_URL + "trend/get/"+boardId, { headers: authHeader() });
    }

}
export default new TrendService();