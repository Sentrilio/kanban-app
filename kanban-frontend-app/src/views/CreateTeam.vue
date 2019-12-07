<template>
  <div>
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