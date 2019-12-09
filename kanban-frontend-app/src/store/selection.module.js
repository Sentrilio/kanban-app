// export const selection = {
//     namespaced: true,
//     state: {
//         selectedTeam: null,
//         selectedBoard: null,
//     },
//     mutations: {
//         setSelectedTeam(state, selectedTeam) {
//             console.log("team mutation");
//             state.selectedTeam = selectedTeam;
//         },
//         setSelectedBoard(state, selectedBoard) {
//             console.log("board mutation");
//             state.selectedBoard = selectedBoard;
//         }
//     },
//     actions: {
//         setSelectedTeam({ commit }, selectedTeam) {
//             console.log("team action");
//             commit('setSelectedTeam', selectedTeam);
//         },
//         setSelectedBoard({ commit }, selectedBoard) {
//             console.log("board action");
//             commit('setSelectedBoard', selectedBoard);
//         }
//     },
// };