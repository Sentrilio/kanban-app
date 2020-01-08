<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark" v-if="!$route.meta.hideNavigation">
      <div class="navbar-nav mr-auto">
        <li class="nav-item">
          <!-- <a @click="homeClick" href="/home" class="nav-link"> -->
          <a @click="homeClick" class="nav-link btn">
            <font-awesome-icon icon="home" />
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
            >Boards</button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
              <div v-for="team in teams" :key="team.id">
                <div>
                  <button
                    v-on:click="switchToTeam(team)"
                    class="dropdown-item"
                    style="color: #000066; text-decoration: underline;"
                  >{{team.name}}</button>
                  <div v-for="board in team.boards" :key="board.id">
                    <div class="board-btn">
                      <button
                        v-on:click="switchToBoard(team,board)"
                        class="dropdown-item"
                      >{{board.name}}</button>
                    </div>
                  </div>
                </div>
              </div>
              <div>
                <a class="dropdown-item" href="#" @click="createBoard">Create board</a>
              </div>
            </div>
          </div>
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
    <div>
      <router-view />
    </div>
  </div>
</template>

<script>
import TeamService from "./services/TeamService";
export default {
  data() {
    return {
      teams: [],
    };
  },
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
    homeClick(){
      if(this.currentUser){
        this.$router.push({name:'main'})
      }else{
        this.$router.push({name:'home'})
      }
    },
    compare(a, b) {
      return a.name.localeCompare(b.name);
    },
    sortTeams() {
      this.teams.sort(this.compare);
    },
    sortBoards() {
      this.teams.forEach(team => {
        team.boards.sort(this.compare);
      });
    },
    getTeams() {
      TeamService.getTeams()
        .then(response => {
          this.teams = response.data;
          this.sortTeams();
          this.sortBoards();
          console.log("team retrieved");
        })
        .catch(e => {
          console.log("Error", e);
        });
    },
    getData() {
      if (this.$store.state.auth.user) {
        this.getTeams();
      }
    },

    getTeamByName(teamName) {
      return this.teams.find(team => (team.name = teamName));
    },

    logOut() {
      this.$store.dispatch("auth/logout");
      this.$router.push("/login");
    },
    createBoard() {
      this.$router.push({ path: "/board/create" });
    },
    createTeam() {
      this.$router.push({ path: "/team/create" });
    },
    switchToTeam(team) {
      if (team) {
        console.log("team switching");
        let teamId = team.id;
        let teamName = team.name.replace(/\s/g,'');//maybe it can be moved into seperate component
        this.$router.push({
          name: "team",
          params: { teamId, teamName }
        });
      }
    },
    switchToBoard(team, board) {
      if (team) {
        console.log("board switching");
        let boardId = board.id;
        let boardName = board.name;
        let teamId = team.id;
        this.$router.push({
          name: "board",
          params: { teamId, boardId, boardName }
        });
        this.$route.params.pathMatch;
      } else {
        console.log("team does not exists");
      }
    }
  },
  created() {
    this.getData();
  },
  watch: {
    $route: "getData"
  }
};
</script>
<style lang="css">
.board-btn:hover {
  -webkit-transition-duration: 0.4s; /* Safari */
  transition-duration: 0.4s;
}

.board-btn {
  background-color: #f5f5f5;
  color: black;
}
.btn {
  margin-right: 10px !important;
}
</style>