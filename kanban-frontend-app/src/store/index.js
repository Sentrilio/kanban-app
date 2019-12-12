import Vue from 'vue';
import Vuex from 'vuex';

import { auth } from './auth.module';
import { user } from './user.data';
import { selection } from './selection.module';
import createPersistedState from 'vuex-persistedstate';
// import * as Cookies from 'js-cookie'

Vue.use(Vuex);

export default new Vuex.Store({

  strict: true,
  plugins: [
    createPersistedState()
    // {getState: (key) => Cookies.getJSON(key),
    // setState: (key, state) => Cookies.set(key, state, { expires: 3, secure: true })}
  ],

  modules: {
    auth,
    selection,
    user
  }
});