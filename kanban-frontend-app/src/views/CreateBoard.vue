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
      >{{selectedTeam.name}}</button>
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
      selectedTeam: null,
      boardName: "",
      teams: []
    };
  },
  mounted() {
    this.teams = this.$store.state.user.teams;
    if (this.teams != null) {
      this.selectedTeam = this.teams[0];
    }
  },
  methods: {
    selectTeam(team) {
      this.selectedTeam = team;
      this.$store.dispatch("selection/setSelectedTeam", team);
    },
    getTeams() {
      this.teams = this.$store.state.user.teams;
    },
    createBoard() {
      UserService.createBoard(this.boardName, this.selectedTeam.teamId).then(
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
    // call again the method if the route changes
    $route: "getTeams"
  }
};
</script>
<style lang="scss" scoped>
</style>