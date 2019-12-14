<template>
  <div v-if="currentUser">
    <form @submit.prevent="createTeam">
      <input type="text" v-model="teamName" />
      <button type="submit">Create Team</button>
    </form>
  </div>
</template>

<script>
import UserService from "../services/user.service";

export default {
  name: "CreateTeam",
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  data() {
    return {
      teamName: "",
    };
  },
  mounted() {
  },
  methods: {

    createTeam() {
      UserService.createTeam(this.teamName)
      .then((response) => {
          console.log(response)
        },
        error => {
          console.log(error.response)
        }
      );
    }
  }
};
</script>
<style lang="scss" scoped>
</style>