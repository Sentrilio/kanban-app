export const selection = {
    namespaced: true,
    state: {
        selectedTeamId: null,
        selectedBoardId: null,
    },
    mutations: {
        setSelectedTeam(state, team) {
            console.log("team mutation");
            state.selectedTeamId = team.teamId;
        },
        setSelectedBoard(state, board) {
            console.log("board mutation");
            state.selectedBoardId = board.boardId;
        }
    },
    actions: {
        setSelectedTeam({ commit }, team) {
            console.log("team action");
            commit('setSelectedTeam', team);
        },
        setSelectedBoard({ commit }, board) {
            console.log("board action");
            commit('setSelectedBoard', board);
        }
    },
};