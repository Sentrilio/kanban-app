<template>
  <div class="container">
    <nav class="navbar navbar-light bg-light">
      <a class="navbar-brand" href="#">Navbar</a>
      <li class="nav-item btn">
        <a @click="membersClick">members</a>
      </li>
    </nav>
    <a>{{team.name}}</a>
    <br />
    <a>{{team.id}}</a>
    <div>
      <br />
      <a>Boards:</a>
      <div v-for="board in team.boards" :key="board.id">
        <a>{{board.name}}</a>
      </div>
    </div>
  </div>
</template>

<script>
import TeamService from "../services/TeamService";
import SortService from "../services/SortService";

export default {
  name: "Team",
  data() {
    return {
      team: {}
    };
  },
  methods: {
    membersClick() {
      let teamId = this.$route.params.teamId;
      let teamName = this.$route.params.teamName;
      this.$router.push({ name: "teamMembers", params: { teamId, teamName } });
    },
    getData() {
      TeamService.getTeam(this.$route.params.teamId)
        .then(response => {
          this.team = response.data;
          SortService.sortByName(this.team.boards);
        })
        .catch(error => {
          console.log(error);
        });
    }
  },
  created() {
    this.getData();
  },

  watch: {
    // call again the method if the route changes
    $route: "getData"
  }
};
</script>
<style lang="scss" scoped>
</style>