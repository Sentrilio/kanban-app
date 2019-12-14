export const selection = {
    namespaced: true,
    state: {
        selectedTeam: {},
        selectedBoard: {},
    },
    mutations: {
        setSelectedTeam(state, team) {
            console.log("team mutation");
            state.selectedTeam = team;
        },
        setSelectedBoard(state, board) {
            console.log("board mutation");
            state.selectedBoard = { name: board.name, id: board.boardId }
            // state.selectedBoardId = board.boardId;
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
    getters: {
        getTeam: state => {
            return state.selectedTeam;
        },
        getBoard: state => {
            return state.selectedBoard;
        }
    }
};