import axios from 'axios';
import authHeader from './AuthHeader';

const API_URL = 'http://localhost:8000/api/';

class TeamService {

    addUserToTeam(teamId, email) {
        return axios.post(API_URL + "team/add/user/", { teamId: teamId, email: email }, { headers: authHeader() })
    }
    createTeam(teamName) {
        return axios.post(API_URL + "userteam/create", { teamName: teamName }, { headers: authHeader() });
    }
    getTeam(teamId) {
        return axios.get(API_URL + "team/get/" + teamId, { headers: authHeader() });
    }

    getTeams() {
        return axios.get(API_URL + "team/get", { headers: authHeader() });
    }

    getTeamMembers(teamId) {
        return axios.get(API_URL + "team/members/get/" + teamId, { headers: authHeader() });
    }
}
export default new TeamService();