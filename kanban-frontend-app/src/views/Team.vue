<template>
  <div class="container" v-if="team">
    <a>{{team.name}}</a>
    <br>
    <a>{{team.teamId}}</a>
  </div>
</template>

<script>
import UserService from "../services/user.service";

export default {
  name: "Team",
  data() {
    return {
      team: null
    };
  },
  methods: {
    getData() {
      UserService.getTeam(this.$route.params.teamId)
        .then(response => {
          this.team = response.data;
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