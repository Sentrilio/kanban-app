import Vue from 'vue';
import App from './App.vue';
import { router } from './router';
import store from './store';
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import VeeValidate from 'vee-validate';
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faCircle,
  faDotCircle,
  faHome,
  faUser,
  faUserPlus,
  faSignInAlt,
  faSignOutAlt,
  faMinus,
  faPlus,
  faEllipsisV,
  faChartLine,
  faChartArea
} from '@fortawesome/free-solid-svg-icons';
import VueApexCharts from 'vue-apexcharts'

library.add(faHome, faUser, faUserPlus, faSignInAlt, faSignOutAlt, faMinus, faPlus, faEllipsisV, faChartLine,faChartArea,faDotCircle,faCircle);

Vue.use(VueApexCharts)
Vue.component('apexchart', VueApexCharts)

Vue.config.productionTip = false;

Vue.use(VeeValidate);
Vue.component('font-awesome-icon', FontAwesomeIcon);
import { BCollapse } from 'bootstrap-vue'
Vue.component('b-collapse', BCollapse)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');