<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark">
      <!-- <a href="#" class="navbar-brand">Kanban</a> -->
      <div class="navbar-nav mr-auto">
        <li class="nav-item">
          <a href="/home" class="nav-link">
            <font-awesome-icon icon="home" />Home
          </a>
        </li>
        <li>
          <div class="dropdown" v-if="currentUser">
            <button
              class="btn btn-secondary dropdown-toggle"
              type="button"
              id="dropdownMenuButton"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="false"
            >Teams</button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
              <li v-for="team in teams" v-on:click="selectedvalue(board)" v-bind:key="team.id">
                <a class="dropdown-item" href="#">{{team.name}}</a>
              </li>
              <a class="dropdown-item" href="/create-team">Create Team</a>
              <!-- <li v-for="board in boards">{{ board }}</li> -->
              <!-- <a class="dropdown-item" href="#">Action</a> -->
            </div>
          </div>
        </li>
        <li>
          <!-- <div class="dropdown" v-if="currentUser">
            <button
              class="btn btn-secondary dropdown-toggle"
              type="button"
              id="dropdownMenuButton"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="false"
            >Boards</button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
              <li v-for="board in boards" v-on:click="selectedvalue(board)" v-bind:key="board.id">
                <a class="dropdown-item" href="#">{{board.name}}</a>
              </li>
              <a class="dropdown-item" href="/create-board">Create</a>
              <li v-for="board in boards">{{ board }}</li>
              <a class="dropdown-item" href="#">Action</a>
            </div>
          </div>-->
        </li>
        <li class="nav-item" v-if="showAdminBoard">
          <a href="/admin" class="nav-link">Admin Board</a>
        </li>

        <li class="nav-item" v-if="showAdminBoard">
          <!-- <a href="/boards" class="nav-button" v-if="currentUser">Boards</a> -->
        </li>
        <li class="nav-item" v-if="showModeratorBoard">
          <a href="/mod" class="nav-link">Moderator Board</a>
        </li>
        <li class="nav-item">
          <a href="/user" class="nav-link" v-if="currentUser">User</a>
        </li>
      </div>

      <div class="navbar-nav ml-auto" v-if="!currentUser">
        <li class="nav-item">
          <a href="/register" class="nav-link">
            <font-awesome-icon icon="user-plus" />Sign Up
          </a>
        </li>
        <li class="nav-item">
          <a href="/login" class="nav-link">
            <font-awesome-icon icon="sign-in-alt" />Login
          </a>
        </li>
      </div>

      <div class="navbar-nav ml-auto" v-if="currentUser">
        <li class="nav-item">
          <a href="/profile" class="nav-link">
            <font-awesome-icon icon="user" />
            {{currentUser.username}}
          </a>
        </li>
        <li class="nav-item">
          <a href class="nav-link" @click="logOut">
            <font-awesome-icon icon="sign-out-alt" />LogOut
          </a>
        </li>
      </div>
    </nav>

    <div class="container">
      <router-view />
    </div>
  </div>
</template>

<script>
import UserService from "./services/user.service";
export default {
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    showAdminBoard() {
      if (this.currentUser) {
        return this.currentUser.roles.includes("ROLE_ADMIN");
      }

      return false;
    },
    showModeratorBoard() {
      if (this.currentUser) {
        return this.currentUser.roles.includes("ROLE_MODERATOR");
      }

      return false;
    }
  },
  methods: {
    getData() {
      UserService.getTeams()
        .then(response => {
          this.teams = response.data;
          console.log("team retrieved");
        })
        .catch(e => {
          console.log("Error", e);
        });
    },
    logOut() {
      this.$store.dispatch("auth/logout");
      this.$router.push("/login");
    }
  },
  data() {
    return {
      boards: ["board1", "board2", "board3"],
      teams: [],
      selectedTeam: ""
    };
  },
  created() {
    if (this.currentUser) {
      this.getData();
    }
  },
  watch: {
    // call again the method if the route changes
    $route: "getData"
  }
};
</script>