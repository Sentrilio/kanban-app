<template>
  <div>
    <form @submit.prevent="createTeam">
      <input type="text" v-model="teamName" />
      <button type="submit">Create Team</button>
    </form>
    <a>{{teamName}}</a>
    <div>

    </div>
  </div>
      
</template>

<script>
import UserService from "../services/user.service";
import { log } from 'util';

export default {
  name: "CreateTeam",

  data() {
    return {
      teamName: "",
    //   teams: []
    };
  },
  mounted() {},
  methods: {
    createTeam() {
      UserService.createTeam(this.teamName).then(
        response => {
          log(response)
          this.teams = response.data;
        },
        error => {
          log(error.response)
          this.content = error.response.data.message;
        }
      );
    }
  }
};
</script>
<style lang="scss" scoped>
</style>