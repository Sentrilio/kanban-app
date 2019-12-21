<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark" v-if="!$route.meta.hideNavigation">
      <div class="navbar-nav mr-auto">
        <li class="nav-item">
          <a href="/home" class="nav-link">
            <font-awesome-icon icon="home" />
          </a>
        </li>
        <li>
          <div class="dropdown" v-if="currentUser && selectedBoard">
            <button
              class="btn btn-secondary dropdown-toggle"
              type="button"
              id="dropdownMenuButton"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="false"
            >Boards</button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
              <div v-for="(teamBoards, teamName) in boardTeams" :key="teamName">
                <h4
                  class="dropdown-header"
                  style="color: #000066; text-decoration: underline;"
                >{{teamName}}</h4>
                <div
                  v-for="board in teamBoards"
                  :key="board.boardId"
                  v-on:click="switchToBoard(teamName,board)"
                >
                  <a class="dropdown-item" href>{{board.name}}</a>
                </div>
              </div>
              <div>
                <a class="dropdown-item" href="#" @click="createBoard">Create board</a>
              </div>
            </div>
          </div>
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
              <div v-for="team in teams" :key="team.teamId">
                <a class="dropdown-item">{{team.name}}</a>
                <!-- <a class="dropdown-item" @click="redirectToTeam">{{team.name}}</a> -->
              </div>
              <div>
                <a class="dropdown-item" href="#" @click="createTeam">Create Team</a>
              </div>
            </div>
          </div>
        </li>
        <!-- <li class="nav-item" v-if="currentUser && currentBoard">
          <a class="nav-link">{{currentBoard.name}}</a>
        </li>
        <li class="nav-item" v-if="currentUser && currentTeam">
          <a class="nav-link">{{currentTeam.name}}</a>
        </li>-->
        <!-- 
        <li class="nav-item" v-if="showAdminBoard">
          <a href="/admin" class="nav-link">Admin Board</a>
        </li>

        <li class="nav-item" v-if="showModeratorBoard">
          <a href="/mod" class="nav-link">Moderator Board</a>
        </li>
        <li class="nav-item">
          <a href="/user" class="nav-link" v-if="currentUser">User</a>
        </li>-->
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
    <!-- <div class="container"> -->
    <div>
      <!-- <a>{{selectedTeam.name}}</a> -->
      <router-view />
    </div>
  </div>
</template>

<script>
import UserService from "./services/user.service";
export default {
  data() {
    return {
      boards: [],
      teams: [],
      boardTeams: {},
      selectedTeam: "Teams",
      selectedBoard: "Boards"
    };
  },
  computed: {
    currentBoard() {
      // return this.$store.state.selection.board;
      return this.$store.state.selection.selectedBoard;
    },
    currentTeam() {
      return this.$store.state.selection.selectedTeam;
    },
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
          // if (this.teams != null) {
          //   this.selectTeam(this.teams[0]);
          // }
          this.$store.dispatch("user/setTeams", this.teams);
          console.log("team retrieved");
        })
        .catch(e => {
          console.log("Error", e);
        });

      UserService.getBoards()
        .then(response => {
          console.log(response);
          this.boards = response.data;
          this.boards.forEach(function(board) {
            console.log(board.team.name);
          });

          // if (this.boards != null) {
          //   this.selectBoard(this.boards[0]);
          //   this.boards.sort(function(a, b) {
          //     if (a.team.name > b.team.name) {
          //       return -1;
          //     }
          //     if (b.team.name > b.team.name) {
          //       return 1;
          //     }
          //     return 0;
          //   });
          // }
          console.log("boards retrieved");
        })
        .then(() => {
          this.boardTeams = {};
          this.boards.forEach(board => {
            if (!this.boardTeams[board.team.name]) {
              this.boardTeams[board.team.name] = [];
            }
            this.boardTeams[board.team.name].push({
              name: board.name,
              boardId: board.boardId
            });
            console.log("added board to map");
          });
          console.log(this.boardTeams);
        })
        .catch(e => {
          console.log("Error", e);
        });
    },

    redirectToTeam(teamName) {
      console.log("redirection");
      let team = this.getTeamByName(teamName);
      if (team) {
        console.log("team found");
        this.selectTeam(team);
        this.$router.push("/team-info").catch(err => {
          console.log(err);
        });
      } else {
        console.log("team not found");
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
    selectTeam(team) {
      console.log("team selected");
      this.selectedTeam = team;
      this.$store.dispatch("selection/setSelectedTeam", team);
    },
    selectBoard(board) {
      // localStorage.clear();
      console.log("board selected");
      this.selectedBoard = board;
      this.$store.dispatch("selection/setSelectedBoard", board);
    },
    switchToBoard(teamName, board) {
      let team = this.getTeamByName(teamName);
      if (team) {
        console.log("board switching");
        this.selectBoard(board);
        this.selectTeam(team);
        let boardId = board.boardId;
        let boardName = board.name;
        this.$router.push({ name: "board", params: { boardId, boardName } });
        this.$route.params.pathMatch;
      } else {
        console.log("team does not exists");
      }
    }
  },
  mounted() {
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
<style lang="css">
.btn {
  margin-right: 10px !important;
}
</style>