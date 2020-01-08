import Vue from 'vue';
import Router from 'vue-router';
import Home from './views/Home.vue';
import Login from './views/Login.vue';
import Register from './views/Register.vue';
import Team from './views/Team.vue';
import CreateBoard from './views/CreateBoard.vue';
import CreateTeam from './views/CreateTeam.vue';
import Board from './views/Board.vue';
import store from './store/index';
import Main from './views/Main.vue';


Vue.use(Router);

function isLoggedIn(to, from, next) {
  if (store.state.auth.user) {
    next();
  }else{
    next('/login');
  }
}
export const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'main',
      component: Main,
    },
    {
      path: '/:teamName-:teamId',
      name: 'team',
      component: Team,
      beforeEnter: isLoggedIn,
    },
    // {
    //   path: '/:teamName-:teamId/members',
    //   name: 'teamMembers',
    //   component: TeamMembers,
    //   beforeEnter: isLoggedIn,
    // },
    {
      path: '/t/:teamId/b/:boardId-:boardName',
      name: 'board',
      component: Board,
      beforeEnter: isLoggedIn,
    },
    {
      path: '/home',
      component: Home,
    },
    {
      path: '/board/create',
      component: CreateBoard,
      meta: { hideNavigation: true },
      beforeEnter: isLoggedIn,

    },
    // {
    //   path: '/team-info',
    //   component: Team,
    //   beforeEnter: isLoggedIn,
    //   // meta: { hideNavigation: true },
    // },
    {
      path: '/team/create',
      component: CreateTeam,
      meta: { hideNavigation: true },
      beforeEnter: isLoggedIn,
    },
    {
      path: '/login',
      component: Login,
    },
    {
      path: '/register',
      component: Register,
    },
    {
      path: '/profile',
      name: 'profile',
      beforeEnter: isLoggedIn,
      // lazy-loaded
      component: () => import('./views/Profile.vue')
    },
    // {
    //   path: '/admin',
    //   name: 'admin',
    //   beforeEnter: isLoggedIn,
    //   // lazy-loaded
    //   component: () => import('./views/BoardAdmin.vue')
    // },
    // {
    //   path: '/mod',
    //   name: 'moderator',
    //   beforeEnter: isLoggedIn,
    //   // lazy-loaded
    //   component: () => import('./views/BoardModerator.vue')
    // },
    // {
    //   path: '/user',
    //   name: 'user',
    //   beforeEnter: isLoggedIn,
    //   // lazy-loaded
    //   component: () => import('./views/BoardUser.vue')
    // },
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