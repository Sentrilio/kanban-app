<template>
  <div>
    <li>
      <div class="dropdown" v-if="currentUser">
        <button
          class="btn btn-secondary dropdown-toggle"
          type="button"
          id="dropdownMenuButton"
          data-toggle="dropdown"
          aria-haspopup="true"
          aria-expanded="false"
        >{{selectedTeam}}</button>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
          <li v-for="team in teams" v-on:click="selectTeam(team)" v-bind:key="team.id">
            <a class="dropdown-item" href="#">{{team.name}}</a>
          </li>
          <a class="dropdown-item" href="/team/create">Create Team</a>
        </div>
      </div>
    </li>
    <form @submit.prevent="createBoard">
      <input type="text" v-model="boardName" />
      <button type="submit">Create Board</button>
    </form>
  </div>
</template>

<script>
import UserService from "../services/user.service";

export default {
  name: "CreateBoard",

  data() {
    return {
      boardName: ""
    };
  },
  mounted() {},
  methods: {
    createBoard() {
      UserService.createBoard(
        this.boardName,
        this.$store.state.selectedTeam.teamId
      ).then(
        response => {
          console.log(response);
          this.$router.push("/"); //should be prompted info about successful creation and ok to click
        },
        error => {
          console.log(error.response);
        }
      );
    }
  }
};
</script>
<style lang="scss" scoped>
</style>