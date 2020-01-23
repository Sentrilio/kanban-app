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
          <a v-if="!selectedTeam">Wybierz zespół</a>
          <a v-else>{{selectedTeam.name}}</a>
        </button>
      <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
        <li v-for="team in teams" v-on:click="selectTeam(team)" v-bind:key="team.id">
          <a class="dropdown-item" href="#">{{team.name}}</a>
        </li>
        <a class="dropdown-item" href="/team/create">Stwórz zespół</a>
      </div>
    </div>

    <form @submit.prevent="createBoard">
      <input type="text" placeholder="Nazwa tablicy" v-model="boardName" />
      <button :disabled="!selectedTeam || !boardName" type="submit">Stwórz tablicę</button>
    </form>
  </div>
</template>

<script>
import BoardService from "../services/BoardService";
import TeamService from "../services/TeamService";

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
      TeamService.getTeams()
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
      BoardService.createBoard(this.boardName, this.selectedTeam.id).then(
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
div {
  margin-bottom: 15px;
}
</style>