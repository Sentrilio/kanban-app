<template>
  <div class="jumbotron text-center">
    <div v-if="currentUser">
      <form @submit.prevent="createTeam">
        <input type="text" placeholder="Team Name" v-model="teamName" />
        <button :disabled="!teamName" type="submit">Create Team</button>
      </form>
    </div>
  </div>
</template>

<script>
import TeamService from "../services/TeamService";

export default {
  name: "CreateTeam",
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  data() {
    return {
      teamName: ""
    };
  },
  mounted() {},
  methods: {
    createTeam() {
      TeamService.createTeam(this.teamName).then(
        response => {
          console.log(response);
          this.$router.push("/");
        },
        error => {
          console.log(error.response);
        }
      );
    }
  }
};
</script>
<style lang="css">
/* .jumbotron{ */
/* background-color: blue !important; */
/* przy tworzeniu klasy możliwe, że też important zadziała */
/* } */
</style>