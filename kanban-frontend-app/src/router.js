import Vue from 'vue';
import Router from 'vue-router';
import Home from './views/Home.vue';
import Login from './views/Login.vue';
import Register from './views/Register.vue';
import Team from './views/Team.vue';
import CreateBoard from './views/CreateBoard.vue';
import CreateTeam from './views/CreateTeam.vue';


Vue.use(Router);

export const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/home',
      component: Home
    },
    {
      path: '/board/create',
      component: CreateBoard,
      meta: { hideNavigation: true }

    },
    {
      path: '/team-info',
      component: Team,
      // meta: { hideNavigation: true }
    },
    {
      path: '/team/create',
      component: CreateTeam,
      meta: { hideNavigation: true }
    },
    {
      path: '/login',
      component: Login
    },
    {
      path: '/register',
      component: Register
    },
    {
      path: '/profile',
      name: 'profile',
      // lazy-loaded
      component: () => import('./views/Profile.vue')
    },
    {
      path: '/admin',
      name: 'admin',
      // lazy-loaded
      component: () => import('./views/BoardAdmin.vue')
    },
    {
      path: '/mod',
      name: 'moderator',
      // lazy-loaded
      component: () => import('./views/BoardModerator.vue')
    },
    {
      path: '/user',
      name: 'user',
      // lazy-loaded
      component: () => import('./views/BoardUser.vue')
    },
  ]
});


//checking authorization every request
// router.beforeEach((to, from, next) => {
//     const publicPages = ['/login', '/home'];
//     const authRequired = !publicPages.includes(to.path);
//     const loggedIn = localStorage.getItem('user');

//     // try to access a restricted page + not logged in
//     if (authRequired && !loggedIn) {
//       return next('/login');
//     }

//     next();
//   });