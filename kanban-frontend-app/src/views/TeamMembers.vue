<template>
  <div class="container">
    <header>Team members ({{teamMembers.length}})</header>
    <br />
    <form name="form" @submit.prevent="handleAddUser">
      <input
        type="email"
        class="form-control"
        name="email"
        v-model="userEmail"
        v-validate="'required|email'"
        placeholder="user@kanban.com"
      />
      <button>Add User</button>
    </form>
    <div v-for="member in teamMembers" :key="member.user.id">
      <TeamMember :member="member"></TeamMember>
      <!-- {{member.user.username}} -->
      </div>
  </div>
</template>
<script>
import TeamService from "../services/TeamService";
import TeamMember from "../components/TeamMember";
export default {
  components:{
    TeamMember
  },
  data() {
    return {
      userEmail: "",
      teamMembers: []
    };
  },
  methods: {
    getData() {
      this.getTeamMembers();
    },
    addUserToTeam() {
      TeamService.addUserToTeam(this.$route.params.teamId, this.userEmail)
        .then(response => {
          console.log(response);
        })
        .catch(err => {
          console.log(err);
        });
    },
    handleAddUser() {
      this.$validator.validate().then(valid => {
        if (valid) {
          console.log("valid");
          this.addUserToTeam();
        } else {
          console.log("invalid");
        }
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
  }
};
</script>
<style>
</style>