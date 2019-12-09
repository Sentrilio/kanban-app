export const selection = {
    namespaced: true,
    state: {
        selectedTeamId: null,
        selectedBoardId: null,
    },
    mutations: {
        setSelectedTeam(state, selectedTeam) {
            console.log("team mutation");
            state.selectedTeamId = selectedTeam.teamId;
        },
        setSelectedBoard(state, selectedBoard) {
            console.log("board mutation");
            state.selectedBoardId = selectedBoard.boardId;
        }
    },
    actions: {
        setSelectedTeam({ commit }, selectedTeam) {
            console.log("team action");
            commit('setSelectedTeam', selectedTeam);
        },
        setSelectedBoard({ commit }, selectedBoard) {
            console.log("board action");
            commit('setSelectedBoard', selectedBoard);
        }
    },
};