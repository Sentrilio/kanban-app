<template>
  <div v-if="currentUser" class="jumbotron text-center">
    <div class="dropdown">
      <button
        class="btn btn-secondary dropdown-toggle"
        type="button"
        id="dropdownMenuButton"
        data-toggle="dropdown"
        aria-haspopup="true"
        aria-expanded="false"
      >
        <a v-if="!selectedTeam">Select Team</a>
        <a v-else>{{selectedTeam.name}}</a>
      </button>

      <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
        <li v-for="team in teams" v-on:click="selectTeam(team)" v-bind:key="team.id">
          <a class="dropdown-item" href="#">{{team.name}}</a>
        </li>
        <a class="dropdown-item" href="/team/create">Create Team</a>
      </div>
    </div>

    <form @submit.prevent="createBoard">
      <input type="text" placeholder="Board Name" v-model="boardName" />
      <button :disabled="!selectedTeam || !boardName" type="submit">Create Board</button>
    </form>
  </div>
</template>

<script>
import UserService from "../services/user.service";

export default {
  name: "CreateBoard",
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  data() {
    return {
      teams: [],
      selectedTeam: null,
      boardName: ""
    };
  },
  created() {
    this.getData();
  },
  methods: {
    getData() {
      this.getTeams();
    },
    getTeams() {
      UserService.getTeams()
        .then(response => {
          this.teams = response.data;
          console.log(this.teams);
        })
        .catch(e => {
          console.log("Error", e);
        });
    },
    selectTeam(team) {
      this.selectedTeam = team;
    },

    createBoard() {
      UserService.createBoard(this.boardName, this.selectedTeam.id).then(
        response => {
          console.log(response);
          this.$router.push("/"); //should be prompted info about successful creation and ok to click
        },
        error => {
          console.log(error.response);
        }
      );
    }
  },
  watch: {
    $route: "getTeams"
  }
};
</script>
<style lang="scss" scoped>
</style>