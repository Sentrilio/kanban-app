export const user = {
    namespaced: true,
    state: {
        teams: null,
        // boards: null,
    },
    mutations: {
        setTeams(state, teams) {
            state.teams = teams;
        },
        // setBoards(state, boards) {
        //     state.boards = boards;
        // }
    },
    actions: {
        setTeams({ commit }, teams) {
            commit('setTeams', teams);
        },
        // setBoards({ commit }, boards) {
        //     commit('setBoards', boards);
        // }
    },
};