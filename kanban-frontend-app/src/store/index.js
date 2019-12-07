import Vue from 'vue';
import Vuex from 'vuex';

import { auth } from './auth.module';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    selectedTeam: null,
  },
  mutations: {
    setSelectedTeam(state, selectedTeam) {
      state.selectedTeam = selectedTeam;
    }
  },
  modules: {
    auth
  }
});