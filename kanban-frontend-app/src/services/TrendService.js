import axios from 'axios';
import authHeader from './AuthHeader';

const API_URL = 'http://localhost:8000/api/';

class TeamService {

    getTrends(data) {
        return axios.post(API_URL + "trend/get", data, { headers: authHeader() });
    }

}
export default new TeamService();