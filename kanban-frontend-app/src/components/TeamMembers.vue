<template>
  <div class="members">
    <header>Członkowie zespołu ({{teamMembers.length}})</header>
    <br />
    <div v-for="member in teamMembers" :key="member.user.id">
      <TeamMember :member="member"></TeamMember>
    </div>
    <br />
    <form name="form" @submit.prevent="handleAddUser">
      <input
        type="email"
        class="form-control"
        name="email"
        v-model="userEmail"
        v-validate="'required|email'"
        placeholder="użytkownik@email.com"
      />
      <button>Dodaj użytkownika</button>
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
          this.getData();
        })
        .catch(err => {
          console.log(err);
        });
    },
    handleAddUser() {
      this.$validator.validate().then(valid => {
        if (valid) {
          this.addUserToTeam();
        } else {
          console.log("invalid email");
        }
      });
    },
    getTeamMembers() {
      TeamService.getTeamMembers(this.$route.params.teamId)
        .then(response => {
          this.teamMembers = response.data;
        })
        .catch(err => {
          if (err.response.status === 404) {
            console.log("User not found");
          }
          console.log(err);
        });
    }
  },
  created() {
    this.getData();
  },
  updated(){
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