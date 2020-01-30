<template>
  <div class="team">
    <div class="team-header">
      <h1>{{team.name}}</h1>
    </div>
    <div>
      <div class="btn-group" role="group" aria-label="Basic example">
        <button type="button" @click="boardsClick" class="btn btn-secondary">Tablice</button>
        <button type="button" @click="membersClick" class="btn btn-secondary">Członkowie</button>
        <button type="button" @click="settingsClick" class="btn btn-secondary">Ustawienia</button>
      </div>
      <div class="selected-content">
        <div v-if="selectedContent==='boards'">
          <div>
            <a>Boards:</a>
            <div v-for="board in team.boards" :key="board.id">
              <a>{{board.name}}</a>
            </div>
          </div>
        </div>
        <div v-else-if="selectedContent==='members'">
          <TeamMembers :teamMembers="teamMembers"></TeamMembers>
        </div>
        <div v-else-if="selectedContent==='settings'">
          <a>ustawienia</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TeamService from "../services/TeamService";
import SortService from "../services/SortService";
import TeamMembers from "../components/TeamMembers";
export default {
  name: "Team",
  components: {
    TeamMembers
  },
  data() {
    return {
      team: {},
      visible: true,
      selectedContent: "boards",
      teamMembers: []
    };
  },
  methods: {
    settingsClick() {
      this.selectedContent = "settings";
    },
    boardsClick() {
      this.selectedContent = "boards";
      this.getData();
    },
    membersClick() {
      this.selectedContent = "members";
      // let teamId = this.$route.params.teamId;
      // let teamName = this.$route.params.teamName;
      // this.$router.push({ name: "teamMembers", params: { teamId, teamName } });
    },
    getData() {
      this.getTeam();
      this.getTeamMembers();
    },
    getTeam() {
      TeamService.getTeam(this.$route.params.teamId)
        .then(response => {
          this.team = response.data;
          SortService.sortByName(this.team.boards);
        })
        .catch(error => {
          console.log(error);
        });
    },
    getTeamMembers() {
      TeamService.getTeamMembers(this.$route.params.teamId)
        .then(response => {
          this.teamMembers = response.data;
        })
        .catch(err => {
          console.log(err);
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
.navbar .navbar-collapse {
  text-align: center;
  align-items: center;
  align-content: center;
  display: flexbox;
  // width: 50vh !important;
}
.team {
  text-align: center;
  align-items: center;
  align-content: center;
  // width: 50vh;
}
</style>