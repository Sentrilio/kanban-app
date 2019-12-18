<template>
  <div class="container" v-if="currentUser">
    <a>{{teamName}}</a>
  </div>
</template>

<script>
// import UserService from '../services/user.service';

export default {
  name: "Team",
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
    // getTeam() {
    //   this.teamName = this.$store.state.selection.team.name;
    // }
  },
  data() {
    return {
      teamName: "TEAM NAME"
    };
  },
  methods: {
    getTeam() {
      let team = this.$store.state.selection.team;
      // UserService.getTeam();
      if(team){
          console.log("team exists");
          this.teamName=team.name;
      }else{
          console.log("team not exists");
      }
    }
  },
  mounted() {
    if (this.currentUser) {
      this.getTeam();
    }
  },
  watch: {
    // call again the method if the route changes
    $route: "getTeam"
  }
};
</script>
<style lang="scss" scoped>
</style>