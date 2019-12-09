import Vue from 'vue';
import Vuex from 'vuex';

import { auth } from './auth.module';
// import { selection } from './selection.module';
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);

export default new Vuex.Store({
  strict: true,
  plugins: [
    createPersistedState()
  ],

  modules: {
    auth,
    // selection
  }
});