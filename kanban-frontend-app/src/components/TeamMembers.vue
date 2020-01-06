<template>
  <div class="members">
    <header>Team members ({{teamMembers.length}})</header>
    <br />
    <div v-for="member in teamMembers" :key="member.user.id">
      <TeamMember :member="member"></TeamMember>
    </div>
    <br>
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
  </div>
</template>
<script>
import TeamService from "../services/TeamService";
import TeamMember from "./TeamMember";
export default {
  components: {
    TeamMember
  },
  props: {
    members: Array
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
.members {
  margin-left: 20%;
  margin-right: 20%;
}
header {
  margin-top: 15px;
}
</style>